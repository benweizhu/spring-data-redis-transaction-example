package me.zeph.spring.data.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.zeph.spring.data.redis.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StudentController {

  @Autowired
  private RedisTemplate redisTemplate;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @PostMapping(value = "/students/transaction-sleep")
  public ResponseEntity<Student> saveStudentWithSleep(@RequestBody Student student) {
    redisTemplate.execute(new SessionCallback<List<Object>>() {
      public List<Object> execute(RedisOperations operations) throws DataAccessException {
        operations.multi();
        operations.watch(String.valueOf(student.getId()));
        String studentString = null;
        try {
          Thread.sleep(10000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        try {
          studentString = objectMapper.writeValueAsString(student);
        } catch (JsonProcessingException e) {
          e.printStackTrace();
        }
        operations.opsForSet().add(String.valueOf(student.getId()), studentString);
        return operations.exec();
      }
    });
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @PostMapping(value = "/students/transaction")
  public ResponseEntity<Student> saveStudentWithoutSleep(@RequestBody Student student) {
    redisTemplate.execute(new SessionCallback<List<Object>>() {
      public List<Object> execute(RedisOperations operations) throws DataAccessException {
        operations.multi();
        String studentString = null;
        try {
          studentString = objectMapper.writeValueAsString(student);
        } catch (JsonProcessingException e) {
          e.printStackTrace();
        }
        operations.opsForSet().add(String.valueOf(student.getId()), studentString);
        return operations.exec();
      }
    });
    return new ResponseEntity<>(HttpStatus.CREATED);
  }
}

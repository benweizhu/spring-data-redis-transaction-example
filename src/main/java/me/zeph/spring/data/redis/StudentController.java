package me.zeph.spring.data.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.zeph.spring.data.redis.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StudentController {

  @Autowired
  private RedisTemplate<String, Student> redisTemplate;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @PostMapping(value = "/students/transaction-sleep")
  public ResponseEntity<Object> saveStudentWithSleep(@RequestBody Student student) {
    redisTemplate.watch(student.getId());
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    redisTemplate.multi();
    redisTemplate.opsForValue().set(String.valueOf(student.getId()), student);
    List<Object> exec = redisTemplate.exec();
    return new ResponseEntity<>(exec, HttpStatus.CREATED);
  }

  @PostMapping(value = "/students/transaction")
  public ResponseEntity<Object> saveStudentWithoutSleep(@RequestBody Student student) {
    redisTemplate.opsForValue().set(String.valueOf(student.getId()), student);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

}

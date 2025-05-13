package pro.sky.hogwarts.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.hogwarts.model.Students;
import pro.sky.hogwarts.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/student-query")
public class StudentQueryController {

    private final StudentService studentService;

    public StudentQueryController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getCountOfStudents() {
        return ResponseEntity.ok().body(studentService.getCountStudents());
    }

    @GetMapping("/average-age")
    public ResponseEntity<Double> getAverageAgeOfStudents() {
        return ResponseEntity.ok().body(studentService.getAverageAge());
    }

    @GetMapping("/5-last-students")
    public ResponseEntity<List<Students>> getLast5Students() {
        return ResponseEntity.ok().body(studentService.getLast5Students());
    }
}

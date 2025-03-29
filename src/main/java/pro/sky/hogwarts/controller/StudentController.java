package pro.sky.hogwarts.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.hogwarts.model.Students;
import pro.sky.hogwarts.service.StudentService;

import java.util.Collection;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Students> getStudent(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @GetMapping
    public ResponseEntity<Collection<Students>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @PostMapping
    public ResponseEntity<Students> createStudent(@RequestBody Students students) {
        Students createdStudents = studentService.createStudent(students);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudents);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudentById(id);
        return ResponseEntity.ok("Student is deleted");
    }

    @PutMapping("/edit")
    public ResponseEntity<Students> editStudent(@PathVariable Students students) {
        return ResponseEntity.ok(studentService.editStudent(students));
    }

    @GetMapping("/findByAge/{age}")
    public ResponseEntity<Collection<Students>> findByAge(@PathVariable int age) {
        return ResponseEntity.ok(studentService.findByAge(age));
    }
}

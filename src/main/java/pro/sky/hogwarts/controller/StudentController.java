package pro.sky.hogwarts.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pro.sky.hogwarts.model.Faculty;
import pro.sky.hogwarts.model.Students;
import pro.sky.hogwarts.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Students> createStudent(@RequestBody Students students) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.createStudent(students));
    }

    @PutMapping()
    public ResponseEntity<Students> editStudent(@RequestBody Students students) {
        Students foundStudent = studentService.editStudent(students);
        if (foundStudent == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            return ResponseEntity.ok(foundStudent);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Students> getStudent(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @GetMapping
    public ResponseEntity<List<Students>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/ByAge/{age}")
    public ResponseEntity<List<Students>> findByAge(@PathVariable int age) {
        return ResponseEntity.ok(studentService.findByAge(age));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudentById(id);
        return ResponseEntity.ok("Student is deleted");
    }

    @GetMapping("/byAgeBetween")
    public ResponseEntity<List<Students>> getAgeBetween(
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge) {
        return ResponseEntity.ok(studentService.findByAgeBetween(minAge, maxAge));
    }

    @GetMapping("/get-faculty/{id}")
    public ResponseEntity<Faculty> getFacultyByStudentId(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getFacultyByStudentId(id));
    }

    @GetMapping("/students/print-parallel")
    public void printParallel() {
        studentService.printStudentsParallel();
    }

    @GetMapping("/students/print-synchronized")
    public void printSynchronized() {
        studentService.printStudentsSynchronized();
    }
}

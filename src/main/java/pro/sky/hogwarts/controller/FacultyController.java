package pro.sky.hogwarts.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.hogwarts.model.Faculty;
import pro.sky.hogwarts.model.Students;
import pro.sky.hogwarts.service.FacultyService;

import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public ResponseEntity<Faculty> createFaculty(@RequestBody Faculty faculty) {
        Faculty createtedFaculty = facultyService.createFaculty(faculty);
        return ResponseEntity.status(HttpStatus.CREATED).body(createtedFaculty);
    }

    @PutMapping
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        return ResponseEntity.ok(facultyService.editFaculty(faculty));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Faculty> findFacultyById(@PathVariable Long id) {
        return ResponseEntity.ok(facultyService.getFacultyById(id));
    }

    @GetMapping
    public ResponseEntity<List<Faculty>> findAllFaculty() {
        return ResponseEntity.ok(facultyService.getAllFaculties());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/find/{strSearch}")
    public ResponseEntity<List<Faculty>> findFaculty(@PathVariable String strSearch) {
        return ResponseEntity.ok(facultyService.findFaculty(strSearch));
    }

    @GetMapping("/get-students/{id}")
    public ResponseEntity<List<Students>> getStudentsByFacultyId(@PathVariable Long id) {
        return ResponseEntity.ok(facultyService.getStudentsByFacultyId(id));
    }
}

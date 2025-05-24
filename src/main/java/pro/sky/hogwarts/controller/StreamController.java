package pro.sky.hogwarts.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.hogwarts.service.FacultyService;
import pro.sky.hogwarts.service.StudentService;

import java.util.List;
import java.util.stream.LongStream;

@RestController
@RequestMapping("/stream" )
public class StreamController {

    private final StudentService studentService;
    private final FacultyService facultyService;

    public StreamController(StudentService studentService, FacultyService facultyService) {
        this.studentService = studentService;
        this.facultyService = facultyService;
    }

    @GetMapping("/all-starts-with-a" )
    public ResponseEntity<List<String>> getAllStudentStartsWithA() {

        return ResponseEntity.ok().body(studentService.getAllStudentsWithNameStartWithA());
    }

    @GetMapping("/average-age" )
    public ResponseEntity<Double> getAverageAge() {

        return ResponseEntity.ok().body(studentService.getAverageAgeWithStream());
    }

    @GetMapping("/longest-faculty-name" )
    public ResponseEntity<String> getLongestFacultyName() {

        return ResponseEntity.ok().body(facultyService.getLongestFacultyName());
    }

    @GetMapping("/return-value" )
    public ResponseEntity<Long> returnValue() {

        long startTime = System.nanoTime();

        long sum = LongStream.rangeClosed(1, 1_000_000)
                .parallel()
                .sum();

        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000;
        System.out.println(duration + " мкс" );

        return ResponseEntity.ok().body(sum);
    }
}

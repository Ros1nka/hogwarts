package pro.sky.hogwarts.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pro.sky.hogwarts.model.Faculty;
import pro.sky.hogwarts.model.Students;
import pro.sky.hogwarts.repository.StudentRepository;
import pro.sky.hogwarts.service.FacultyService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/students";
    }

    @Test
    public void contextLoads() throws Exception {
        Assertions
                .assertThat(studentController)
                .isNotNull();
    }

    @Test
    public void shouldReturnCreatedStudent() {

        Students student = new Students();
        student.setAge(123);
        student.setName("Test Student");

        ResponseEntity<Students> response = restTemplate.postForEntity(baseUrl, student, Students.class);

        Assertions
                .assertThat(response.getBody())
                .isNotNull();
        Assertions
                .assertThat(HttpStatus.CREATED)
                .isEqualTo(response.getStatusCode());
        Assertions
                .assertThat("Test Student")
                .isEqualTo(response.getBody().getName());
        Assertions
                .assertThat(123)
                .isEqualTo(response.getBody().getAge());
        Assertions
                .assertThat(response.getBody().getId())
                .isNotNull();

        studentController.deleteStudent(response.getBody().getId());
    }

    @Test
    void shouldEditExistingStudent() {
        Students student = new Students();
        student.setAge(123);
        student.setName("Test Student");
        Students savedStudent = studentRepository.save(student);

        Students editedStudent = new Students();
        editedStudent.setId(savedStudent.getId());
        editedStudent.setAge(321);
        editedStudent.setName("Edited Student");

        ResponseEntity<Students> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.PUT,
                new HttpEntity<>(editedStudent),
                Students.class);

        Assertions
                .assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        Assertions
                .assertThat(response.getBody())
                .isNotNull();
        Assertions
                .assertThat(response.getBody())
                .extracting("id", "name", "age")
                .containsExactly(savedStudent.getId(), "Edited Student", 321);

        studentController.deleteStudent(savedStudent.getId());
    }

    @Test
    void shouldReturnRightStudentById() {
        Students student = new Students();
        student.setAge(123);
        student.setName("Test Student");
        Students savedStudent = studentRepository.save(student);

        ResponseEntity<Students> response = restTemplate.getForEntity(
                baseUrl + "/" + savedStudent.getId(),
                Students.class);

        Assertions
                .assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        Assertions
                .assertThat(response.getBody())
                .isNotNull();

        Assertions
                .assertThat(response.getBody())
                .extracting("id", "name", "age")
                .containsExactly(savedStudent.getId(), "Test Student", 123);

        studentController.deleteStudent(savedStudent.getId());
    }

    @Test
    public void shouldGetAllStudents() throws Exception {

        Students student1 = new Students();
        student1.setAge(123);
        student1.setName("Test Student");
        Students savedStudent1 = studentRepository.save(student1);

        Students student2 = new Students();
        student2.setAge(321);
        student2.setName("Test2 Student");
        Students savedStudent2 = studentRepository.save(student2);

        studentRepository.saveAll(List.of(savedStudent1, savedStudent2));

        ResponseEntity<List> response = restTemplate.getForEntity(
                baseUrl, List.class);

        Assertions
                .assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        Assertions
                .assertThat(response.getBody())
                .isNotNull();
        Assertions
                .assertThat(response.getBody()
                        .size() >= 2).isTrue();

        studentController.deleteStudent(savedStudent1.getId());
        studentController.deleteStudent(savedStudent2.getId());
    }

    @Test
    void shouldReturnStudentsByAge() {

        Students student = new Students();
        student.setAge(123);
        student.setName("Test Student");
        Students savedStudent = studentRepository.save(student);

        ResponseEntity<Students[]> response = restTemplate.getForEntity(
                baseUrl + "/ByAge/" + savedStudent.getAge(),
                Students[].class);

        Assertions
                .assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        List<Students> students = Arrays.asList(response.getBody());

        Assertions
                .assertThat(students)
                .isNotEmpty();
        Assertions
                .assertThat(students)
                .anyMatch(s -> "Test Student".equals(s.getName()) && s.getAge() == 123);

        studentController.deleteStudent(savedStudent.getId());
    }

    @Test
    void shouldDeleteStudent() {

        Students student = new Students();
        student.setAge(123);
        student.setName("Test Student");
        Students savedStudent = studentRepository.save(student);

        Long id = savedStudent.getId();

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/" + id,
                HttpMethod.DELETE,
                null,
                String.class);

        Assertions
                .assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        Assertions
                .assertThat(response.getBody())
                .contains("Student is deleted");

        Optional<Students> studentOptional = studentRepository.findById(id);
        Assertions
                .assertThat(studentOptional)
                .isEmpty();
    }

    @Test
    void shouldReturnStudentsByAgeBetween() {
        Students student1 = new Students();
        student1.setAge(123);
        student1.setName("Test Student1");
        Students savedStudent1 = studentRepository.save(student1);

        Students student2 = new Students();
        student2.setAge(99);
        student2.setName("Test Student2");
        Students savedStudent2 = studentRepository.save(student2);

        int minAge = 100;
        int maxAge = 200;

        ResponseEntity<Students[]> response = restTemplate.getForEntity(
                baseUrl + "/byAgeBetween?minAge=" + minAge + "&maxAge=" + maxAge, Students[].class);

        List<Students> students = List.of(response.getBody());
        Assertions
                .assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        Assertions
                .assertThat(students)
                .isNotEmpty();
        Assertions
                .assertThat(students)
                .anyMatch(s -> "Test Student1".equals(s.getName()));
        Assertions
                .assertThat(students)
                .doesNotContain(savedStudent2);

        studentController.deleteStudent(savedStudent1.getId());
        studentController.deleteStudent(savedStudent2.getId());
    }

    @Test
    void shouldReturnFacultyByStudentId() {
        Students student = new Students();
        student.setAge(123);
        student.setName("Test Student");
        student.setFaculty(facultyService.findFaculty("leaf").get(0));
        Students savedStudent = studentRepository.save(student);

        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                baseUrl + "/get-faculty/" + savedStudent.getId(), Faculty.class);

        Assertions
                .assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        Assertions
                .assertThat(response.getBody())
                .isNotNull();
        Assertions
                .assertThat(response.getBody())
                .isEqualTo((facultyService.findFaculty("leaf").get(0)));

        studentController.deleteStudent(savedStudent.getId());
    }
}
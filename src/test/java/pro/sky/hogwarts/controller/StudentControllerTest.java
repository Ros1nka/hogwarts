package pro.sky.hogwarts.controller;

import org.assertj.core.api.Assertions;
import org.checkerframework.checker.units.qual.A;
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
import pro.sky.hogwarts.model.Students;
import pro.sky.hogwarts.repository.StudentRepository;

import java.util.Arrays;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private StudentRepository studentRepository;

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
    }

    @Test
    public void shouldGetAllStudents() throws Exception {

        Students student1 = new Students();
        student1.setAge(123);
        student1.setName("Test Student");

        Students student2 = new Students();
        student2.setAge(321);
        student2.setName("Test2 Student2");

        studentRepository.saveAll(List.of(student1, student2));

        ResponseEntity<List> response = restTemplate.getForEntity(baseUrl, List.class);

        Assertions
                .assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        Assertions
                .assertThat(response.getBody())
                .isNotNull();
        Assertions
                .assertThat(response.getBody().size() >= 2).isTrue();
    }

    @Test
    void shouldReturnStudentsWithGivenAge() {

        Students student = new Students();
        student.setAge(123);
        student.setName("Test Student");
        Students savedStudent = studentRepository.save(student);

        ResponseEntity<List> response = restTemplate.getForEntity(
                baseUrl + "/ByAge/" + savedStudent.getAge(),
                List.class);

        Assertions
                .assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        Assertions
                .assertThat(response.getBody())
                .isNotNull();
        Assertions
                .assertThat(!response.getBody().isEmpty()).isTrue();
    }

    @Test
    void shouldDeleteStudent() {

//        Students student = new Students();
//        student.setAge(123);
//        student.setName("Test Student");
//        Students savedStudent = studentRepository.save(student);

//        Long id = savedStudent.getId();
        long id = 2002L;

        restTemplate.delete(baseUrl + "/" + id);

        Assertions
                .assertThat(this.restTemplate.getForObject(baseUrl + "/" + id, String.class))
                .contains("Student is deleted");
    }

    @Test
    void getAgeBetween() {
    }

    @Test
    void getFacultyByStudentId() {
    }
}
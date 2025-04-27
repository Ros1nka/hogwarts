package pro.sky.hogwarts.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import pro.sky.hogwarts.model.Students;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;
    
    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/students";
    }

    @Test
    void testCreateStudent() throw Exception {
        Students student = new Students();
        student.setAge(123);
        student.setName("Test Test");

        ResponseEntity<Students> response = restTemplate.postForObject(baseUrl, student, String);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Test Test", response.getBody().getName());
        assertEquals(123, response.getBody().getAge());
    }

    @Test
    void testEditStudent() {
    }

    @Test
    void getStudent() {
    }

    @Test
    public void testGetAllStudents() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/students", String.class))
                .isNotNull();
    }

    @Test
    void findByAge() {
    }

    @Test
    void testDeleteStudent() throws Exception {

        Students student = new Students();
        student.setAge(123);
        student.setName("Harry Potter");

        this.restTemplate.postForObject("http://localhost:" + port + "/students", student, String.class);

        Long id = student.getId();

        Assertions.assertThat(studentController.getStudent(id)).isNotNull();

        restTemplate.delete("http://localhost:" + port + "/students/" + id);

        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/students/" + id, String.class))
                .contains("Student is deleted");

    }

    @Test
    void getAgeBetween() {
    }

    @Test
    void getFacultyByStudentId() {
    }
}

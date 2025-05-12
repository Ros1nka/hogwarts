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
import pro.sky.hogwarts.repository.FacultyRepository;
import pro.sky.hogwarts.repository.StudentRepository;
import pro.sky.hogwarts.service.FacultyService;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    StudentRepository studentRepository;

    private String baseUrl;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/faculty";
    }

    private Faculty createFaculty(Long id, String name, String color) {
        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);
        return faculty;
    }

    @Test
    void shouldReturnCreateFaculty() {

        Faculty faculty = createFaculty(null, "TestFaculty", "red");

        ResponseEntity<Faculty> responseEntity = restTemplate.postForEntity(baseUrl, faculty, Faculty.class);

        Assertions
                .assertThat(responseEntity.getBody())
                .isNotNull();
        Assertions
                .assertThat(HttpStatus.CREATED)
                .isEqualTo(responseEntity.getStatusCode());
        Assertions
                .assertThat(faculty.getName())
                .isEqualTo(responseEntity.getBody().getName());
        Assertions
                .assertThat(faculty.getColor())
                .isEqualTo(responseEntity.getBody().getColor());
        Assertions
                .assertThat(responseEntity.getBody().getId())
                .isNotNull();

        facultyController.deleteFaculty(responseEntity.getBody().getId());
    }

    @Test
    void shouldEditExistingFaculty() {

        Faculty faculty = createFaculty(null, "TestFaculty", "red");
        Faculty savedFaculty = facultyService.createFaculty(faculty);

        Faculty editedFaculty = createFaculty(savedFaculty.getId(), "EditedFaculty", "blue");

        ResponseEntity<Faculty> responseEntity = restTemplate.exchange(
                baseUrl,
                HttpMethod.PUT,
                new HttpEntity<>(editedFaculty),
                Faculty.class);

        Assertions
                .assertThat(responseEntity.getBody())
                .isNotNull();
        Assertions
                .assertThat(HttpStatus.OK)
                .isEqualTo(responseEntity.getStatusCode());
        Assertions
                .assertThat(responseEntity.getBody())
                .extracting("id", "name", "color")
                .containsExactly(savedFaculty.getId(), editedFaculty.getName(), editedFaculty.getColor());

        facultyController.deleteFaculty(responseEntity.getBody().getId());
    }

    @Test
    void shouldReturnRightFacultyById() {

        Faculty faculty = createFaculty(null, "TestFaculty", "red");
        Faculty savedFaculty = facultyService.createFaculty(faculty);

        ResponseEntity<Faculty> responseEntity = restTemplate.getForEntity(
                baseUrl + "/" + savedFaculty.getId(), Faculty.class);

        Assertions
                .assertThat(responseEntity.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        Assertions
                .assertThat(responseEntity.getBody())
                .isNotNull();
        Assertions
                .assertThat(responseEntity.getBody())
                .extracting("id", "name", "color")
                .containsExactly(savedFaculty.getId(), savedFaculty.getName(), savedFaculty.getColor());

        facultyController.deleteFaculty(responseEntity.getBody().getId());
    }

    @Test
    void shouldReturnAllFaculty() {

        Faculty faculty = createFaculty(null, "TestFaculty", "red");
        Faculty faculty2 = createFaculty(null, "TestFaculty2", "blue");

        facultyRepository.saveAll(List.of(faculty, faculty2));

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

        facultyController.deleteFaculty(faculty.getId());
        facultyController.deleteFaculty(faculty2.getId());
    }

    @Test
    void shouldDeleteFaculty() {

        Faculty faculty = createFaculty(null, "TestFaculty", "red");
        Faculty savedFaculty = facultyService.createFaculty(faculty);

        ResponseEntity<Faculty> responseEntity = restTemplate.exchange(
                baseUrl + "/" + savedFaculty.getId(),
                HttpMethod.DELETE,
                null,
                Faculty.class);

        Assertions
                .assertThat(responseEntity.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        Assertions
                .assertThat(facultyRepository.findById(savedFaculty.getId()))
                .isEmpty();
    }

    @Test
    void shouldReturnFoundFacultiesByNameOrColor() {

        Faculty faculty = createFaculty(null, "TestFaculty", "red");
        Faculty savedFaculty = facultyService.createFaculty(faculty);

        Faculty faculty2 = createFaculty(null, "TestFaculty2", "blue");
        Faculty savedFaculty2 = facultyService.createFaculty(faculty2);

        String str = "testfaculty";

        //поиск по имени
        ResponseEntity<Faculty[]> response = restTemplate.getForEntity(
                baseUrl + "/find/" + str, Faculty[].class);

        String str2 = "blue";

        //поиск по цвету
        ResponseEntity<Faculty[]> response2 = restTemplate.getForEntity(
                baseUrl + "/find/" + str2, Faculty[].class);

        Assertions
                .assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        Assertions
                .assertThat(response.getBody())
                .isNotNull();

        //должны быть найдены оба факультета
        List<Faculty> faculties = List.of(response.getBody());
        Assertions
                .assertThat(faculties)
                .anyMatch(f -> f.getName().equals(savedFaculty.getName()))
                .anyMatch(f2 -> f2.getName().equals(savedFaculty2.getName()));

        //должен быть найден только один факультет
        List<Faculty> faculties2 = List.of(response2.getBody());
        Assertions
                .assertThat(faculties2)
                .anyMatch(f -> f.getName().equals(savedFaculty2.getName()))
                .doesNotContain(faculty);

        facultyController.deleteFaculty(savedFaculty.getId());
        facultyController.deleteFaculty(savedFaculty2.getId());
    }

    @Test
    void shouldReturnStudentsByFacultyId() {

        Faculty faculty = createFaculty(null, "TestFaculty", "red");
        Faculty savedFaculty = facultyService.createFaculty(faculty);

        Students student1 = new Students();
        student1.setAge(123);
        student1.setName("Test Student1");
        student1.setFaculty(faculty);
        studentRepository.save(student1);

        Students student2 = new Students();
        student2.setAge(99);
        student2.setName("Test Student2");
        student2.setFaculty(faculty);
        studentRepository.save(student2);

        ResponseEntity<Students[]> response = restTemplate.getForEntity(
                baseUrl + "/get-students/" + savedFaculty.getId(), Students[].class);

        Assertions
                .assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        Assertions
                .assertThat(response.getBody())
                .isNotNull();
        Assertions
                .assertThat(response.getBody())
                .anyMatch(s -> s.getName().equals(student1.getName()))
                .anyMatch(s -> s.getName().equals(student2.getName()));

        studentRepository.delete(student1);
        studentRepository.delete(student2);
        facultyController.deleteFaculty(savedFaculty.getId());
    }
}
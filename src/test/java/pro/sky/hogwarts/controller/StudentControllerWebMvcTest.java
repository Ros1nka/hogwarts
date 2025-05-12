package pro.sky.hogwarts.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.*;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.hogwarts.model.Faculty;
import pro.sky.hogwarts.model.Students;
import pro.sky.hogwarts.repository.StudentRepository;
import pro.sky.hogwarts.service.FacultyService;
import pro.sky.hogwarts.service.StudentService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = StudentController.class)
class StudentControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    @MockitoBean
    private FacultyService facultyService;

    private Students createTestStudent(Long id, String name, int age, Faculty faculty) {
        Students student = new Students();
        student.setId(id);
        student.setName(name);
        student.setAge(age);
        student.setFaculty(faculty);
        return student;
    }

    @Test
    public void shouldReturnCreatedStudent() throws Exception {

        final String name = "Test Student";
        final int age = 123;
        final long id = 1;

        JSONObject studentObject = new JSONObject();

        studentObject.put("name", name);
        studentObject.put("age", age);

        Students student = createTestStudent(id, name, age, null);

        when(studentService.createStudent(any(Students.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/students")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }

    @Test
    void shouldEditExistingStudent() throws Exception {

        final String name = "Test Student";
        final String newName = "Edited Student";
        final int age = 123;
        final int newAge = 321;
        final long id = 1;

        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);
        studentObject.put("age", age);

        Students student = createTestStudent(id, name, age, null);

        Students editedStudent = createTestStudent(id, newName, newAge, null);

        when(studentService.getStudentById(any(Long.class))).thenReturn(student);
        when(studentService.editStudent(any(Students.class))).thenReturn(editedStudent);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/students")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(newName))
                .andExpect(jsonPath("$.age").value(newAge));
    }

    @Test
    void shouldReturnRightStudentById() throws Exception {

        final String name = "Test Student";
        final int age = 123;
        final long id = 1;

        Students student = createTestStudent(id, name, age, null);

        when(studentService.getStudentById(any(Long.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }

    @Test
    public void shouldGetAllStudents() throws Exception {

        final String name1 = "Test Student1";
        final int age1 = 123;
        final long id1 = 1;

        final String name2 = "Test Student2";
        final int age2 = 234;
        final long id2 = 2;

        Students student1 = createTestStudent(id1, name1, age1, null);

        Students student2 = createTestStudent(id2, name2, age2, null);

        when(studentService.getAllStudents()).thenReturn(List.of(student1, student2));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(name1))
                .andExpect(jsonPath("$[1].name").value(name2));
    }

    @Test
    void shouldReturnStudentsByAge() throws Exception {

        Students student = createTestStudent(1L, "Test Student", 123, null);

        when(studentService.findByAge(any(Integer.class))).thenReturn(List.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/ByAge/" + 123)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].age").value(123));
    }

    @Test
    void shouldDeleteStudent() throws Exception {

        mockMvc.perform(delete("/students/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Student is deleted"));
    }

    @Test
    void shouldReturnStudentsByAgeBetween() throws Exception {
        int minAge = 100;
        int maxAge = 200;

        Students student1 = createTestStudent(1L, "Test Student", 123, null);
        Students student2 = createTestStudent(2L, "Test Student2", 234, null);

        when(studentService.findByAgeBetween(any(Integer.class), any(Integer.class))).thenReturn(List.of(student1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/byAgeBetween?minAge=" + minAge + "&maxAge=" + maxAge)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(student1.getName()))
                .andExpect(jsonPath("$[1]").doesNotExist());
    }

    @Test
    void shouldGetFacultyByStudentId() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Test Faculty");
        faculty.setColor("Red");

        Students student = createTestStudent(1L, "Test Student", 123, faculty);

        when(studentService.getFacultyByStudentId(any(Long.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/get-faculty/" + 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()));
    }
}
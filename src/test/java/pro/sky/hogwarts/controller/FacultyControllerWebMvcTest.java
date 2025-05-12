package pro.sky.hogwarts.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.hogwarts.model.Faculty;
import pro.sky.hogwarts.model.Students;
import pro.sky.hogwarts.service.FacultyService;
import pro.sky.hogwarts.service.StudentService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FacultyController.class)
class FacultyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FacultyService facultyService;

    @MockitoBean
    private StudentService studentService;


    private Faculty createFaculty(Long id, String name, String color) {
        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);
        return faculty;
    }

    @Test
    void shouldReturnCreateFaculty() throws Exception {

        final String name = "TestFaculty";
        final String color = "red";
        final long id = 1;

        JSONObject facultyObject = new JSONObject();

        facultyObject.put("name", name);
        facultyObject.put("color", color);

        Faculty faculty = createFaculty(id, name, color);

        when(facultyService.createFaculty(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    void shouldEditExistingFaculty() throws Exception {

        final String name = "TestFaculty";
        final String color = "red";
        final long id = 1;
        final String newName = "NewTestFaculty";
        final String newColor = "blue";

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", name);
        facultyObject.put("color", color);

        Faculty faculty = createFaculty(id, name, color);
        Faculty newFaculty = createFaculty(id, newName, newColor);

        when(facultyService.editFaculty(any(Faculty.class))).thenReturn(newFaculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(newName))
                .andExpect(jsonPath("$.color").value(newColor));
    }

    @Test
    void shouldReturnRightFacultyById() throws Exception {

        final String name = "TestFaculty";
        final String color = "red";
        final long id = 1;

        Faculty faculty = createFaculty(id, name, color);

        when(facultyService.getFacultyById(id)).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    void shouldReturnAllFaculty() throws Exception {
        final String name1 = "TestFaculty";
        final String color1 = "red";
        final long id1 = 1;
        final String name2 = "TestFaculty";
        final String color2 = "red";
        final long id2 = 2;

        Faculty faculty = createFaculty(id1, name1, color1);
        Faculty faculty2 = createFaculty(id2, name2, color2);

        when(facultyService.getAllFaculties()).thenReturn(List.of(faculty, faculty2));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id1))
                .andExpect(jsonPath("$[0].name").value(name1))
                .andExpect(jsonPath("$[0].color").value(color1))
                .andExpect(jsonPath("$[1].id").value(id2))
                .andExpect(jsonPath("$[1].name").value(name2))
                .andExpect(jsonPath("$[1].color").value(color2));
    }

    @Test
    void shouldDeleteFaculty() throws Exception {

        final long id = 1;

        doNothing().when(facultyService).deleteFaculty(id);

        mockMvc.perform(delete("/faculty/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnFoundFacultiesByNameOrColor() throws Exception {

        final String name1 = "TestFaculty1";
        final String color1 = "red";
        final long id1 = 1;
        final String name2 = "TestFaculty2";
        final String color2 = "blue";
        final long id2 = 2;

        Faculty faculty1 = createFaculty(id1, name1, color1);
        Faculty savedFaculty1 = facultyService.createFaculty(faculty1);

        Faculty faculty2 = createFaculty(id2, name2, color2);
        Faculty savedFaculty2 = facultyService.createFaculty(faculty2);

        String str = "test".toLowerCase();

        List<Faculty> result = List.of(faculty1, faculty2);

        when(facultyService.findFaculty(str)).thenReturn(result);

        mockMvc.perform(get("/faculty/find/" + str)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("TestFaculty1"))
                .andExpect(jsonPath("$[1].name").value("TestFaculty2"));
    }

    @Test
    void shouldReturnEmptyList_whenNoFacultiesFound() throws Exception {

        String str = "nothing".toLowerCase();
        when(facultyService.findFaculty(str)).thenReturn(List.of());

        mockMvc.perform(get("/faculty/find/" + str)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    @Test
    void shouldReturnStudentsByFacultyId() throws Exception {

        final String name = "TestFaculty";
        final String color = "red";
        final long id = 1;

        Faculty faculty = createFaculty(id, name, color);

        Students student = new Students();
        student.setId(1L);
        student.setName("TestFirstName");
        student.setAge(99);
        student.setFaculty(faculty);

        Students student2 = new Students();
        student2.setId(2L);
        student2.setName("TestSecondName");
        student2.setAge(123);
        student2.setFaculty(faculty);

        when(facultyService.getStudentsByFacultyId(id)).thenReturn(List.of(student, student2));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/get-students/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value(student.getName()))
                .andExpect(jsonPath("$[1].name").value(student2.getName()));
    }
}

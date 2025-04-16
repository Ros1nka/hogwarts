package pro.sky.hogwarts.service;

import pro.sky.hogwarts.model.Faculty;

import pro.sky.hogwarts.model.Students;

import java.util.List;

public interface FacultyService {

    Faculty createFaculty(Faculty faculty);

    Faculty editFaculty(Faculty faculty);

    List<Faculty> getAllFaculties();

    Faculty getFacultyById(Long id);

    List<Faculty> findFaculty(String strSearch);

    void deleteFaculty(Long id);

    List<Students> getStudentsByFacultyId(Long id);
}

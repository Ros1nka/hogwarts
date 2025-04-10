package pro.sky.hogwarts.service;

import pro.sky.hogwarts.model.Faculty;

import java.util.Collection;

public interface FacultyService {
    Faculty createFaculty(Faculty faculty);

    Faculty editFaculty(Faculty faculty);

    Collection<Faculty> getAllFaculties();

    Faculty getFacultyById(Long id);

    Faculty getFacultyByColor(String color);

    void deleteFaculty(Long id);
}

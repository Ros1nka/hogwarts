package pro.sky.hogwarts.service;

import pro.sky.hogwarts.model.Faculty;
import pro.sky.hogwarts.model.Students;

import java.util.Collection;

public interface FacultyService {
    Faculty createFaculty(Faculty faculty);

    Faculty editFaculty(Faculty faculty);

    Collection<Faculty> getAllFaculties();

    Faculty getFacultyById(Long id);

    Collection<Faculty> findFaculty(String strSearch);

    void deleteFaculty(Long id);

}

package pro.sky.hogwarts.service;

import pro.sky.hogwarts.model.Faculty;

import java.util.Collection;

public interface FacultyService {

    Faculty createFaculty(Faculty faculty);

    Faculty updateFaculty(Faculty faculty);

    Faculty getFaculty(Long id);

    Collection<Faculty> getAllFaculties();

    void deleteFaculty(Long id);
}

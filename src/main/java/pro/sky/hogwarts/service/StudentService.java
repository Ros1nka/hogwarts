package pro.sky.hogwarts.service;

import pro.sky.hogwarts.model.Faculty;
import pro.sky.hogwarts.model.Students;

import java.util.List;

public interface StudentService {

    Students createStudent(Students students);

    Students editStudent(Students students);

    Students getStudentById(Long id);

    void deleteStudentById(Long id);

    List<Students> getAllStudents();

    List<Students> findByAge(int age);

    List<Students> findByAgeBetween(Integer minAge, Integer maxAge);

    Faculty getFacultyByStudentId(Long id);
}
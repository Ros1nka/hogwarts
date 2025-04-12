package pro.sky.hogwarts.service;

import pro.sky.hogwarts.model.Students;

import java.util.Collection;

public interface StudentService {
    Students createStudent(Students students);

    Students editStudent(Students students);

    Students getStudentById(Long id);

    void deleteStudentById(Long id);

    Collection<Students> getAllStudents();

    Collection<Students> findByAge(int age);
}
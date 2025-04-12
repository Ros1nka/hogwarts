package pro.sky.hogwarts.service.impl;

import org.springframework.stereotype.Service;
import pro.sky.hogwarts.model.Students;
import pro.sky.hogwarts.repository.StudentRepository;
import pro.sky.hogwarts.service.StudentService;

import java.util.Collection;

@Service
public class StudentServiceImpl implements StudentService {

    StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Students createStudent(Students students) {
        return studentRepository.save(students);
    }

    @Override
    public Students editStudent(Students students) {
        studentRepository.save(students);
        return students;
    }

    @Override
    public Students getStudentById(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Student with this ID not found"));
    }

    @Override
    public void deleteStudentById(Long id) {
        studentRepository.deleteById(id);
    }

    @Override
    public Collection<Students> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Collection<Students> findByAge(int age) {
        return studentRepository.findAllByAge(age);
    }
}

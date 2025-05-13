package pro.sky.hogwarts.service.impl;

import org.springframework.stereotype.Service;
import pro.sky.hogwarts.Exception.StudentNotFoundException;
import pro.sky.hogwarts.model.Faculty;
import pro.sky.hogwarts.model.Students;
import pro.sky.hogwarts.repository.StudentRepository;
import pro.sky.hogwarts.service.StudentService;

import java.util.List;

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
        return studentRepository.save(students);
    }

    @Override
    public Students getStudentById(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException("Student with id: " + id + "not found"));
    }

    @Override
    public void deleteStudentById(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException("Student with id: " + id + " not found");
        }
        studentRepository.deleteById(id);
    }

    @Override
    public List<Students> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public List<Students> findByAge(int age) {
        return studentRepository.findAllByAge(age);
    }

    @Override
    public List<Students> findByAgeBetween(Integer minAge, Integer maxAge) {
        if (minAge == null) {
            minAge = 0;
        }
        if (maxAge == null) {
            maxAge = Integer.MAX_VALUE;
        }
        if (minAge > maxAge) {
            int temp = minAge;
            minAge = maxAge;
            maxAge = temp;
        }
        return studentRepository.findAllByAgeBetween(minAge, maxAge);
    }

    @Override
    public Faculty getFacultyByStudentId(Long id) {
        Students student = getStudentById(id);
        if (student.getFaculty() == null) {
            throw new StudentNotFoundException("Student with id: " + id + " does not have a faculty");
        }
        return student.getFaculty();
    }

    @Override
    public Integer getCountStudents() {
        return studentRepository.getCountStudents();
    }

    @Override
    public Double getAverageAge() {
        return studentRepository.getAverageAge();
    }

    @Override
    public List<Students> getLast5Students() {
            return studentRepository.getLast5Students();
    }

}

package pro.sky.hogwarts.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.hogwarts.Exception.StudentNotFoundException;
import pro.sky.hogwarts.model.Faculty;
import pro.sky.hogwarts.model.Students;
import pro.sky.hogwarts.repository.StudentRepository;
import pro.sky.hogwarts.service.StudentService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Students createStudent(Students students) {
        logger.info("Was invoked method for create student");

        return studentRepository.save(students);
    }

    @Override
    public Students editStudent(Students students) {
        logger.info("Was invoked method for edit student");

        return studentRepository.save(students);
    }

    @Override
    public Students getStudentById(Long id) {
        logger.info("Was invoked method for get student by id");

        return studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException("Student with id: " + id + "not found"));
    }

    @Override
    public void deleteStudentById(Long id) {
        logger.info("Was invoked method for delete student by id");

        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException("Student with id: " + id + " not found");
        }
        studentRepository.deleteById(id);
    }

    @Override
    public List<Students> getAllStudents() {
        logger.info("Was invoked method for get all students");

        return studentRepository.findAll();
    }

    @Override
    public List<Students> findByAge(int age) {
        logger.info("Was invoked method for get students by age");

        return studentRepository.findAllByAge(age);
    }

    @Override
    public List<Students> findByAgeBetween(Integer minAge, Integer maxAge) {
        logger.info("Was invoked method for get students by age between min and max age");

        if (minAge == null) {
            logger.warn("Min age is null, set to 0");
            minAge = 0;
        }
        if (maxAge == null) {
            logger.warn("Max age is null, set to Integer.MAX_VALUE");
            maxAge = Integer.MAX_VALUE;
        }
        if (minAge > maxAge) {
            logger.warn("Min age is bigger than max age, swap them");
            int temp = minAge;
            minAge = maxAge;
            maxAge = temp;
        }
        return studentRepository.findAllByAgeBetween(minAge, maxAge);
    }

    @Override
    public Faculty getFacultyByStudentId(Long id) {
        logger.info("Was invoked method for get faculty by student id");

        Students student = getStudentById(id);
        if (student.getFaculty() == null) {
            throw new StudentNotFoundException("Student with id: " + id + " does not have a faculty");
        }
        return student.getFaculty();
    }

    @Override
    public Integer getCountStudents() {
        logger.info("Was invoked method for get count students");

        return studentRepository.getCountStudents();
    }

    @Override
    public Double getAverageAge() {
        logger.info("Was invoked method for get average age of students");

        return studentRepository.getAverageAge();
    }

    @Override
    public List<Students> getLast5Students() {
        logger.info("Was invoked method for get last 5 students by id");

        return studentRepository.getLast5Students();
    }

    @Override
    public List<String> getAllStudentsWithNameStartWithA() {

        return (this.getAllStudents())
                .stream()
                .map(Students::getName)
                .map(String::toUpperCase)
                .filter(s -> s.startsWith("A"))
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public Double getAverageAgeWithStream() {

        return (this.getAllStudents())
                .stream()
                .mapToInt(Students::getAge)
                .average()
                .orElse(0);
    }
}

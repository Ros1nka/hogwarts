package pro.sky.hogwarts.service.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.hogwarts.Exception.FacultyNotFoundException;
import pro.sky.hogwarts.model.Faculty;
import pro.sky.hogwarts.model.Students;
import pro.sky.hogwarts.repository.FacultyRepository;
import pro.sky.hogwarts.repository.StudentRepository;
import pro.sky.hogwarts.service.FacultyService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@Service
public class FacultyServiceImpl implements FacultyService {

    Logger logger = LoggerFactory.getLogger(FacultyServiceImpl.class);

    FacultyRepository facultyRepository;
    StudentRepository studentRepository;

    public FacultyServiceImpl(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public Faculty createFaculty(Faculty faculty) {
        logger.info("Was invoked method for create faculty" );

        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty editFaculty(Faculty faculty) {
        logger.info("Was invoked method for edit faculty" );

        return facultyRepository.save(faculty);
    }

    @Override
    public List<Faculty> getAllFaculties() {
        logger.info("Was invoked method for get all faculties" );

        return facultyRepository.findAll();
    }

    @Override
    public Faculty getFacultyById(Long id) {
        return facultyRepository.findById(id).orElseThrow(() -> new FacultyNotFoundException("Faculty with id: " + id + "not found" ));
    }

    @Override
    public List<Faculty> findFaculty(String strSearch) {
        return facultyRepository.findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(strSearch, strSearch);
    }

    @Override
    public void deleteFaculty(Long id) {
        if (!facultyRepository.existsById(id)) {
            throw new FacultyNotFoundException("Faculty with id: " + id + " not found" );
        }
        facultyRepository.deleteById(id);
    }

    @Override
    public List<Students> getStudentsByFacultyId(Long id) {
        if (!facultyRepository.existsById(id)) {
            throw new FacultyNotFoundException("Faculty with id: " + id + "not found" );
        }
        return studentRepository.findAllByFacultyId(id);
    }

    @Override
    public String getLongestFacultyName() {
        return this.getAllFaculties()
                .stream()
                .map(Faculty::getName)
                .max(Comparator.comparingInt(String::length))
                .orElse("" );
    }
}


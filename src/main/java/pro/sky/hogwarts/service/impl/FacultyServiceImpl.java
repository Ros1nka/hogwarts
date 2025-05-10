package pro.sky.hogwarts.service.impl;

import org.springframework.stereotype.Service;
import pro.sky.hogwarts.Exception.FacultyNotFoundException;
import pro.sky.hogwarts.model.Faculty;
import pro.sky.hogwarts.model.Students;
import pro.sky.hogwarts.repository.FacultyRepository;
import pro.sky.hogwarts.repository.StudentRepository;
import pro.sky.hogwarts.service.FacultyService;

import java.util.List;

@Service
public class FacultyServiceImpl implements FacultyService {

    FacultyRepository facultyRepository;
    StudentRepository studentRepository;

    public FacultyServiceImpl(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty editFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    @Override
    public List<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }

    @Override
    public Faculty getFacultyById(Long id) {
        return facultyRepository.findById(id).orElseThrow(() -> new FacultyNotFoundException("Faculty with id: " + id + "not found"));
    }

    @Override
    public List<Faculty> findFaculty(String strSearch) {
        return facultyRepository.findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(strSearch, strSearch);
    }

    @Override
    public void deleteFaculty(Long id) {
        if (!facultyRepository.existsById(id)) {
            throw new FacultyNotFoundException("Faculty with id: " + id + " not found");
        }
        facultyRepository.deleteById(id);
    }

    @Override
    public List<Students> getStudentsByFacultyId(Long id) {
        if (!facultyRepository.existsById(id)) {
            throw new FacultyNotFoundException("Faculty with id: " + id + "not found");
        }
        return studentRepository.findAllByFacultyId(id);
    }
}

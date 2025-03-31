package pro.sky.hogwarts.service.impl;

import org.springframework.stereotype.Service;
import pro.sky.hogwarts.model.Faculty;
import pro.sky.hogwarts.repository.FacultyRepository;
import pro.sky.hogwarts.service.FacultyService;

import java.util.Collection;

@Service
public class FacultyServiceImpl implements FacultyService {

    FacultyRepository facultyRepository;

    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
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
    public Collection<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }

    @Override
    public Faculty getFacultyById(Long id) {
        return facultyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Факультет не найден"));
    }

    @Override
    public Faculty getFacultyByColor(String color) {
        return facultyRepository.findByColor(color);
    }

    @Override
    public void deleteFaculty(Long id) {
        facultyRepository.deleteById(id);
    }
}

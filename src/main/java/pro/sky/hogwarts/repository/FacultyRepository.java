package pro.sky.hogwarts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.hogwarts.model.Faculty;
import pro.sky.hogwarts.model.Students;

import java.util.Collection;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    Collection<Faculty> findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(String strName, String strColor);

}

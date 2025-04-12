package pro.sky.hogwarts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.hogwarts.model.Faculty;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    Faculty findByColor(String color);
}

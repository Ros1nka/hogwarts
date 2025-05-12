package pro.sky.hogwarts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.hogwarts.model.Students;

import java.util.List;

public interface StudentRepository extends JpaRepository<Students, Long> {

    List<Students> findAllByAge(int age);

    List<Students> findAllByAgeBetween(int minAge, int maxAge);

    List<Students> findAllByFacultyId(Long id);
}

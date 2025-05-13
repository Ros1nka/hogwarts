package pro.sky.hogwarts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pro.sky.hogwarts.model.Students;

import java.util.List;

public interface StudentRepository extends JpaRepository<Students, Long> {

    List<Students> findAllByAge(int age);

    List<Students> findAllByAgeBetween(int minAge, int maxAge);

    List<Students> findAllByFacultyId(Long id);

    @Query(value = "SELECT COUNT(name) FROM students", nativeQuery = true)
    Integer getCountStudents();

    @Query(value = "SELECT AVG(age) FROM students", nativeQuery = true)
    Double getAverageAge();

    @Query(value = "SELECT * FROM students ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<Students> getLast5Students();
}

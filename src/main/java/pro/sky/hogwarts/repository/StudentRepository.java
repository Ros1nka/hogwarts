package pro.sky.hogwarts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.hogwarts.model.Students;

import java.util.Collection;

public interface StudentRepository extends JpaRepository<Students, Long> {

    Collection<Students> findAllByAge(int age);
}

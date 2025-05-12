package pro.sky.hogwarts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.hogwarts.model.Avatar;

import java.util.Optional;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {

    Optional<Avatar> findByStudentId(Long studentId);
}

package pro.sky.hogwarts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.hogwarts.model.Avatar;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {
    Avatar findByStudentId(Long studentId);
}

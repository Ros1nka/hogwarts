package pro.sky.hogwarts.service;

import org.springframework.web.multipart.MultipartFile;
import pro.sky.hogwarts.model.Avatar;

import java.io.IOException;

public interface AvatarService {
    void uploadAvatar(Long studentId, MultipartFile avatar) throws IOException;

    Avatar findAvatar(Long id);
}

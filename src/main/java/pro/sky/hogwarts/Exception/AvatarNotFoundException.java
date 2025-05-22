package pro.sky.hogwarts.Exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AvatarNotFoundException extends RuntimeException {
    Logger logger = LoggerFactory.getLogger(AvatarNotFoundException.class);

    public AvatarNotFoundException(String message) {
        super(message);
        logger.error("Was invoked exception AvatarNotFoundException with message: {}", message);
    }
}

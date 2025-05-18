package pro.sky.hogwarts.Exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StudentNotFoundException extends RuntimeException {
    Logger logger = LoggerFactory.getLogger(StudentNotFoundException.class);

    public StudentNotFoundException(String message) {
        super(message);
        logger.error("Was invoked exception StudentNotFoundException with message: {}", message);
    }
}

package pro.sky.hogwarts.Exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FacultyNotFoundException extends RuntimeException {
    Logger logger = LoggerFactory.getLogger(FacultyNotFoundException.class);

    public FacultyNotFoundException(String message) {
        super(message);
        logger.error("Was invoked exception FacultyNotFoundException with message: {}", message);
    }
}

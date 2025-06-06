package pro.sky.hogwarts.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {

    Logger logger = LoggerFactory.getLogger(InfoController.class);

    @Value("${server.port}")
    private String port;

    @GetMapping("/port")
    public String getPort(){
        logger.debug("Was colling test port");
        return port;
    }
}

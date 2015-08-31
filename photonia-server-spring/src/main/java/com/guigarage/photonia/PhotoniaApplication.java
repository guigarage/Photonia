package com.guigarage.photonia;

import com.canoo.dolphin.server.DolphinController;
import com.canoo.dolphin.server.spring.DolphinPlatformSpringBootstrap;
import org.reflections.Reflections;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;

import java.util.Set;

@SpringBootApplication
@Import(DolphinPlatformSpringBootstrap.class)
public class PhotoniaApplication extends SpringBootServletInitializer {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(new Class[]{PhotoniaApplication.class}, args);
    }

}


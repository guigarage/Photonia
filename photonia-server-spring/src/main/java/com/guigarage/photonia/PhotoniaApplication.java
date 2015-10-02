package com.guigarage.photonia;

import com.canoo.dolphin.server.spring.DolphinPlatformSpringBootstrap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(DolphinPlatformSpringBootstrap.class)
public class PhotoniaApplication extends SpringBootServletInitializer {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(new Class[]{PhotoniaApplication.class}, args);
    }

}


package com.guigarage.photonia.spring;

import com.canoo.dolphin.server.spring.DolphinPlatformSpringBootstrap;
import com.guigarage.photonia.Album;
import com.guigarage.photonia.service.AsyncService;
import com.guigarage.photonia.service.PhotoniaService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;

@SpringBootApplication
@Import(DolphinPlatformSpringBootstrap.class)
public class PhotoniaApplication extends SpringBootServletInitializer {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(new Class[]{PhotoniaApplication.class}, args);
    }

    @Inject
    private AsyncService asyncService;

    @Inject
    private PhotoniaService photoniaService;

    @PostConstruct
    public void bootstrap() {

    }
}


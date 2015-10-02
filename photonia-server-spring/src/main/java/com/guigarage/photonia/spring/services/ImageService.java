package com.guigarage.photonia.spring.services;

import com.guigarage.photonia.service.PhotoniaService;
import com.guigarage.photonia.v2.ImageMetadata;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;

@Controller
@RequestMapping("/images")
public class ImageService {

    @Inject
    private PhotoniaService photoniaService;

    @ResponseBody
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImage(@PathVariable String id) throws Exception {
        ImageMetadata imageMetadata = photoniaService.getImageById(id);
        if (imageMetadata != null) {
            throw new RuntimeException("I need the folder :(");
            //return FileUtils.readFileToByteArray(imageMetadata.getFileName());
        } else {
            throw new IllegalArgumentException("Can't find image with id " + id);
        }
    }
}

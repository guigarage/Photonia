package com.guigarage.photonia.spring.services;

import com.guigarage.photonia.service.PhotoniaService;
import com.guigarage.photonia.v2.ImageMetadata;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

@Controller
@RequestMapping("/thumbs")
public class ThumbnailService {

    @Inject
    private PhotoniaService photoniaService;

    @ResponseBody
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImage(@PathVariable String id) throws Exception {
        if(id.endsWith("@2x")) {
            id = id.substring(0, id.length() - 3);
        }
        ImageMetadata imageMetadata = photoniaService.getImageById(id);
        if (imageMetadata != null) {
            BufferedImage thumb = photoniaService.getThumbnailCache().getThumbnail(imageMetadata.getUuid());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(thumb, "JPG", outputStream);
            return outputStream.toByteArray();
        } else {
            throw new IllegalArgumentException("Can't find image with id " + id);
        }
    }
}

package com.guigarage.photonia.spring.services;

import com.guigarage.photonia.Album;
import com.guigarage.photonia.service.PhotoniaService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
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
    @RequestMapping(value = "/get", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImage() throws Exception {
        Album album = photoniaService.getAlbum();
        BufferedImage image = photoniaService.getThumbnailCache().getThumb(album.getAllImages().get(0));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "JPG", outputStream);
        return outputStream.toByteArray();
    }
}

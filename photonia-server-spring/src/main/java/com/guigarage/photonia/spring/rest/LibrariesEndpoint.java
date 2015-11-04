package com.guigarage.photonia.spring.rest;

import com.guigarage.photonia.service.PhotoniaService;
import com.guigarage.photonia.v2.PhotoniaAlbum;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;


@Controller
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@RequestMapping("/library")
@ResponseBody
public class LibrariesEndpoint {

    @Inject
    private PhotoniaService photoniaService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<AlbumData> getAllAlbums() {
        List<AlbumData> ret = new ArrayList<>();
        for(PhotoniaAlbum album : photoniaService.getAllAlbums()) {
            AlbumData albumData = new AlbumData();
            albumData.setUuid(album.getMetadata().getUuid());
            albumData.setName(album.getMetadata().getFolderName());
            albumData.setImageCount(album.getMetadata().getImages().size());
            if(!album.getMetadata().getImages().isEmpty()) {
                albumData.setCoverUrl(photoniaService.getImageThumbnailUrl(album.getMetadata().getImages().get(0).getUuid()));
            }
            ret.add(albumData);
        }
        return ret;
    }

}

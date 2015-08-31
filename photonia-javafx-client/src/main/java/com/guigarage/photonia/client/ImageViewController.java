package com.guigarage.photonia.client;

import com.canoo.dolphin.client.ClientBeanManager;
import com.guigarage.photonia.controller.ImageViewBean;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageViewController {

    @FXML
    private ImageView imageView;

    private final ClientBeanManager beanManager;

    private final Routing routing;

    public ImageViewController(ClientBeanManager beanManager, Routing routing, String id) {
        this.beanManager = beanManager;
        this.routing = routing;

        beanManager.onAdded(ImageViewBean.class, bean -> update(bean));
        beanManager.send("ImageViewController:initView", new ClientBeanManager.Param("id", id));
    }

    @FXML
    public void initialize() {
    }

    private void update(ImageViewBean bean) {
        bean.getImageUrl().onChanged(e -> imageView.setImage(new Image(bean.getImageUrl().get())));
        imageView.setImage(new Image(bean.getImageUrl().get()));
    }

}

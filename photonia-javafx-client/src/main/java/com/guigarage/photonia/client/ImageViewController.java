package com.guigarage.photonia.client;

import com.canoo.dolphin.client.AbstractViewController;
import com.canoo.dolphin.client.ClientContext;
import com.canoo.dolphin.client.ControllerProxy;
import com.guigarage.photonia.controller.ImageViewBean;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageViewController extends AbstractViewController<ImageViewBean> {

    @FXML
    private ImageView imageView;

    private final Routing routing;

    private String id;

    public ImageViewController(ClientContext clientContext, Routing routing, String id) {
        super(clientContext, "ImageViewController");
        this.routing = routing;
    }

    @FXML
    public void initialize() {
    }


    @Override
    protected void init(ControllerProxy<ImageViewBean> controller) {
        ImageViewBean model = controller.getModel();
        model.idProperty().set(id);
        model.imageUrlProperty().onChanged(e -> imageView.setImage(new Image(model.imageUrlProperty().get())));
        imageView.setImage(new Image(model.imageUrlProperty().get()));
    }
}

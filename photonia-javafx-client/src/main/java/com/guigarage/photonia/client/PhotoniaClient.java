package com.guigarage.photonia.client;

import com.canoo.dolphin.client.ClientBeanManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class PhotoniaClient extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        StackPane mainPane = new StackPane();

        ClientBeanManager beanManager = ClientBeanManager.create("http://localhost:8080/dolphin");
        Routing routing = new Routing(mainPane, beanManager);

        routing.showLibrary();

        primaryStage.setScene(new Scene(mainPane));
        primaryStage.show();
    }

    public static void main(String... args) {
        launch(args);
    }
}

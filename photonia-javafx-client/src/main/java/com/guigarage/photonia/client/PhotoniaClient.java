package com.guigarage.photonia.client;

import com.canoo.dolphin.client.ClientContext;
import com.canoo.dolphin.client.ClientContextFactory;
import com.canoo.dolphin.client.javafx.JavaFXConfiguration;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.concurrent.CompletableFuture;

public class PhotoniaClient extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        StackPane mainPane = new StackPane();

        CompletableFuture<ClientContext> clientFuture = ClientContextFactory.connect(new JavaFXConfiguration("http://localhost:8080/dolphin"));
        clientFuture.thenAccept(client -> {
            Routing routing = new Routing(mainPane, client);
            routing.showLibrary();
            Scene scene = new Scene(mainPane);
            scene.getStylesheets().add(PhotoniaClient.class.getResource("style.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        });
    }

    public static void main(String... args) {
        launch(args);
    }
}

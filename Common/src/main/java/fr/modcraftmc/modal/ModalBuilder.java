package fr.modcraftmc.modal;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class ModalBuilder extends Parent {

    private FXMLLoader loader;
    private static Stage window;


    public ModalBuilder() {


        loader = new FXMLLoader(this.getClass().getClassLoader().getResource("fxml/modal.fxml"));
        Parent test = null;
        try {
            test = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(test);
        scene.setFill(Color.TRANSPARENT);

    }

}

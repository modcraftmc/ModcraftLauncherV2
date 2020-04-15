package fr.modcraftmc.crashreporter;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

public class CrashReporter {

    public static void catchException(Exception e, Window mainWindow) {

        final Stage dialog = new Stage();
        dialog.initOwner(mainWindow);
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(new Text("This is a Dialog"));
        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();

    }
}

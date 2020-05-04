import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class modal extends Application {

    private FXMLLoader loader;

    @Override
    public void start(Stage stage) throws Exception {

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);

        loader = new FXMLLoader(this.getClass().getClassLoader().getResource("fxml/modal.fxml"));
        Parent test = loader.load();

        Scene scene = new Scene(test);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);

        test.getStylesheets().add(this.getClass().getClassLoader().getResource("css/modal.css").toExternalForm());
        stage.show();

    }
}

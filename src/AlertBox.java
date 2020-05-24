package sample;

import javafx.scene.*;
import javafx.stage.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class AlertBox {

    public static void display(String title, String message) {
        Stage window = new Stage();

        //prevent user from interacting with other windows until this window is taken care of
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        //Alert message
        Label label = new Label();
        label.setText(message);

        //Close button
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER); //centre the elements

        Scene scene = new Scene(layout);
        window.setScene(scene);

        //the window needs to be closed before going back to the Main. Will block any user interaction until window is closed
        window.showAndWait();
    }

}

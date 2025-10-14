package application;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This page displays a simple welcome message for the user.
 */

public class UserHomePage {

    public void show(Stage primaryStage) {
    	VBox layout = new VBox();
    	VBox questionLayout = new VBox();
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // Label to display Hello user
	    Label userLabel = new Label("Hello, Student!");
	    userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

	    
	    Scene userScene = new Scene(layout, 800, 400);
	    //Create button and scene for adding questions
	    Scene questionScene = new Scene(questionLayout, 800, 400);
	    Button questionButton = new Button("Ask a question");
	    
	    layout.getChildren().addAll(userLabel, questionButton);
	    // Set the scene to primary stage
	    primaryStage.setScene(userScene);
	    primaryStage.setTitle("User Page");
    	
    }
}
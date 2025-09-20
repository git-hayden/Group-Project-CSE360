package application;

import java.sql.SQLException;
import java.util.Optional;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * AdminPage class represents the user interface for the admin user.
 * This page displays a simple welcome message for the admin.
 */

public class AdminHomePage {
	/**
     * Displays the admin page in the provided primary stage.
     * @param primaryStage The primary stage where the scene will be displayed.
     */
    public void show(Stage primaryStage) {
    	VBox layout = new VBox();
    	
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    //Adding buttons to admin home page
	    Button deleteUserButton = new Button("Delete a User");
	    Button changePasswordButton = new Button("Change a user password");
	    // label to display the welcome message for the admin
	    Label adminLabel = new Label("Hello, Admin!");
	    
	    adminLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    
	    layout.getChildren().addAll(adminLabel, deleteUserButton, changePasswordButton);
	    Scene adminScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(adminScene);
	    primaryStage.setTitle("Admin Page");
	    
	    //Create scene for deleting user
	    VBox deleteLayout = new VBox();
	    deleteLayout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    Scene deleteUserScene = new Scene(deleteLayout, 800, 400);
	    Label deleteLabel = new Label("Enter username to delete");
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter userName to delete");
        userNameField.setMaxWidth(250);
        //Add functionality for delete
        Button deleteUser = new Button("Delete User");
        deleteUser.setOnAction(e -> {
        	String userName = userNameField.getText();
        	if (userName == "") {
        		Alert alert = new Alert(Alert.AlertType.ERROR);
        		alert.setTitle("Invalid User");
        		alert.setContentText("Error Invalid Username");
        		alert.showAndWait();
        	}
        	DatabaseHelper databaseHelper = new DatabaseHelper();
        	try {
        		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        		confirmAlert.setTitle("Confirm Delete");
        		confirmAlert.setHeaderText("Are you sure you want to delete?");
        		confirmAlert.setContentText("Are you sure you want to delete " + userName);
        		Optional<ButtonType> confirmation = confirmAlert.showAndWait();
        		
        		if (confirmation.isPresent() && confirmation.get() == ButtonType.OK) {
        			databaseHelper.connectToDatabase();
        			databaseHelper.deleteUser(userName);
        			primaryStage.setScene(adminScene);
        		}
        	} catch(SQLException ex){
        		ex.printStackTrace();
        	}
        });
        deleteLayout.getChildren().addAll(deleteLabel, userNameField, deleteUser);
        
	    //Scene transition to deleteLayout
	    deleteUserButton.setOnAction(e -> {
	    	primaryStage.setScene(deleteUserScene);
	    });
	    
	    //Create scene for password change
	    VBox passwordLayout = new VBox();
	    passwordLayout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    Scene passwordChangeScene = new Scene(passwordLayout, 800, 400);
	    Label passwordLabel = new Label("Enter username to change password");
        TextField userNameFieldPassword = new TextField();
        userNameFieldPassword.setPromptText("Enter userName to change password");
        userNameFieldPassword.setMaxWidth(250);
	    
        //Add functionality for password change
        Button changePass = new Button("Change Password");
        changePass.setOnAction(e -> {
        	String userToChange = userNameFieldPassword.getText();
        	DatabaseHelper databaseHelper = new DatabaseHelper();
        	try {
        		databaseHelper.connectToDatabase();
        		String newPassword = databaseHelper.generatePasswordCode(userToChange);
        		Alert alert = new Alert(Alert.AlertType.INFORMATION);
        		alert.setTitle("One time password");
        		alert.setContentText(newPassword);
        		alert.showAndWait();
        		primaryStage.setScene(adminScene);
        	} catch(SQLException ex){
        		ex.printStackTrace();
        	}
        });
        
        passwordLayout.getChildren().addAll(passwordLabel, userNameFieldPassword, changePass);
	    //Scene transition to changePasswordLayout
	    changePasswordButton.setOnAction(e -> {
	    	primaryStage.setScene(passwordChangeScene);
	    });
	    
    }
}
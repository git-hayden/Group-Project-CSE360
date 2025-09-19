package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

import databasePart1.*;

/**
 * The UserLoginPage class provides a login interface for users to access their accounts.
 * It validates the user's credentials and navigates to the appropriate page upon successful login.
 */
public class UserLoginPage {
	
    private final DatabaseHelper databaseHelper;

    public UserLoginPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage) {
    	// Input field for the user's userName, password
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter userName");
        userNameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);
        
        // Label to display error messages
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");


        Button loginButton = new Button("Login");
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(userNameField, passwordField, loginButton, errorLabel);
        
        Scene loginScene = new Scene(layout, 800, 400);
        
        loginButton.setOnAction(a -> {
        	// Retrieve user inputs
            String userName = userNameField.getText();
            String password = passwordField.getText();
            try {
            	User user=new User(userName, password, "");
            	//Check if password is otp and valid
        		String passwordValidCheck = databaseHelper.checkPassword(userName);
            	//If otp is entered prompt for password change
            	if (password.length() == 4 && passwordValidCheck.equals(password)) {
            		VBox otpLayout = new VBox();
            		Button otpChangePass = new Button("Change Password");
            		otpLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");
            	    Scene otpChangeScene = new Scene(otpLayout, 800, 400);
            	    Label passwordLabel = new Label("Enter a new password");
                    TextField userNameFieldPassword = new TextField();
                    userNameFieldPassword.setPromptText("Enter new password");
                    userNameFieldPassword.setMaxWidth(250);
                    
                    //Add functionality for otp password change
                    otpChangePass.setOnAction(e -> {
                    	String newPassword = userNameFieldPassword.getText();
                    	String errPasswordMessage = PasswordEvaluator.evaluatePassword(newPassword);
                    	if (errPasswordMessage != "") {
                    		Alert alert = new Alert(Alert.AlertType.ERROR);
                    		alert.setTitle("Invalid Password");
                    		alert.setContentText(errPasswordMessage);
                    		alert.showAndWait();
                    		return;
                    	}
                    	DatabaseHelper databaseHelper = new DatabaseHelper();
                    	try {
                    		databaseHelper.connectToDatabase();
                    		databaseHelper.updateUserPassword(newPassword, userName);
                    		Alert successAlert = new Alert(Alert.AlertType.ERROR);
                    		successAlert.setTitle("Password Changed");
                    		successAlert.setContentText("You have successfully changed your password, please log back in.");
                    		successAlert.showAndWait();
                    		primaryStage.setScene(loginScene);
                    	} catch(SQLException ex){
                    		ex.printStackTrace();
                    	}
                    });
                    
                    otpLayout.getChildren().addAll(passwordLabel, userNameFieldPassword, otpChangePass);
                    primaryStage.setScene(otpChangeScene);
                    return;
            	}
            	
            	WelcomeLoginPage welcomeLoginPage = new WelcomeLoginPage(databaseHelper);
            	
            	// Retrieve the user's role from the database using userName
            	String role = databaseHelper.getUserRole(userName);
            	
            	if(role!=null) {
            		user.setRole(role);
            		if(databaseHelper.login(user)) {
            			welcomeLoginPage.show(primaryStage,user);
            		}
            		else {
            			// Display an error if the login fails
                        errorLabel.setText("Error logging in");
            		}
            	}
            	else {
            		// Display an error if the account does not exist
                    errorLabel.setText("user account doesn't exists");
            	}
            	
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
            } 
        });

        primaryStage.setScene(loginScene);
        primaryStage.setTitle("User Login");
        primaryStage.show();
    }
}

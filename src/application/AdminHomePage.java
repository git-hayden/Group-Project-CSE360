package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.SQLException;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import databasePart1.DatabaseHelper;


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
    	VBox layout = new VBox(10);
    	layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
	    
	    // label to display the welcome message for the admin
	    Label adminLabel = new Label("Hello, Admin!");
	    adminLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

		//create table
		TableView<User> userTable = createUserTable();

		//add users to table
		loadUsers(userTable);
		layout.getChildren().addAll(adminLabel, userTable);
		primaryStage.setScene(new Scene(layout, 800, 400)); //increase height for table

	    Scene adminScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(adminScene);
	    primaryStage.setTitle("Admin Page");
    }
	
	private TableView<User> createUserTable() {
		TableView<User> table = new TableView<>();
		table.setPrefWidth(600);
		table.setPrefHeight(300);

		//username column
		TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
		usernameColumn.setCellValueFactory(data -> {
			String username = data.getValue().getUserName();
			return new javafx.beans.property.SimpleStringProperty(username);
		});
		
		usernameColumn.setPrefWidth(300);

		//role column
		TableColumn<User, String> roleColumn = new TableColumn<>("Role");
		roleColumn.setCellValueFactory(data -> {
			String role = data.getValue().getRole();
			return new javafx.beans.property.SimpleStringProperty(role);
		});
		roleColumn.setPrefWidth(300);

		//add columns to table
		table.getColumns().addAll(usernameColumn, roleColumn);
		return table;
	}

	//load users into table
	private void loadUsers(TableView<User> table) {
		DatabaseHelper databaseHelper = new DatabaseHelper();
		try {
			databaseHelper.connectToDatabase();
			List<User> users = databaseHelper.getAllUsers();
			ObservableList<User> userList = FXCollections.observableArrayList(users);
			table.setItems(userList);

			//Error message if no users are found
			if (users.isEmpty()) {
				table.setPlaceholder(new Label("No users found"));
			}
		} catch (SQLException e) {
			//Alert if error occurs
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Database Error");
			alert.setContentText("Error loading users: " + e.getMessage());
			alert.showAndWait();
		}
		finally {
			databaseHelper.closeConnection();
		}
	}
}
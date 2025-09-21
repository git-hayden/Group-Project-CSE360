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
import java.util.Optional;
import javafx.scene.control.TextInputDialog;



/**
 * AdminPage class represents the user interface for the admin user.
 * This page displays a simple welcome message for the admin.
 */

public class AdminHomePage {
	/**
     * Displays the admin page in the provided primary stage.
     * @param primaryStage The primary stage where the scene will be displayed.
     */
	//fields
	private DatabaseHelper databaseHelper;
	private User currentUser;

	// Accept dependencies
	 public AdminHomePage(DatabaseHelper databaseHelper, User user) {
		this.databaseHelper = databaseHelper;
		this.currentUser = user;
	}

	//existing no args constructor to ensure compatibility.
	public AdminHomePage() {
		this.databaseHelper = null;
		this.currentUser = null;
	}

	
    public void show(Stage primaryStage) {
    	VBox layout = new VBox(10);
    	layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
	    
	    // label to display the welcome message for the admin
	    Label adminLabel = new Label("Hello, Admin!");
	    adminLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

		//create a return button
		Button returnButton = new Button("Back");
		returnButton.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white;");
		returnButton.setOnAction(e -> returnToWelcomePage(primaryStage));


		//create table
		TableView<User> userTable = createUserTable();

		//add users to table
		loadUsers(userTable);

		//only return if dependencies are present
		if(databaseHelper != null && currentUser != null) {
			layout.getChildren().addAll(adminLabel, userTable,returnButton);
		}else {layout.getChildren().addAll(adminLabel, userTable);}
		primaryStage.setScene(new Scene(layout, 800, 400)); //increase height for table

	    Scene adminScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(adminScene);
	    primaryStage.setTitle("Admin Page");
    }
	
	private TableView<User> createUserTable() {
		TableView<User> table = new TableView<>();
		table.setPrefWidth(800);
		table.setPrefHeight(300);

		//username column
		TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
		usernameColumn.setCellValueFactory(data -> {
			String username = data.getValue().getUserName();
			return new javafx.beans.property.SimpleStringProperty(username);
		});
		
		usernameColumn.setPrefWidth(250);

		//role column
		TableColumn<User, String> roleColumn = new TableColumn<>("Role");
		roleColumn.setCellValueFactory(data -> {
			String role = data.getValue().getRole();
			return new javafx.beans.property.SimpleStringProperty(role);
		});
		roleColumn.setPrefWidth(300);



	//actions column for delete button and admin actions
	TableColumn<User, Void> actionsColumn = new TableColumn<>("Actions");
	actionsColumn.setCellFactory(col -> new TableCell<User, Void>() {
		private Button deleteButton = new Button("Delete");
		{
			deleteButton.setOnAction(event -> {
				User user = getTableView().getItems().get(getIndex());
				deleteUser(user, table);
			});
		}
		@Override
		protected void updateItem(Void item, boolean empty) {
			super.updateItem(item, empty);
			if (empty ){
				setGraphic(null);
			} else {
				User user = getTableView().getItems().get(getIndex());
				if("admin".equalsIgnoreCase(user.getRole())) {
					//Disable Delete button for admin users and change appearance of button.
					deleteButton.setText("Disabled");
					deleteButton.setDisable(true);
					deleteButton.setStyle("-fx-background-color: #cccccc; -fx-text-fill: #666666;");
					deleteButton.setTooltip(new Tooltip("Admin users cannot be removed."));
				} else {
					//If user is not admin show delete button
					deleteButton.setText("Delete");
					deleteButton.setDisable(false);
					deleteButton.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white;");
					deleteButton.setTooltip(new Tooltip("Click to remove user."));
				}
				setGraphic(deleteButton);
			}
			}
		});
		//set width of actions column
				actionsColumn.setPrefWidth(100);
				//add columns to table
				table.getColumns().addAll(usernameColumn, roleColumn, actionsColumn);
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
	//Handle user deletion prevent admin from being deleted and ask for confirmation
	private void deleteUser(User user, TableView<User> table) {
		if("admin".equalsIgnoreCase(user.getRole())) {
			Alert adminProtectionAlert = new Alert(Alert.AlertType.WARNING); //admin protection and alert
			adminProtectionAlert.setTitle("Not allowed.");
			adminProtectionAlert.setHeaderText("Admin cannot be deleted.");
			adminProtectionAlert.setContentText("Only non-admin users can be deleted.");
			adminProtectionAlert.showAndWait();
			return;
		}
		//Custom input field for confirmation
		TextInputDialog confirmationField = new TextInputDialog();
		confirmationField.setTitle("Confirm Deletion");
		confirmationField.setHeaderText("Delete User:" + user.getUserName());
		confirmationField.setContentText("This action cannot be undone, are you sure you want to delete this user? \n\n " + "Type 'Yes' to confirm.");
		confirmationField.getEditor().setPromptText("Type 'Yes' to confirm");

		Optional<String> result = confirmationField.showAndWait();

		result.ifPresent(input -> {
			if("Yes".equals(input)) { //requires capitalization, error if not
				performUserDeletion(user, table);
			} else if(input.equalsIgnoreCase("yes")) {
				Alert caseSensitiveAlert = new Alert(Alert.AlertType.ERROR);
				caseSensitiveAlert.setTitle("Confirmation Failed");
				caseSensitiveAlert.setHeaderText("Case Sensitive Error");
				caseSensitiveAlert.setContentText("Please type 'Yes' to confirm. Confirmation is case sensitive.");
				caseSensitiveAlert.showAndWait();}
				else{ //invalid input alert for any non-yes input
					Alert invalidInputAlert = new Alert(Alert.AlertType.ERROR);
					invalidInputAlert.setTitle("Invalid Input");
					invalidInputAlert.setHeaderText("Confirmation input not valid.");
					invalidInputAlert.setContentText("Please type 'Yes' to confirm. Confirmation is case sensitive.");
					invalidInputAlert.showAndWait();
				}
		});
	}
	//method to handle the actual user deletion
	private void performUserDeletion(User user, TableView<User> table) {
		DatabaseHelper databaseHelper = new DatabaseHelper();
		try {
			databaseHelper.connectToDatabase() ;
			boolean deleted = databaseHelper.deleteUser(user.getUserName());

			if(deleted) {
				//remove user from table
				table.getItems().remove(user);

				//success alert
				Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
				successAlert.setTitle("User Deleted");
				successAlert.setHeaderText("User Deleted Successfully");
				successAlert.setContentText("The user " + user.getUserName() + " has been deleted successfully.");
				successAlert.showAndWait();
			} else {
				//deletion failed error
				Alert errorAlert = new Alert(Alert.AlertType.ERROR);
				errorAlert.setTitle("Deletion Failed");
				errorAlert.setHeaderText("Database Error");
				errorAlert.setContentText("Failed to delete user " + user.getUserName() + " from the database.");
				errorAlert.showAndWait();
			}
		} catch (SQLException e) { //Db error alert
			Alert dbErrorAlert = new Alert(Alert.AlertType.ERROR);
			dbErrorAlert.setTitle("Database Error");
			dbErrorAlert.setHeaderText("Database connection Error");
			dbErrorAlert.setContentText("Failed to connect to the database: " + e.getMessage());
			dbErrorAlert.showAndWait();
		} finally {
			databaseHelper.closeConnection();
		}
	}
	//method to return to welcome page
	private void returnToWelcomePage(Stage primaryStage) {
		if(databaseHelper != null && currentUser != null) {
			WelcomeLoginPage welcomePage = new WelcomeLoginPage(databaseHelper);
			welcomePage.show(primaryStage, currentUser);
		}else{
			//Show error and navigate to login.
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Navigation error");
			alert.setHeaderText("Cannot return to welcome page");
			alert.setContentText("Please login again.");
			alert.showAndWait();
			new SetupLoginSelectionPage(new DatabaseHelper()).show(primaryStage);
		}
	}
}

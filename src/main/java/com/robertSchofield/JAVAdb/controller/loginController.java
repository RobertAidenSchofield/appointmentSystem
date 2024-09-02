package com.robertSchofield.JAVAdb.controller;

import com.robertSchofield.JAVAdb.DAOs.appointmentDAO;
import com.robertSchofield.JAVAdb.DAOs.userDAO;
import com.robertSchofield.JAVAdb.helper.JDBC;
import com.robertSchofield.JAVAdb.helper.fxmlHelper;
import com.robertSchofield.JAVAdb.model.appointments;
import com.robertSchofield.JAVAdb.model.user;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

import static com.robertSchofield.JAVAdb.helper.alertHandler.errorType;

/**
 * This class handles the login functionality of the application.
 * It includes methods for user authentication, language selection, and logging login attempts.
 *
 * @Author Robert Schofield
 */
public class loginController {

  Locale locale = Locale.getDefault();

  ResourceBundle langBundle = ResourceBundle.getBundle("lang", locale);
  @FXML
  private Button logInBtn;

  @FXML
  private Button cancelBtn;

  @FXML
  private ToggleButton languageTog;

  @FXML
  private Label label;

  @FXML
  private TextField userNameField;

  @FXML
  private TextField passwordField;

  /**
   * Initializes the login controller.
   * Sets up the UI elements and prompts based on the current language.
   */
  @FXML
  public void initialize() {
    try {
      if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault()
          .getLanguage()
          .equals("en")) {
        userNameField.setPromptText(langBundle.getString("Username"));
        passwordField.setPromptText(langBundle.getString("Password"));
        cancelBtn.setText(langBundle.getString("Cancel"));
        logInBtn.setText(langBundle.getString("Login"));
        label.setText(String.valueOf(ZoneId.systemDefault()));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Records a login attempt in the login_activity.txt file.
   *
   * @param username   The username of the user attempting to log in.
   * @param successful Indicates whether the login attempt was successful.
   */
  private void recordLoginAttempt(String username, boolean successful) {
    try (FileWriter fileWriter = new FileWriter("login_activity.txt", true)) {
      LocalDateTime dateTime = LocalDateTime.now();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      String timestamp = dateTime.format(formatter);

      fileWriter.write("Username: " + username + "\n");
      fileWriter.write("Timestamp: " + timestamp + "\n");
      fileWriter.write("Successful: " + (successful ? "Yes" : "No") + "\n");
      fileWriter.write("---------------------------------\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Handles the login action.
   * Validates the user's credentials, checks for upcoming appointments, and performs necessary actions.
   *
   * @param event The action event triggered by the login button.
   * @throws Exception If an error occurs during the login process.
   */
  @FXML
  public void exLogIn(ActionEvent event) throws Exception {
    JDBC.openConnection();
    try {
      String username = userNameField.getText();
      String password = passwordField.getText();
      user user = new user(username, password);
      // Check for upcoming appointments within 15 minutes
      ObservableList<appointments> upcomingAppointments =
          appointmentDAO.getUpcomingAppointments(15);

      if (upcomingAppointments.isEmpty()
          && com.robertSchofield.JAVAdb.DAOs.userDAO.find(user) != null) {
        Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        fxmlHelper.loadFXMLScene("/com/robertSchofield/JAVAdb/mainMenu.fxml", "Main Menu",
            currentStage);
        // Record log-in attempt in login_activity.txt
        recordLoginAttempt(username, true);
        errorType(10);
      } else if (com.robertSchofield.JAVAdb.DAOs.userDAO.find(user) == null) {
        // Record failed log-in attempt in login_activity.txt
        recordLoginAttempt(username, false);
        errorType(6);
      } else if (!upcomingAppointments.isEmpty()
          && com.robertSchofield.JAVAdb.DAOs.userDAO.find(user) != null) {
        Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        fxmlHelper.loadFXMLScene("/com/robertSchofield/JAVAdb/mainMenu.fxml", "Main Menu",
            currentStage);
        // Record log-in attempt in login_activity.txt
        recordLoginAttempt(username, true);
        // Display an alert with appointment details
        errorType(1);
      } else if (username.isEmpty()) {
        errorType(8);
      } else if (password.isEmpty()) {
        errorType(9);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Handles the cancel action.
   * Displays a confirmation message and closes the login window.
   */
  @FXML
  public void exCancel() {
    errorType(12);
    Stage currentStage = (Stage) cancelBtn.getScene().getWindow();
    currentStage.close();
  }
}
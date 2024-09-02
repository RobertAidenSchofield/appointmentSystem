package com.robertSchofield.JAVAdb.controller;

import com.robertSchofield.JAVAdb.helper.fxmlHelper;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 * The type Main menu controller.
 *
 * @Author Robert Schofield
 */
public class mainMenuController {
  /**
   * This function handles the action event for the "Appointments" menu item.
   * It loads the "Appointments.fxml" scene into the current stage.
   *
   * @param event The action event that triggered this function.
   * @throws Exception If an error occurs while loading the FXML scene.
   */
  @FXML void menuAppoint(ActionEvent event) throws Exception {
    Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    fxmlHelper.loadFXMLScene("/com/robertSchofield/JAVAdb/Appointments.fxml", "Appointment",
        currentStage);
  }

  /**
   * This function handles the action event for the "Customers" menu item.
   * It loads the "Customers.fxml" scene into the current stage.
   *
   * @param event The action event that triggered this function.
   * @throws Exception If an error occurs while loading the FXML scene.
   */
  @FXML void menuCustomer(ActionEvent event) throws Exception {
    Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    fxmlHelper.loadFXMLScene("/com/robertSchofield/JAVAdb/Customers.fxml", "Customer",
        currentStage);
  }

  /**
   * This function handles the action event for the "Exit" menu item.
   * It displays an alert asking the user if they are sure they want to exit,
   * and then exits the application if the user confirms.
   *
   * @param event The action event that triggered this function.
   */
  @FXML void menuExit(ActionEvent event) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setContentText("Are you sure you want to exit?");
    Optional<ButtonType> result = alert.showAndWait();

    if (result.isPresent() && result.get() == ButtonType.OK) {
      System.exit(0);
    }
  }

  /**
   * This function handles the action event for the "Report" menu item.
   * It loads the "report.fxml" scene into the current stage.
   *
   * @param event The action event that triggered this function.
   * @throws Exception If an error occurs while loading the FXML scene.
   */
  @FXML void menuReport(ActionEvent event) throws Exception {
    Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    fxmlHelper.loadFXMLScene("/com/robertSchofield/JAVAdb/report.fxml", "Report", currentStage);
  }
}

package com.robertSchofield.JAVAdb.controller;

import com.robertSchofield.JAVAdb.DAOs.appointmentDAO;
import com.robertSchofield.JAVAdb.DAOs.contactDAO;
import com.robertSchofield.JAVAdb.DAOs.customerDAO;
import com.robertSchofield.JAVAdb.DAOs.userDAO;
import com.robertSchofield.JAVAdb.helper.JDBC;
import com.robertSchofield.JAVAdb.helper.fxmlHelper;
import com.robertSchofield.JAVAdb.helper.timeHelper;
import com.robertSchofield.JAVAdb.model.appointments;
import com.robertSchofield.JAVAdb.model.contact;
import com.robertSchofield.JAVAdb.model.customers;
import com.robertSchofield.JAVAdb.model.user;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import static com.robertSchofield.JAVAdb.controller.appointmentController.selectedAppointment;

/**
 * This class handles the modification of appointments in the application.
 * It includes methods for validating appointment details, saving changes,
 * and canceling the modification process.
 *
 * @Author Robert Schofield
 */
public class modAppointmentController implements Initializable {
  private final appointmentDAO appointmentDAO = new appointmentDAO();
  private final customerDAO customerDAO = new customerDAO();
  private final contactDAO contactDAO = new contactDAO();
  private final userDAO userDAO = new userDAO();
  @FXML private TextField appointmentDescriptionTextField;

  @FXML private TextField appointmentIDTextField;

  @FXML private TextField appointmentLocationTextField;

  @FXML private TextField appointmentTitleTextField;

  @FXML private TextField appointmentTypeTextField;

  @FXML private ComboBox<LocalTime> endTimeCombo;

  @FXML private ComboBox<LocalTime> startTimeCombo;

  @FXML private Button cancelButton;

  @FXML private ComboBox<contact> contactCombo;

  @FXML private ComboBox<customers> customerCombo;

  @FXML private DatePicker endDatePicker;

  @FXML private Button saveButton;

  @FXML private DatePicker startDatePicker;

  @FXML private ComboBox<user> userCombo;

  /**
   * This method handles the action when the user clicks the "Cancel" button.
   * It displays an alert message to inform the user about canceling the appointment,
   * and then closes the modification scene.
   *
   * @param event The event triggering the action (button click).
   * @throws Exception If an error occurs while loading the scene.
   */
  @FXML void actionCancelButton(ActionEvent event) throws Exception {
    // Displays alter message box to inform the user
    //  about canceling the appointment
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Appointment Creation");
    alert.setHeaderText(null);
    alert.setContentText("Appointment creation cancelled.");
    alert.showAndWait();
    // Closes the add appointment scene
    Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    fxmlHelper.loadFXMLScene("/com/robertSchofield/JAVAdb/Appointments.fxml", "Appointment",
        currentStage);
  }

  /**
   * This method handles the action when the user clicks the "Save" button.
   * It validates the appointment details, checks for overlapping appointments,
   * and saves the changes to the database. It also displays appropriate alert messages
   * based on the validation results.
   *
   * @param event The event triggering the action (button click).
   * @throws Exception If an error occurs while saving the appointment or displaying alert messages.
   */
  @FXML void actionSaveButton(ActionEvent event) throws Exception {
    JDBC.openConnection();
    int appointmentID = selectedAppointment.getAppointmentID();
    int customerID = customerCombo.getValue().getCustomerID();

    // Validate inputs
    if (appointmentTitleTextField.getText().isEmpty()
        || appointmentDescriptionTextField.getText()
        .isEmpty()
        || appointmentLocationTextField.getText().isEmpty()
        || appointmentTypeTextField.getText().isEmpty()
        || startTimeCombo.getValue() == null
        || endTimeCombo.getValue() == null
        || customerCombo.getValue() == null
        || userCombo.getValue() == null
        || contactCombo.getValue() == null) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Appointment Modification");
      alert.setHeaderText(null);
      alert.setContentText("Please fill in all required fields.");
      alert.showAndWait();
      return;
    }
    if (startTimeCombo.getValue().isAfter(endTimeCombo.getValue())) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Appointment Modification");
      alert.setHeaderText(null);
      alert.setContentText("The start time must be before the end time.");
      alert.showAndWait();
      return;
    }

    LocalDateTime startNow =
        LocalDateTime.of(startDatePicker.getValue(), startTimeCombo.getValue());
    LocalDateTime endNow = LocalDateTime.of(endDatePicker.getValue(), endTimeCombo.getValue());
    ZonedDateTime convertStart = timeHelper.toEasternTime(
        LocalDateTime.of(startDatePicker.getValue(), startTimeCombo.getValue()));
    ZonedDateTime convertEnd = timeHelper.toEasternTime(
        LocalDateTime.of(endDatePicker.getValue(), endTimeCombo.getValue()));
    if (convertStart.toLocalTime().isAfter(LocalTime.of(22, 0))
        || convertEnd.toLocalTime()
        .isAfter(LocalTime.of(22, 0))
        || convertStart.toLocalTime().isBefore(LocalTime.of(8, 0))
        || convertEnd.toLocalTime().isBefore(LocalTime.of(8, 0))) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Appointment Modification");
      alert.setHeaderText(null);
      alert.setContentText(
          "The selected appointment time is outside the business hours (8 AM - 10 PM Eastern Time).");
      alert.showAndWait();
      return;
    }

    if (timeHelper.overlapCheck(appointmentID, customerID, startNow, endNow)) {
      appointments appointment =
          new appointments(appointmentID, appointmentTitleTextField.getText(),
              appointmentDescriptionTextField.getText(), appointmentLocationTextField.getText(),
              appointmentTypeTextField.getText(),
              LocalDateTime.of(startDatePicker.getValue(), startTimeCombo.getValue()),
              LocalDateTime.of(endDatePicker.getValue(), endTimeCombo.getValue()),
              customerCombo.getValue().getCustomerID(), userCombo.getValue().getUserID(),
              contactCombo.getValue().getContactId());

      appointmentDAO.update(appointment);

      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Appointment Modification");
      alert.setHeaderText(null);
      alert.setContentText("Appointment modification successful.");
      alert.showAndWait();
      Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
      fxmlHelper.loadFXMLScene("/com/robertSchofield/JAVAdb/Appointments.fxml", "Appointment",
          currentStage);
    } else {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Appointment Modification");
      alert.setHeaderText(null);
      alert.setContentText("The selected appointment time overlaps with another appointment.");
      alert.showAndWait();
    }
  }

  /**
   * This method sets the date and time pickers and combo boxes with the selected appointment's details.
   * It is called when the modification scene is initialized.
   *
   * @throws SQLException If an error occurs while retrieving appointment details from the database.
   */
  public void fillBoxes() throws SQLException {
    // Set the date and time pickers and combo boxes with the selected appointment's details
    startDatePicker.setValue(selectedAppointment.getStart().toLocalDate());
    endDatePicker.setValue(selectedAppointment.getEnd().toLocalDate());
    startTimeCombo.getSelectionModel().select(selectedAppointment.getStart().toLocalTime());
    endTimeCombo.getSelectionModel().select(selectedAppointment.getEnd().toLocalTime());
    customerCombo.setValue(customerDAO.findById(selectedAppointment.getCustomerID()));
    userCombo.setValue(userDAO.findById(selectedAppointment.getUserID()));
    contactCombo.setValue(
        com.robertSchofield.JAVAdb.DAOs.contactDAO.findById(selectedAppointment.getContactID()));
    System.out.println(
        "Loaded appointment details: " + (com.robertSchofield.JAVAdb.DAOs.contactDAO.findById(
            selectedAppointment.getContactID())));
  }

  /**
   * This method initializes the components of the modification scene.
   * It sets up the combo box converters, sets the appointment details in the text fields,
   * and populates the date and time pickers with the selected appointment's start and end times.
   *
   * @param url            The location used to resolve relative paths for resources.
   * @param resourceBundle The resource bundle containing localizable strings.
   */
  @Override public void initialize(URL url, ResourceBundle resourceBundle) {

    customerCombo.setConverter(new StringConverter<>() {
      @Override public String toString(customers customer) {
        return customer != null ? customer.getCustomerName() : "";
      }

      @Override public customers fromString(String s) {
        return null;
      }
    });

    userCombo.setConverter(new StringConverter<>() {
      @Override public String toString(user user) {
        return user != null ? user.getUserName() : "";
      }

      @Override public user fromString(String s) {
        return null;
      }
    });

    contactCombo.setConverter(new StringConverter<>() {
      @Override public String toString(contact contact) {
        return contact != null ? contact.getContactName() : "";
      }

      @Override public contact fromString(String s) {
        return null;
      }
    });
    appointmentIDTextField.setText(String.valueOf(selectedAppointment.getAppointmentID()));
    appointmentTitleTextField.setText(selectedAppointment.getTitle());
    appointmentDescriptionTextField.setText(selectedAppointment.getDescription());
    appointmentLocationTextField.setText(selectedAppointment.getLocation());
    appointmentTypeTextField.setText(selectedAppointment.getType());

    ObservableList<LocalTime> times = FXCollections.observableArrayList();
    for (int hour = 7; hour < 23; hour++) {
      for (int minute = 0; minute < 60; minute += 15) { // Assuming 15-minute intervals
        times.add(LocalTime.of(hour, minute));
      }
    }
    try {
      fillBoxes();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    startTimeCombo.setItems(times);
    endTimeCombo.setItems(times);
    userCombo.setItems(com.robertSchofield.JAVAdb.DAOs.userDAO.listAll());
    contactCombo.setItems(com.robertSchofield.JAVAdb.DAOs.contactDAO.listAll());
    customerCombo.setItems(com.robertSchofield.JAVAdb.DAOs.customerDAO.listAll());
    customerCombo.setValue(customerDAO.findById(selectedAppointment.getCustomerID()));

    userCombo.setValue(userDAO.findById(selectedAppointment.getUserID()));
  }
}

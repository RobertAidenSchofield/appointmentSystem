package com.robertSchofield.JAVAdb.controller;

import com.robertSchofield.JAVAdb.DAOs.appointmentDAO;
import com.robertSchofield.JAVAdb.helper.JDBC;
import com.robertSchofield.JAVAdb.helper.fxmlHelper;
import com.robertSchofield.JAVAdb.helper.timeHelper;
import com.robertSchofield.JAVAdb.model.appointments;
import com.robertSchofield.JAVAdb.model.contact;
import com.robertSchofield.JAVAdb.model.customers;
import com.robertSchofield.JAVAdb.model.user;
import java.net.URL;
import java.time.LocalDate;
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

/**
 * This class handles the functionality of the add appointment scene in the JavaFX application.
 * It includes methods for validating appointment details, checking for overlapping appointments,
 * and saving new appointments to the database.
 *
 * @Author Robert Schofield
 */
public class addAppointmentController implements Initializable {
  private final appointmentDAO appointmentDAO = new appointmentDAO();
  /**
   * The Alert.
   */
  Alert alert = new Alert(Alert.AlertType.INFORMATION);

  @FXML
  private TextField appointDescriptionTextField;

  @FXML
  private TextField appointLocationTextField;

  @FXML
  private TextField appointTitleTextField;

  @FXML
  private TextField appointTypeTextField;

  @FXML
  private TextField appointmentIDTextField;

  @FXML
  private Button cancelButton;

  @FXML
  private ComboBox<contact> contactComboAdd;

  @FXML
  private ComboBox<customers> customerComboAdd;

  @FXML
  private DatePicker endDatePickerAdd;

  @FXML
  private ComboBox<LocalTime> endTimeComboAdd;

  @FXML
  private Button saveButton;

  @FXML
  private DatePicker startDatePickerAdd;

  @FXML
  private ComboBox<LocalTime> startTimeComboAdd;

  @FXML
  private ComboBox<user> userComboAdd;

  /**
   * This method handles the action when the cancel button is clicked.
   * It displays an alert message to inform the user about canceling the appointment,
   * and then closes the add appointment scene.
   *
   * @param event The event that triggered this method (the cancel button click).
   * @throws Exception If an error occurs while closing the scene.
   */
  @FXML
  void actionCancelButton(ActionEvent event) throws Exception {
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
   * This method handles the action when the save button is clicked.
   * It validates the input fields, checks for overlapping appointments,
   * and saves new appointments to the database.
   *
   * @param event The event that triggered this method (the save button click).
   * @throws Exception If an error occurs while saving the appointment.
   */
  @FXML
  void actionSaveButton(ActionEvent event) throws Exception {
    JDBC.openConnection();

    // Validate the input fields
    if (appointTitleTextField.getText().isEmpty() || appointDescriptionTextField.getText().isEmpty()
        || appointLocationTextField.getText().isEmpty() || appointTypeTextField.getText().isEmpty()
        || startDatePickerAdd.getValue() == null || startTimeComboAdd.getValue() == null
        || endDatePickerAdd.getValue() == null || endTimeComboAdd.getValue() == null
        || customerComboAdd.getValue() == null || userComboAdd.getValue() == null
        || contactComboAdd.getValue() == null) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Invalid Input");
      alert.setHeaderText(null);
      alert.setContentText("Please fill in all required fields and ensure valid inputs.");
      alert.showAndWait();
      return;
    }

    // Check if the start date and time are before the end date and time
    LocalDateTime startDateTime =
        LocalDateTime.of(startDatePickerAdd.getValue(), startTimeComboAdd.getValue());
    LocalDateTime endDateTime = LocalDateTime.of(endDatePickerAdd.getValue(),
        endTimeComboAdd.getValue());
    ZonedDateTime convertStart = timeHelper.toEasternTime(
        LocalDateTime.of(startDatePickerAdd.getValue(), startTimeComboAdd.getValue()));
    ZonedDateTime convertEnd = timeHelper.toEasternTime(
        LocalDateTime.of(endDatePickerAdd.getValue(), endTimeComboAdd.getValue()));
    if (startDateTime.isAfter(endDateTime)) {
      alert.setTitle("Invalid Date/Time");
      alert.setHeaderText(null);
      alert.setContentText("Start date/time cannot be after end date/time.");
      alert.showAndWait();
      return;
    }
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
    int id = 0;
    if (timeHelper.overlapCheck(id, customerComboAdd.getValue().getCustomerID(),
        LocalDateTime.of(startDatePickerAdd.getValue(), startTimeComboAdd.getValue()),
        LocalDateTime.of(endDatePickerAdd.getValue(), endTimeComboAdd.getValue()))) {
      // Create a new Appointment object with the input values
      appointments newAppointment = new appointments(
          appointTitleTextField.getText(),
          appointDescriptionTextField.getText(),
          appointLocationTextField.getText(),
          appointTypeTextField.getText(),
          LocalDateTime.of(startDatePickerAdd.getValue(), startTimeComboAdd.getValue()),
          LocalDateTime.of(endDatePickerAdd.getValue(), endTimeComboAdd.getValue()),
          customerComboAdd.getValue().getCustomerID(),
          userComboAdd.getValue().getUserID(),
          contactComboAdd.getValue().getContactId());

      appointmentDAO.add(newAppointment);

      alert.setTitle("Appointment Creation");
      alert.setHeaderText(null);
      alert.setContentText("Appointment created successfully.");
      alert.showAndWait();

      // Closes the add appointment scene
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

  @Override public void initialize(URL location, ResourceBundle resources) {

    appointmentIDTextField.setId(appointmentIDTextField.getId());
    // Load customers, users, and contacts into the combo boxes
    ObservableList<customers> customers = com.robertSchofield.JAVAdb.DAOs.customerDAO.listAll();
    customerComboAdd.setItems(customers);

    ObservableList<user> users = com.robertSchofield.JAVAdb.DAOs.userDAO.listAll();
    userComboAdd.setItems(users);

    ObservableList<contact> contacts = com.robertSchofield.JAVAdb.DAOs.contactDAO.listAll();
    contactComboAdd.setItems(contacts);

    // Set the default values for the date and time pickers and combo boxes
    startDatePickerAdd.setValue(LocalDate.now());
    endDatePickerAdd.setValue(LocalDate.now());

    // Populate  startTimeComboAdd and endTimeComboAdd with the available times
    ObservableList<LocalTime> times = FXCollections.observableArrayList();
    for (int hour = 7; hour < 23; hour++) {
      for (int minute = 0; minute < 60; minute += 15) { // Assuming 15-minute intervals
        times.add(LocalTime.of(hour, minute));
      }
    }
    startTimeComboAdd.setItems(times);
    endTimeComboAdd.setItems(times);
  }
}

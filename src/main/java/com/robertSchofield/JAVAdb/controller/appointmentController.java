package com.robertSchofield.JAVAdb.controller;

import com.robertSchofield.JAVAdb.DAOs.appointmentDAO;
import com.robertSchofield.JAVAdb.DAOs.customerDAO;
import com.robertSchofield.JAVAdb.helper.JDBC;
import com.robertSchofield.JAVAdb.helper.fxmlHelper;
import com.robertSchofield.JAVAdb.model.appointments;
import com.robertSchofield.JAVAdb.model.contact;
import com.robertSchofield.JAVAdb.model.customers;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * This class handles the appointment-related functionality in the application.
 * It provides methods to build data, add, delete, update, and view appointments.
 *
 * @Author Robert Schofield
 */
public class appointmentController implements Initializable {
  /**
   * The constant selectedAppointment.
   */
  public static appointments selectedAppointment;
  private final appointmentDAO appointmentDAO = new appointmentDAO();
  private final customerDAO customerDAO = new customerDAO();

  /**
   * The Appointment view toggle group.
   */
  @FXML ToggleGroup appointmentViewToggleGroup = new ToggleGroup();
  @FXML private Button appointAddLabel;
  @FXML private Button appointDeleteLabel;
  @FXML private TableView<appointments> tableview;
  @FXML private Button appointUpdateLabel;
  @FXML private RadioButton weeklyAppointmentsRadioButton;
  @FXML private RadioButton monthlyAppointmentsRadioButton;
  @FXML private RadioButton allAppointmentsRadioButton;

  /**
   * Builds data for the appointment table view based on the specified method name.
   *
   * @param methodName The name of the method to call from appointmentDAO.
   * @throws SQLException If an error occurs while connecting to the database.
   */
  public void buildData(String methodName) throws SQLException {
    try {
      JDBC.openConnection(); // Use your JDBC connection method

      // Call the specified method from appointmentDAO
      ObservableList<appointments> appointmentsList;
      switch (methodName) {
        case "getAppointmentsByMonth":
          appointmentsList = appointmentDAO.getAppointmentsByMonth();
          break;
        case "getAppointmentsByWeek":
          appointmentsList = appointmentDAO.getAppointmentsByWeek();
          break;
        case "getAllAppointmentTypes":
          appointmentsList = appointmentDAO.listAll();
          break;
        default:
          System.out.println("Invalid method name");
          return;
      }

      // Check if the list is not loginController
      if (appointmentsList != null && !appointmentsList.isEmpty()) {
        // Clear existing columns
        tableview.getColumns().clear();
        // Get the list of field names from the customers class
        List<String> fieldNames = Arrays.stream(appointments.class.getDeclaredFields())
            .map(Field::getName)
            .collect(Collectors.toList());

        // Filter out the unwanted field names
        List<String> wantedFieldNames = fieldNames.stream()
            .filter(fieldName -> !fieldName.equals("divName")
                && !fieldName.equals("userID")
                && !fieldName.equals("total"))
            .collect(Collectors.toList());

        // Map field names to their corresponding column names
        Map<String, String> columnNamesMap = new HashMap<>();
        columnNamesMap.put("customerID", "Customer");
        columnNamesMap.put("contactID", "Contact");
        columnNamesMap.put("appointmentID", "Appointment ID");
        columnNamesMap.put("title", "Title");
        columnNamesMap.put("description", "Description");
        columnNamesMap.put("location", "Location");
        columnNamesMap.put("type", "Type");
        columnNamesMap.put("url", "URL");
        columnNamesMap.put("start", "Start Date | Start Time");
        columnNamesMap.put("end", "End Date | End Time");

        // Add table columns dynamically
        for (String fieldName : wantedFieldNames) {
          TableColumn<appointments, String> col =
              new TableColumn<>(columnNamesMap.getOrDefault(fieldName, fieldName));
          col.setCellValueFactory(param -> {
            try {
              Method getterMethod = appointments.class.getMethod(
                  "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));

              Object value = getterMethod.invoke(param.getValue());

              if (value instanceof LocalDateTime) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                    .withZone(ZoneId.systemDefault()); // Use the user's local time zone
                return new SimpleStringProperty(formatter.format((LocalDateTime) value));
              } else if (fieldName.equals("customerID")) {
                customers customer = customerDAO.findById((Integer) value);
                return new SimpleStringProperty(customer.getCustomerName());
              } else if (fieldName.equals("contactID")) {
                contact contact =
                    com.robertSchofield.JAVAdb.DAOs.contactDAO.findById((Integer) value);
                return new SimpleStringProperty(contact.getContactName());
              } else {
                return new SimpleStringProperty(String.valueOf(value));
              }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
              throw new RuntimeException(e);
            }
          });
          tableview.getColumns().add(col);
        }

        // Add data to TableView
        tableview.setItems(appointmentsList);
        System.out.println("Data successfully loaded into TableView");
      } else {
        System.out.println("No appointments found for the specified period");
      }
    } catch (Exception e) {
      System.out.println("Error on Building Data");
    } finally {
      JDBC.closeConnection(); // Close the connection when done
    }
  }

  /**
   * Handles the action when the "Add Appointment" button is clicked.
   * It loads the "addAppointment.fxml" scene in a new stage.
   *
   * @param event The event that triggered this method.
   */
  @FXML void actionAppointAdd(ActionEvent event) {
    try {
      Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
      fxmlHelper.loadFXMLScene("/com/robertSchofield/JAVAdb/addAppointment.fxml", "Add Appointment",
          currentStage);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Handles the action when the "Delete Appointment" button is clicked.
   * It prompts the user to confirm the deletion and then deletes the selected appointment.
   *
   * @param event The event that triggered this method.
   */
  @FXML void actionAppointDelete(ActionEvent event) {
    appointments selectedAppointment = tableview.getSelectionModel().getSelectedItem();
    if (selectedAppointment != null) {
      try {
        Alert alert =
            new Alert(Alert.AlertType.WARNING, "Are you sure you want to delete this appointment?");
        alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
          com.robertSchofield.JAVAdb.DAOs.appointmentDAO.delete(selectedAppointment);
          alert = new Alert(Alert.AlertType.INFORMATION);
          alert.setTitle("Appointment Deleted");
          alert.setHeaderText("Appointment Deleted Successfully.");
          alert.setContentText("Appointment ID: "
              + selectedAppointment.getAppointmentID()
              + "\nType: "
              + selectedAppointment.getType()
              + "\nDeleted successfully.");
          alert.showAndWait();
          buildData("getAllAppointmentTypes"); // Refresh the table view
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("No Selection");
      alert.setHeaderText("No appointment selected.");
      alert.setContentText("Please select an appointment from the table.");
      alert.showAndWait();
    }
  }

  /**
   * Handles the action when the "Update Appointment" button is clicked.
   * It loads the "modAppointment.fxml" scene in a new stage.
   *
   * @param event The event that triggered this method.
   */
  @FXML void actionAppointUpdate(ActionEvent event) {
    selectedAppointment = tableview.getSelectionModel().getSelectedItem();
    if (selectedAppointment != null) {
      try {
        Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        fxmlHelper.loadFXMLScene("/com/robertSchofield/JAVAdb/modAppointment.fxml",
            "Mod Appointment",
            currentStage);
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("No Selection");
      alert.setHeaderText("No appointment selected.");
      alert.setContentText("Please select an appointment from the table.");
      alert.showAndWait();
    }
  }

  /**
   * Handles the action when the "All Appointments" radio button is selected.
   * It builds data for all appointment types.
   *
   * @param event The event that triggered this method.
   * @throws SQLException If an error occurs while connecting to the database.
   */
  @FXML void allAppointments(ActionEvent event) throws SQLException {
    if (allAppointmentsRadioButton.isSelected()) {
      buildData("getAllAppointmentTypes");
    }
  }

  /**
   * Handles the action when the "Back to Menu" button is clicked.
   * It loads the "mainMenu.fxml" scene in a new stage with the given title.
   *
   * @param event The event that triggered this method.
   */
  @FXML void backToMenu(ActionEvent event) {
    try {
      Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
      fxmlHelper.loadFXMLScene("/com/robertSchofield/JAVAdb/mainMenu.fxml", "Main Menu",
          currentStage);
      // Load the mainMenu scene with the given title
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Handles the action when the "Monthly Appointments" radio button is selected.
   * It builds data for appointments in the current month and refreshes the table view.
   *
   * @param event The event that triggered this method.
   * @throws SQLException If an error occurs while connecting to the database.
   */
  @FXML void monthlyAppointments(ActionEvent event) throws SQLException {
    if (monthlyAppointmentsRadioButton.isSelected()) {
      buildData("getAppointmentsByMonth");
    }
    JDBC.openConnection(); // Reconnect to the database after switching views
    if (appointmentDAO.getAppointmentsByMonth().isEmpty()) {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("No Appointments");
      alert.setHeaderText("No appointments found for this month.");
      alert.setContentText("Please choose a different view option.");
      alert.showAndWait();
    }
  }

  /**
   * Handles the action when the "Weekly Appointments" radio button is selected.
   * It builds data for appointments in the current week and refreshes the table view.
   *
   * @param event The event that triggered this method.
   * @throws SQLException If an error occurs while connecting to the database.
   */
  @FXML void weeklyAppointments(ActionEvent event) throws SQLException {
    if (weeklyAppointmentsRadioButton.isSelected()) {
      buildData("getAppointmentsByWeek");
    }
    JDBC.openConnection(); // Reconnect to the database after switching views
    if (appointmentDAO.getAppointmentsByWeek().isEmpty()) {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("No Appointments");
      alert.setHeaderText("No appointments found for this week.");
      alert.setContentText("Please choose a different view option.");
      alert.showAndWait();
    }
  }

  /**
   * Initializes the appointmentController by building data for all appointment types.
   *
   * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
   * @param rb  The resources used to localize the root object, or null if the root object was not localized.
   */
  @Override public void initialize(URL url, ResourceBundle rb) {
    try {
      buildData("getAllAppointmentTypes");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    weeklyAppointmentsRadioButton.setToggleGroup(appointmentViewToggleGroup);
    monthlyAppointmentsRadioButton.setToggleGroup(appointmentViewToggleGroup);
    allAppointmentsRadioButton.setToggleGroup(appointmentViewToggleGroup);
  }
}


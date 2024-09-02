package com.robertSchofield.JAVAdb.controller;

import com.robertSchofield.JAVAdb.DAOs.appointmentDAO;
import com.robertSchofield.JAVAdb.DAOs.contactDAO;
import com.robertSchofield.JAVAdb.DAOs.customerDAO;
import com.robertSchofield.JAVAdb.helper.fxmlHelper;
import com.robertSchofield.JAVAdb.model.appointments;
import com.robertSchofield.JAVAdb.model.contact;
import com.robertSchofield.JAVAdb.model.customers;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

/**
 * This class handles the report generation functionality for the appointment scheduling system.
 * It provides methods to populate and display various report tables, such as appointment totals by type,
 * appointment totals by month, and customer totals by division.
 *
 * @Author Robert Schofield
 */
public class reportController implements Initializable {
  private final contactDAO contactDAO = new contactDAO();
  private final customerDAO customerDAO = new customerDAO();
  private final appointmentDAO appointmentDAO = new appointmentDAO();

  @FXML private TableColumn<?, ?> appointContact;

  @FXML private TableColumn<?, ?> appointCountry;

  @FXML private Tab appointCountryTab;

  @FXML private TableColumn<?, ?> appointCountryTotal;

  @FXML private TableColumn<?, ?> appointCustId;

  @FXML private TableColumn<?, ?> appointDescription;

  @FXML private TableColumn<?, ?> appointEnd;

  @FXML private TableColumn<?, ?> appointId;

  @FXML private TableColumn<?, ?> appointMonth;

  @FXML private TableView<appointments> appointMonthTable;

  @FXML private TableColumn<?, ?> appointMonthTotal;

  @FXML private TableColumn<?, ?> appointStart;

  @FXML private TableColumn<?, ?> appointTitle;

  @FXML private Tab appointTotalTab;

  @FXML private TableColumn<?, ?> appointTotalType;

  @FXML private TableColumn<?, ?> appointType;

  @FXML private TableView<appointments> appointTypeTable;

  @FXML private TableColumn<?, ?> appointTypeTotal;

  @FXML private TableColumn<?, ?> appointUserId;

  @FXML private Button backToMenu;

  @FXML private ComboBox<contact> contactCombo;

  @FXML private Tab contactScheduleTab;

  @FXML private TableView contactTable;

  @FXML private TableView<customers> countryTable;

  @FXML private TableView<appointments> appointmentTable;

  @FXML private TableColumn<appointments, String> appointIdColumn;

  @FXML private TableColumn<appointments, String> appointTitleColumn;

  @FXML private TableColumn<appointments, String> appointDescriptionColumn;

  @FXML private TableColumn<appointments, LocalDateTime> appointStartColumn;

  @FXML private TableColumn<appointments, LocalDateTime> appointEndColumn;

  /**
   * Handles the action when the backToMenu button is clicked.
   * Displays an information alert and closes the current window,
   * then loads the main menu scene.
   *
   * @param event The action event that triggered this method.
   */
  @FXML void backToMenu(ActionEvent event) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setContentText("Back to Main Menu");
    alert.showAndWait();
    ((Button) event.getSource()).getScene().getWindow().hide();  // Close the current window
    try {
      Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
      fxmlHelper.loadFXMLScene("/com/robertSchofield/JAVAdb/mainMenu.fxml", "Main Menu",
          currentStage);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Populates the contactTable with appointment data for the selected contact.
   * Displays an error alert if no appointments are found for the selected contact.
   *
   * @param event The action event that triggered this method.
   */
  @FXML void contactPopulate(ActionEvent event) {
    try {
      // get contact name from ComboBox
      String selectedContact = String.valueOf(contactCombo.getValue());
      // get contact id via contact name
      int contactId = contactDAO.findByName(selectedContact);
      if (selectedContact != null) {
        // get appointments by contact id
        ObservableList<appointments> filteredAppointments =
            appointmentDAO.listByContactId(contactId);

        if (filteredAppointments.isEmpty()) {
          // Display warning alert if no appointments found for the contact
          Alert alert = new Alert(Alert.AlertType.WARNING);
          alert.setHeaderText("Appointment Warning");
          alert.setContentText("No Appointments found tied with this contact");
          alert.showAndWait();

          // Clear the table if no appointments found
          contactTable.getColumns().clear();
          contactTable.setItems(null);
        } else {
          // Clear existing columns
          contactTable.getColumns().clear();
          // Get the list of field names from the appointments class
          List<String> fieldNames = Arrays.stream(appointments.class.getDeclaredFields())
              .map(Field::getName)  // Fixed: Replace Field::getName with field -> field.getName()
              .toList();
          // Filter out the unwanted field names
          List<String> wantedFieldNames = fieldNames.stream()
              .filter(fieldName -> !fieldName.equals("total")
                  && !fieldName.equals("totalCustomers")
                  && !fieldName.equals("customerID")
                  && !fieldName.equals("countryID")
                  && !fieldName.equals("userID"))
              .toList();
          // Map field names to their corresponding column names
          Map<String, String> columnNamesMap = new HashMap<>();
          columnNamesMap.put("appointmentID", "appointment ID");
          columnNamesMap.put("contactID", "contact ID");
          columnNamesMap.put("start", "start date | start time");
          columnNamesMap.put("end", "end date | end time");

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
                  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                  return new SimpleStringProperty(formatter.format((LocalDateTime) value));
                } else {
                  return new SimpleStringProperty(String.valueOf(value));
                }
              } catch (NoSuchMethodException | IllegalAccessException |
                       InvocationTargetException e) {
                throw new RuntimeException(e);
              }
            });
            contactTable.getColumns().add(col);
          }

          // Add data to TableView
          contactTable.setItems(filteredAppointments);
          System.out.println("Data successfully loaded into contactTable");
        }
      } else {
        // Display warning alert if no contact is selected
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("Contact Warning");
        alert.setContentText("No contact selected");
        alert.showAndWait();

        // Clear the table if no contact is selected
        contactTable.getColumns().clear();
        contactTable.setItems(null);
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Error on fetching contact data");
    }
  }

  /**
   * Initializes the reportController by populating the report tables with data.
   *
   * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
   * @param resources The resources used to localize the root object, or null if the root object was not localized.
   * @lambda initialization to populate the report tables with data.
   */

  @Override public void initialize(URL location, ResourceBundle resources) {
    // Populate appointMonthTable with appointment data and total appointment count for each month
    appointTypeTable.setItems(appointmentDAO.getAllAppointmentTypes());
    // Create and set the columns
    TableColumn<appointments, String> typeColumn = new TableColumn<>("Appointment Type");
    typeColumn.setCellValueFactory(
        cellData -> new SimpleStringProperty(cellData.getValue().getType()));

    TableColumn<appointments, Integer> totalColumn = new TableColumn<>("Total Appointments");
    totalColumn.setCellValueFactory(
        cellData -> new SimpleIntegerProperty(cellData.getValue().getTotal()).asObject());

    appointTypeTable.getColumns().setAll(typeColumn, totalColumn);

    // Populate appointMonthTable with appointment data and total appointment count for each month
    appointMonthTable.setItems(appointmentDAO.getAppointmentTotalByMonth());
    // Set the column properties for the appointMonthTable
    TableColumn<appointments, String> appointMonthColumn = new TableColumn<>("Month");
    appointMonthColumn.setCellValueFactory(
        cellData -> new SimpleStringProperty(cellData.getValue().getType()));

    TableColumn<appointments, String> appointTotalColumn = new TableColumn<>("Total Appointments");
    appointTotalColumn.setCellValueFactory(
        cellData -> new SimpleIntegerProperty(cellData.getValue().getTotal()).asObject()
            .asString());

    appointMonthTable.getColumns().setAll(appointMonthColumn, appointTotalColumn);

    // Populate Customer totals by divison table
    countryTable.setItems(customerDAO.getCustDivTotal());
    // Set the column properties for the customerTotalsByDivision table
    TableColumn<customers, String> customerDivisionColumn = new TableColumn<>("Division");
    customerDivisionColumn.setCellValueFactory(
        cellData -> new SimpleStringProperty(cellData.getValue().getDivName()));

    TableColumn<customers, Integer> customerTotalColumn = new TableColumn<>("Total Customers");
    customerTotalColumn.setCellValueFactory(
        cellData -> new SimpleIntegerProperty(cellData.getValue().getTotalCustomers()).asObject());

    countryTable.getColumns().setAll(customerDivisionColumn, customerTotalColumn);

    ObservableList<contact> contacts =
        com.robertSchofield.JAVAdb.DAOs.contactDAO.listAll();  // Fetch contact data from database
    contactCombo.setItems(contacts);  // Set the contact data to the combo box
  }
}


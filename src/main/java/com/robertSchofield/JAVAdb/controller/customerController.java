package com.robertSchofield.JAVAdb.controller;

import com.robertSchofield.JAVAdb.DAOs.customerDAO;
import com.robertSchofield.JAVAdb.helper.JDBC;
import com.robertSchofield.JAVAdb.helper.fxmlHelper;
import com.robertSchofield.JAVAdb.model.customers;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

/**
 * This class is the controller for the customer management functionality.
 * It handles the actions related to adding, deleting, updating, and viewing customer data.
 * It also populates the customer data in a TableView.
 *
 * @Author Robert Schofield
 */
public class customerController implements Initializable {

  public static customers selectedCustomer;

  public final customerDAO customerDAO = new customerDAO();
  @FXML private Button custAddLabel;

  @FXML private Button custDeleteLabel;

  @FXML private TableView<customers> tableview;

  @FXML private Button custUpdateLabel;

  /**
   * This method is called when the "Add Customer" button is clicked.
   * It navigates to the add customer page.
   *
   * @param event The ActionEvent triggered by the button click.
   * @throws Exception If an error occurs during the navigation.
   */
  @FXML void actionCustAdd(ActionEvent event) throws Exception {
    // Go to the add customer page
    try {
      Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
      fxmlHelper.loadFXMLScene("/com/robertSchofield/JAVAdb/addCustomer.fxml", "Add Customer",
          currentStage);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * This method is called when the "Delete Customer" button is clicked.
   * It confirms the deletion of the selected customer and updates the TableView.
   *
   * @param event The ActionEvent triggered by the button click.
   * @throws Exception If an error occurs during the deletion or updating.
   */
  @FXML void actionCustDelete(ActionEvent event) throws Exception {
    selectedCustomer = tableview.getSelectionModel().getSelectedItem();

    if (selectedCustomer != null) {
      // Confirm the deletion
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("Confirm Customer Deletion");
      alert.setHeaderText("Are you sure you want to delete the selected customer?");
      alert.setContentText("This action cannot be undone!");
      Optional<ButtonType> result = alert.showAndWait();
      if (result.get() == ButtonType.OK) {
        customerDAO.delete(selectedCustomer);
        buildData(); // Refresh the table view with the updated data
      }
    } else {

      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("No Customer Selected");
      alert.setContentText("Please select a customer to delete!");
      alert.showAndWait();
    }
  }

  /**
   * This method is called when the "Update Customer" button is clicked.
   * It navigates to the update customer page for the selected customer.
   *
   * @param event The ActionEvent triggered by the button click.
   * @throws Exception If an error occurs during the navigation.
   */
  @FXML void actionCustUpdate(ActionEvent event) throws Exception {
    selectedCustomer = tableview.getSelectionModel().getSelectedItem();
    if (selectedCustomer == null) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("No Customer Selected");
      alert.setContentText("Please select a customer to update!");
      alert.showAndWait();
    } else {
      try {
        // Open the update customer page
        Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        fxmlHelper.loadFXMLScene("/com/robertSchofield/JAVAdb/modCustomers.fxml", "Mod Customer",
            currentStage);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * This method is called when the "Back to Menu" button is clicked.
   * It navigates back to the main menu.
   *
   * @param event The ActionEvent triggered by the button click.
   * @throws Exception If an error occurs during the navigation.
   */
  @FXML void backToMenu(ActionEvent event) throws Exception {
    // Go back to the main menu
    Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    fxmlHelper.loadFXMLScene("/com/robertSchofield/JAVAdb/mainMenu.fxml", "Main Menu",
        currentStage);
  }

  /**
   * This method fetches customer data from the database and populates it in the TableView.
   * It also dynamically creates table columns based on the customer class fields.
   *
   * @throws SQLException If an error occurs during the database operation.
   */
  public void buildData() throws SQLException {
    try {
      JDBC.openConnection(); // Use your JDBC connection method

      // Fetch customer data from the database
      ObservableList<customers> customersList =
          com.robertSchofield.JAVAdb.DAOs.customerDAO.listAll();

      // Check if the list is not loginController
      if (customersList != null && !customersList.isEmpty()) {
        // Clear existing columns
        tableview.getColumns().clear();

        // Map field names to their corresponding column names
        Map<String, String> columnNamesMap = new HashMap<>();
        columnNamesMap.put("customerID", "Customer ID");
        columnNamesMap.put("customerName", "Customer Name");
        columnNamesMap.put("address", "Address");
        columnNamesMap.put("postalCode", "Postal Code");
        columnNamesMap.put("phoneNumber", "Phone Number");
        columnNamesMap.put("countryName", "Country");
        columnNamesMap.put("createdBy", "Created By");
        columnNamesMap.put("lastUpdate", "Last Updated");
        columnNamesMap.put("lastUpdatedBy", "Last Updated By");

        // Create a new list of field names in the desired order
        List<String> reorderedFieldNames =
            Arrays.asList("customerID", "customerName", "address", "postalCode", "phoneNumber",
                "countryName", "createdBy", "lastUpdate", "lastUpdatedBy");

        // Add table columns dynamically
        for (String fieldName : reorderedFieldNames) {
          // Exclude unwanted field names
          if (!fieldName.equals("divName")
              && !fieldName.equals("totalCustomers")
              && !fieldName.equals("divID")
              && !fieldName.equals("countryID")
              && !fieldName.equals("createdDate")
              && !fieldName.equals("lastUpdate")) {
            // Add table columns dynamically
            TableColumn<customers, String> col =
                new TableColumn<>(columnNamesMap.getOrDefault(fieldName, fieldName));
            col.setCellValueFactory(param -> {
              try {
                Method getterMethod = customers.class.getMethod(
                    "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));

                Object value = getterMethod.invoke(param.getValue());

                if (value instanceof LocalDateTime) {
                  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                  return new SimpleStringProperty(formatter.format((LocalDateTime) value));
                } else {
                  return new SimpleStringProperty(String.valueOf(value));
                }
              } catch (NoSuchMethodException | IllegalAccessException |
                       InvocationTargetException e) {
                throw new RuntimeException(e);
              }
            });
            tableview.getColumns().add(col);
          }
        }

        // Add data to TableView
        tableview.setItems(customersList);
        System.out.println("Data successfully loaded into TableView");
      } else {
        System.out.println("No customers found");
      }
    } catch (Exception e) {
      System.out.println("Error on Building Data");
    } finally {
      JDBC.closeConnection(); // Close the connection when done
    }
  }

  /**
   * This method is called when the customerController class is initialized.
   * It calls the buildData method to populate the customer data in the TableView.
   *
   * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
   * @param resources The resources used to localize the root object, or null if the root object was not localized.
   */
  @Override public void initialize(URL location, ResourceBundle resources) {
    try {
      buildData();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}

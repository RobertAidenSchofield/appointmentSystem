package com.robertSchofield.JAVAdb.controller;

import com.robertSchofield.JAVAdb.DAOs.countryDAO;
import com.robertSchofield.JAVAdb.DAOs.customerDAO;
import com.robertSchofield.JAVAdb.DAOs.firstLevelDivDAO;
import com.robertSchofield.JAVAdb.helper.JDBC;
import com.robertSchofield.JAVAdb.helper.fxmlHelper;
import com.robertSchofield.JAVAdb.model.country;
import com.robertSchofield.JAVAdb.model.customers;
import com.robertSchofield.JAVAdb.model.firstLevelDiv;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static com.robertSchofield.JAVAdb.controller.customerController.selectedCustomer;

/**
 * This class is the controller for the "Modify Customer" window. It handles the functionality to modify an existing customer's details.
 *
 * @Author Robert Schofield
 */
public class modCustomerController implements Initializable {
  private static final customerDAO customerDAO = new customerDAO();
  private static final countryDAO countryDAO = new countryDAO();
  private static final firstLevelDivDAO firstLevelDivDAO = new firstLevelDivDAO();
  @FXML private TextField customerAddressTextField;
  @FXML private TextField customerIDTextField;
  @FXML private TextField customerNameTextField;
  @FXML private TextField customerPhoneTextField;
  @FXML private TextField customerPostalTextField;
  @FXML private ComboBox<country> customerCountryCombo;
  @FXML private ComboBox<firstLevelDiv> customerDivisionCombo;
  @FXML private Button cancelButton;
  @FXML private Button saveButton;

  /**
   * This method handles the action when the "Cancel" button is clicked. It prompts the user to confirm the cancellation and then
   * returns to the "Customers Table" window.
   *
   * @param event The event that triggered this method.
   * @throws Exception If an error occurs while loading the "Customers Table" window.
   */
  @FXML void actionCancelButton(ActionEvent event) throws Exception {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Cancel updating Customer");
    alert.setHeaderText("Are you sure you want to cancel updating the customer?");
    alert.setContentText("All entered data will be lost.");
    Optional<ButtonType> result = alert.showAndWait();

    if (result.isPresent() && result.get() == ButtonType.OK) {
      Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
      fxmlHelper.loadFXMLScene("/com/robertSchofield/JAVAdb/Customers.fxml", "Customers Table",
          currentStage);
    }
  }

  /**
   * This method handles the action when the selected country in the "Country" combo box changes. It loads the corresponding
   * divisions for the selected country into the "Division" combo box.
   *
   * @param event The event that triggered this method.
   */
  @FXML void actionCountryLoad(ActionEvent event) {
    country selectedCountry = customerCountryCombo.getSelectionModel().getSelectedItem();
    loadDivisions(selectedCountry.getCountryId());
  }

  /**
   * This method loads the divisions for a given country into the "Division" combo box.
   *
   * @param countryId The ID of the selected country.
   * @lambda expression is used to load the divisions for a given country into the "Division" combo box.
   * This is done using the filter method of the Stream interface, which takes a predicate as an argument.
   * In this case, the predicate is a lambda expression that checks if the country ID of each division matches the selected country ID.
   */
  public void loadDivisions(int countryId) {
    customerDivisionCombo.setItems(firstLevelDivDAO.listAll()
        .stream()
        .filter(d -> d.getCountryId() == countryId)
        .collect(Collectors.toCollection(FXCollections::observableArrayList)));
  }

  /**
   * This method handles the action when the "Save" button is clicked. It validates the input fields, updates the customer's details,
   * and saves the changes to the database.
   *
   * @param event The event that triggered this method.
   * @throws SQLException If an error occurs while updating the customer's details.
   */
  @FXML void actionSaveButton(ActionEvent event) throws Exception {
    JDBC.openConnection();
    firstLevelDiv selectedDivision = customerDivisionCombo.getSelectionModel().getSelectedItem();

    if (areFieldsEmpty()) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText("Error Saving Customer");
      alert.setContentText("Please fill in all fields.");
      alert.showAndWait();
      return;
    }
    String customerName = customerNameTextField.getText();
    String customerPhone = customerPhoneTextField.getText();
    String customerAddress = customerAddressTextField.getText();
    String customerPostal = customerPostalTextField.getText();
    int divisionID = selectedDivision.getDivisionId();
    int customerID = Integer.parseInt(customerIDTextField.getText());
    customers customer =
        new customers(customerID, customerName, customerAddress, customerPostal, customerPhone,
            divisionID);
    customerDAO.update(customer);

    // Display success message
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Customer Updated");
    alert.setHeaderText(null);
    alert.setContentText("Customer Updated successfully.");
    alert.showAndWait();

    // Go back to the customers table view
    Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    fxmlHelper.loadFXMLScene("/com/robertSchofield/JAVAdb/Customers.fxml", "Customers",
        currentStage);
  }

  /**
   * This method checks if any of the input fields are empty.
   *
   * @return True if any of the input fields are empty, false otherwise.
   */
  private boolean areFieldsEmpty() {
    return customerIDTextField.getText().isEmpty()
        || customerNameTextField.getText().isEmpty()
        || customerPhoneTextField.getText().isEmpty()
        || customerAddressTextField.getText().isEmpty()
        || customerPostalTextField.getText().isEmpty();
  }

  /**
   * This method initializes the controller by setting up the input fields and loading the available countries and divisions into
   * the respective combo boxes.
   *
   * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
   * @param resources The resources used to localize the root object, or null if the root object was not localized.
   */
  @Override public void initialize(URL location, ResourceBundle resources) {
    //addCustomerController.stringConvert(customerCountryCombo, customerDivisionCombo);
    customerIDTextField.setText(String.valueOf(selectedCustomer.getCustomerID()));
    customerNameTextField.setText(selectedCustomer.getCustomerName());
    customerPhoneTextField.setText(selectedCustomer.getPhoneNumber());
    customerAddressTextField.setText(selectedCustomer.getAddress());
    customerPostalTextField.setText(selectedCustomer.getPostalCode());
    customerCountryCombo.setItems(countryDAO.listAll());
    customerDivisionCombo.setItems(firstLevelDivDAO.listAll());
    addCustomerController.stringConvert(customerCountryCombo, customerDivisionCombo);
    // Load the selected country and division into the combo boxes

    customerCountryCombo.setValue(countryDAO.find(selectedCustomer.getCountryID()));

    try {
      customerDivisionCombo.setValue(
          firstLevelDivDAO.findByDivisionId(selectedCustomer.getDivID()));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}




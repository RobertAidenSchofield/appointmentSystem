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
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * This class handles the functionality for adding a new customer to the database.
 * It includes methods for handling user interactions, validating input, and saving the customer to the database.
 *
 * @Author Robert Schofield
 */
public class addCustomerController implements Initializable {
  private final firstLevelDivDAO firstLevelDivDAO = new firstLevelDivDAO();
  private final countryDAO countryDAO = new countryDAO();
  private final customerDAO customerDAO = new customerDAO();

  @FXML private Button cancelButton;

  @FXML private TextField customerAddressTextField;

  @FXML private ComboBox<country> customerCountryCombo;

  @FXML private ComboBox<firstLevelDiv> customerDivisionCombo;

  @FXML private TextField customerIDTextField;

  @FXML private TextField customerNameTextField;

  @FXML private TextField customerPhoneTextField;

  @FXML private TextField customerPostalTextField;

  @FXML private Button saveButton;

  /**
   * Converts the country and first-level division objects to strings for display in the ComboBoxes.
   *
   * @param customerCountryCombo  The ComboBox for selecting a country.
   * @param customerDivisionCombo The ComboBox for selecting a first-level division.
   */
  static void stringConvert(ComboBox<country> customerCountryCombo,
      ComboBox<firstLevelDiv> customerDivisionCombo) {
    customerCountryCombo.setConverter(new StringConverter<>() {
      @Override public String toString(country country) {
        return country != null ? country.getCountry() : "";
      }

      @Override public country fromString(String s) {
        return null;
      }
    });

    customerDivisionCombo.setConverter(new StringConverter<>() {
      @Override public String toString(firstLevelDiv firstLevelDiv) {
        return firstLevelDiv != null ? firstLevelDiv.getDivision() : "";
      }

      @Override public firstLevelDiv fromString(String s) {
        return null;
      }
    });
  }

  /**
   * Handles the cancel button click event.
   * Displays a confirmation dialog to the user and clears the input fields if the user confirms the cancellation.
   *
   * @param event The ActionEvent triggered by the cancel button click.
   * @throws Exception If an error occurs while loading the customers table view.
   * @lambda expression is used to handle the result of a confirmation dialog.
   * This lambda expression clears all input fields and selections, and then loads the customers table view.
   */
  @FXML void actionCancelButton(ActionEvent event) throws Exception {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Cancel Adding Customer");
    alert.setHeaderText("Are you sure you want to cancel adding a new customer?");
    alert.setContentText("All entered data will be lost.");
    Optional<ButtonType> result = alert.showAndWait();
    result.ifPresent(buttonType -> {
      if (buttonType == ButtonType.OK) {
        customerIDTextField.clear();
        customerNameTextField.clear();
        customerPhoneTextField.clear();
        customerAddressTextField.clear();
        customerPostalTextField.clear();
        customerCountryCombo.getSelectionModel().clearSelection();
        customerDivisionCombo.getSelectionModel().clearSelection();
        // Go back to the customers table view
        try {
          Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
          fxmlHelper.loadFXMLScene("/com/robertSchofield/JAVAdb/Customers.fxml", "Customers",
              currentStage);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Handles the country selection event.
   * Loads the first-level divisions for the selected country into the customerDivisionCombo.
   *
   * @param event The ActionEvent triggered by the country selection.
   */
  @FXML void actionCountryLoad(ActionEvent event) {
    country selectedCountry = customerCountryCombo.getSelectionModel().getSelectedItem();
    if (selectedCountry != null) {
      loadDivisions(selectedCountry.getCountryId());
    } else {
      // Clear the divisions combo box if no country is selected
      customerDivisionCombo.setItems(FXCollections.emptyObservableList());
    }
  }

  /**
   * Loads the first-level divisions for the specified country ID into the customerDivisionCombo.
   *
   * @param countryId The ID of the selected country.
   * @lambda expression is used to filter the list of first-level divisions based on the selected country ID
   * This is done using the filter method of the Stream interface, which takes a predicate as an argument.
   * In this case, the predicate is a lambda expression that checks if the country ID of each division matches
   */
  public void loadDivisions(int countryId) {
    customerDivisionCombo.setItems(firstLevelDivDAO.listAll()
        .stream()
        .filter(d -> d.getCountryId() == countryId)
        .collect(Collectors.toCollection(FXCollections::observableArrayList)));
  }

  /**
   * Handles the save button click event.
   * Validates the input fields, creates a new customer object, saves it to the database, and displays a success message.
   *
   * @param event The ActionEvent triggered by the save button click.
   * @throws Exception If an error occurs while saving the customer to the database or loading the customers table view.
   */
  @FXML void actionSaveButton(ActionEvent event) throws Exception {
    JDBC.openConnection();
    country selectedCountry = customerCountryCombo.getSelectionModel().getSelectedItem();
    firstLevelDiv selectedDivision = customerDivisionCombo.getSelectionModel().getSelectedItem();

    // Get the values from the UI components
    String customerName = customerNameTextField.getText();
    String customerPhone = customerPhoneTextField.getText();
    String customerAddress = customerAddressTextField.getText();
    String customerPostal = customerPostalTextField.getText();
    int divisionID = selectedDivision.getDivisionId();
    // Validate the input fields
    if (customerName.isEmpty()
        || customerPhone.isEmpty()
        || customerAddress.isEmpty()
        || customerPostal.isEmpty()
        || selectedCountry == null
        || selectedDivision == null) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Invalid Input");
      alert.setHeaderText(null);
      alert.setContentText("Please fill in all required fields and ensure valid inputs.");
      alert.showAndWait();
      return;
    }

    customers customer =
        new customers(customerName, customerAddress, customerPostal, customerPhone, divisionID);

    // Save the customer to the database
    customerDAO.add(customer);

    // Display success message
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Customer Added");
    alert.setHeaderText(null);
    alert.setContentText("Customer added successfully.");
    alert.showAndWait();

    // Clear the input fields
    customerIDTextField.clear();
    customerNameTextField.clear();
    customerPhoneTextField.clear();
    customerAddressTextField.clear();
    customerPostalTextField.clear();
    customerCountryCombo.getSelectionModel().clearSelection();
    customerDivisionCombo.getSelectionModel().clearSelection();
    // Go back to the customers table view

    Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    fxmlHelper.loadFXMLScene("/com/robertSchofield/JAVAdb/Customers.fxml", "Customers",
        currentStage);
  }

  /**
   * Initializes the controller by loading the countries and first-level divisions into the respective ComboBoxes.
   *
   * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
   * @param resources The resources used to localize the root object, or null if the root object was not localized.
   */
  @Override public void initialize(URL location, ResourceBundle resources) {

    ObservableList<country> countries = countryDAO.listAll();
    customerCountryCombo.setItems(countries);
    // Load first-level divisions into the customerDivisionCombo
    ObservableList<firstLevelDiv> divisions = firstLevelDivDAO.listAll();
    customerDivisionCombo.setItems(divisions);

    stringConvert(customerCountryCombo, customerDivisionCombo);
  }
}

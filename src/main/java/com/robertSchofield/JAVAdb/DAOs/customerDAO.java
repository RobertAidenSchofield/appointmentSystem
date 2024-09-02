package com.robertSchofield.JAVAdb.DAOs;

import com.robertSchofield.JAVAdb.helper.JDBC;
import com.robertSchofield.JAVAdb.model.customers;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import static com.robertSchofield.JAVAdb.helper.JDBC.connection;

/**
 * This class provides methods for interacting with the 'customers' table in the database.
 * It includes operations such as retrieving customer data, adding new customers, updating existing customers,
 * deleting customers, and finding customers by ID or name.
 *
 * @Author Robert Schofield
 */
public class customerDAO {

  /**
   * Retrieves all customers from the database, including their division and country information.
   *
   * @return An observable list of all customers.
   */
  public static ObservableList<customers> listAll() {
    JDBC.openConnection();
    ObservableList<customers> allCustList = FXCollections.observableArrayList();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT * FROM customers, first_level_divisions, countries WHERE "
              + "customers.Division_ID = first_level_divisions.Division_ID AND "
              + "first_level_divisions.Country_ID = countries.Country_ID");
      ResultSet result = statement.executeQuery();

      while (result.next()) {
        allCustList.add(
            new customers(result.getInt("Customer_ID"), result.getString("Customer_Name"),
                result.getString("Address"), result.getString("Postal_Code"),
                result.getString("Phone"), result.getTimestamp("Create_Date").toLocalDateTime(),
                result.getString("Created_By"),
                result.getTimestamp("Last_Update").toLocalDateTime(),
                result.getString("Last_Updated_By"), result.getInt("Division_ID"),
                result.getInt("Country_ID"), result.getString("Country")));
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Error retrieving customers");
    }
    return allCustList;
  }

  /**
   * Retrieves a list of customers along with their respective divisions and total number of customers per division.
   *
   * @return An observable list of customers with division and total customers information.
   */
  public ObservableList<customers> getCustDivTotal() {
    JDBC.openConnection();
    ObservableList<customers> custDivTotalList = FXCollections.observableArrayList();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT d.Division, COUNT(c.Customer_ID) AS Total_Customers "
              + "FROM customers c "
              + "JOIN first_level_divisions d ON c.Division_ID = d.Division_ID "
              + "GROUP BY d.Division");
      ResultSet result = statement.executeQuery();

      while (result.next()) {
        customers customers =
            new customers(result.getString("Division"), result.getInt("Total_Customers"));
        // Add the division name and total customers to the list
        custDivTotalList.add(customers);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("Error retrieving customers by division");
    }
    return custDivTotalList;
  }

  /**
   * Adds a new customer to the database.
   *
   * @param customer The customer object to be added.
   */
  public void add(customers customer) {
    JDBC.openConnection();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Create_Date,Created_By,Last_Update,Last_Updated_By, Division_ID) VALUES (?,?,?,?,?,?,?,?,?)");
      statement.setString(1, customer.getCustomerName());
      statement.setString(2, customer.getAddress());
      statement.setString(3, customer.getPostalCode());
      statement.setString(4, customer.getPhoneNumber());
      statement.setTimestamp(5, java.sql.Timestamp.valueOf(LocalDate.now().atStartOfDay()));
      statement.setString(6, userDAO.getLoggedInUser());
      statement.setTimestamp(7, java.sql.Timestamp.valueOf(LocalDate.now().atStartOfDay()));
      statement.setString(8, userDAO.getLoggedInUser());
      statement.setInt(9, customer.getDivID());

      statement.executeUpdate();
      System.out.println("New customer added");
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Error adding customer");
    }
  }

  /**
   * Updates an existing customer in the database.
   *
   * @param customer The customer object with updated information.
   */
  public void update(customers customer) {
    JDBC.openConnection();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "UPDATE customers SET Customer_Name=?, Address=?, Postal_Code=?, Phone=?, Last_Update=?,  Last_Updated_By=?,Division_ID=? WHERE Customer_ID=?");
      statement.setString(1, customer.getCustomerName());
      statement.setString(2, customer.getAddress());
      statement.setString(3, customer.getPostalCode());
      statement.setString(4, customer.getPhoneNumber());
      statement.setTimestamp(5, java.sql.Timestamp.valueOf(LocalDate.now().atStartOfDay()));
      statement.setString(6, userDAO.getLoggedInUser());
      statement.setInt(7, customer.getDivID());
      statement.setInt(8, customer.getCustomerID());

      statement.executeUpdate();
      System.out.println("Customer updated");
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Error updating customer");
    }
  }

  /**
   * Deletes a customer from the database. If the customer has associated appointments,
   * an alert is displayed and the appointments are deleted before the customer is deleted.
   *
   * @param customer The customer object to be deleted.
   */
  public void delete(customers customer) {
    JDBC.openConnection();
    int id = customer.getCustomerID();
    // Check if there are any appointments associated with this customer before deleting
    try {
      PreparedStatement statement =
          connection.prepareStatement("SELECT COUNT(*) FROM appointments WHERE Customer_ID=?");
      statement.setInt(1, id);
      ResultSet resultSet = statement.executeQuery();
      resultSet.next();
      int appointmentCount = resultSet.getInt(1);

      if (appointmentCount > 0) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Delete Customer");
        alert.setContentText("Deleting customer with appointments.");
        alert.showAndWait();
        PreparedStatement deleteAppointmentsStatement =
            connection.prepareStatement("DELETE FROM appointments WHERE Customer_ID=?");
        deleteAppointmentsStatement.setInt(1, id);
        deleteAppointmentsStatement.executeUpdate();

        PreparedStatement deleteCustomerStatement =
            connection.prepareStatement("DELETE FROM customers WHERE Customer_ID=?");
        deleteCustomerStatement.setInt(1, id);
        deleteCustomerStatement.executeUpdate();
      } else {
        PreparedStatement preparedStatement =
            connection.prepareStatement("DELETE FROM customers WHERE Customer_ID=?");
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Finds a customer by their ID.
   *
   * @param id The ID of the customer to be found.
   * @return The customer object with the matching ID, or null if no customer is found.
   */
  public customers findById(int id) {
    JDBC.openConnection();

    for (customers customer : listAll()) {
      if (customer.getCustomerID() == id) {
        return customer;
      }
    }
    return null; // Customer not found
  }

  /**
   * Finds a customer by their name.
   *
   * @param customerName The name of the customer to be found.
   * @return The ID of the customer with the matching name, or 0 if no customer is found.
   */
  public int findByName(String customerName) {
    JDBC.openConnection();
    int customerID = 0;
    try {
      PreparedStatement statement =
          connection.prepareStatement("SELECT * FROM customers WHERE Customer_Name=?");
      statement.setString(1, customerName);
      ResultSet result = statement.executeQuery();

      if (result.next()) {
        customerID = result.getInt("Customer_ID");
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Error finding customer by name");
    }
    return customerID;
  }
}

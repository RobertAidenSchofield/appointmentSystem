package com.robertSchofield.JAVAdb.DAOs;

import com.robertSchofield.JAVAdb.helper.JDBC;
import com.robertSchofield.JAVAdb.model.contact;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import static com.robertSchofield.JAVAdb.helper.JDBC.connection;

/**
 * The type Contact dao.
 *
 * @Author Robert Schofield
 */
public class contactDAO {
  /**
   * Retrieves all contacts from the database and returns them as an ObservableList.
   *
   * @return An ObservableList of all contacts in the database. If an error occurs during the retrieval, an empty list is returned and an error message is printed to the console.
   */
  public static ObservableList<contact> listAll() {
    JDBC.openConnection();
    ObservableList<contact> allContactsList = FXCollections.observableArrayList();
    try {
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM contacts");
      ResultSet result = statement.executeQuery();

      while (result.next()) {
        allContactsList.add(
            new contact(result.getInt("Contact_ID"), result.getString("Contact_Name"),
                result.getString("Email")));
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Error retrieving contacts");
    }
    return allContactsList;
  }

  /**
   * Searches for a contact in the database by its ID.
   *
   * @param contact The contact object containing the ID of the contact to be found.
   * @return The contact object if found, otherwise returns null. If an error occurs during the search, an error message is printed to the console and null is returned.
   */
  public static contact find(contact contact) {
    JDBC.openConnection();
    contact foundContact = null;
    try {
      PreparedStatement statement =
          connection.prepareStatement("SELECT * FROM contacts WHERE Contact_ID=?");
      statement.setInt(1, contact.getContactId());
      ResultSet result = statement.executeQuery();

      if (result.next()) {
        foundContact = new contact(result.getInt("Contact_ID"), result.getString("Contact_Name"),
            result.getString("Email"));
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Error finding contact");
    }
    return foundContact;
  }

  /**
   * Searches for a contact in the database by its ID.
   *
   * @param contactId The ID of the contact to be found.
   * @return The contact object if found, otherwise returns null. If an error occurs during the search, an error message is printed to the console and null is returned.
   */
  public static contact findById(int contactId) {
    JDBC.openConnection();
    contact foundContact = null;
    try {
      PreparedStatement statement =
          connection.prepareStatement("SELECT * FROM contacts WHERE Contact_ID=?");
      statement.setInt(1, contactId);
      ResultSet result = statement.executeQuery();

      if (result.next()) {
        foundContact = new contact(result.getInt("Contact_ID"), result.getString("Contact_Name"),
            result.getString("Email"));
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Error finding contact by ID");
    }
    return foundContact;
  }

  /**
   * Searches for a contact in the database by their name.
   *
   * @param contactName The name of the contact to be found.
   * @return The ID of the contact if found, otherwise returns 0. If an error occurs during the search, an error message is printed to the console and 0 is returned.
   */
  public int findByName(String contactName) {
    JDBC.openConnection();
    int contactID = 0;
    try {
      PreparedStatement statement =
          connection.prepareStatement("SELECT * FROM contacts WHERE Contact_Name=?");
      statement.setString(1, contactName);
      ResultSet result = statement.executeQuery();

      if (result.next()) {
        contactID = result.getInt("Contact_ID");
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Error finding contact by name");
    }
    return contactID;
  }
}
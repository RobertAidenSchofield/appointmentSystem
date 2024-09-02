package com.robertSchofield.JAVAdb.DAOs;

import com.robertSchofield.JAVAdb.helper.JDBC;
import com.robertSchofield.JAVAdb.model.country;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import static com.robertSchofield.JAVAdb.helper.JDBC.connection;

/**
 * This class provides methods for interacting with the 'countries' table in a database.
 *
 * @Author Robert Schofield
 */
public class countryDAO {

  /**
   * Adds a new Country record to the database.
   *
   * @param country The country object to be added.
   * @throws SQLException If an error occurs while executing the SQL statement.
   */
  public void add(country country) throws SQLException {
    JDBC.openConnection();
    String sql = "INSERT INTO countries (Country) VALUES (?)";
    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setString(1, country.getCountry());
    statement.executeUpdate();
  }

  /**
   * Retrieves all Country records from the database.
   *
   * @return An ObservableList of country objects.
   */
  public ObservableList<country> listAll() {
    JDBC.openConnection();
    ObservableList<country> countries = FXCollections.observableArrayList();
    try {
      String sql = "SELECT * FROM countries";
      PreparedStatement statement = connection.prepareStatement(sql);
      ResultSet resultSet = statement.executeQuery();

      while (resultSet.next()) {
        countries.add(new country(resultSet.getInt("Country_ID"), resultSet.getString("Country")));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("Error retrieving countries");
    }
    return countries;
  }

  /**
   * Updates an existing Country record in the database.
   *
   * @param country The country object with updated information.
   */
  public void update(country country) {
    JDBC.openConnection();
    try {
      String sql = "UPDATE countries SET country_name=?  WHERE country_id=?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setString(1, country.getCountry());
      statement.setInt(2, country.getCountryId());
      statement.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Error updating country");
    }
  }

  /**
   * Deletes a Country record from the database by its ID.
   *
   * @param countryId The ID of the country to be deleted.
   * @throws SQLException If an error occurs while executing the SQL statement.
   */
  public void delete(int countryId) throws SQLException {
    JDBC.openConnection();
    try {
      String sql = "DELETE FROM countries WHERE country_id=?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setInt(1, countryId);
      statement.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Error deleting country");
    }
  }

  /**
   * Finds a Country record in the database by its ID.
   *
   * @param countryId The ID of the country to be found.
   * @return The country object if found, or null if not found.
   */
  public country find(int countryId) {
    JDBC.openConnection();
    for (country country : listAll()) {
      if (country.getCountryId() == countryId) {
        return country;
      }
    }
    return null;
  }
}
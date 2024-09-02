package com.robertSchofield.JAVAdb.DAOs;

import com.robertSchofield.JAVAdb.helper.JDBC;
import com.robertSchofield.JAVAdb.model.firstLevelDiv;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import static com.robertSchofield.JAVAdb.helper.JDBC.connection;

/**
 * This class provides methods for interacting with the first_level_divisions table in a database.
 * It uses JDBC for database operations and assumes the existence of a JDBC connection.
 *
 * @Author Robert Schofield
 */
public class firstLevelDivDAO {

  /**
   * Finds a firstLevelDiv record by its division ID.
   *
   * @param divisionId The ID of the division to search for.
   * @return The firstLevelDiv object with the matching division ID, or null if not found.
   * @throws SQLException If an error occurs while executing the SQL statement.
   */
  public firstLevelDiv findByDivisionId(int divisionId) throws SQLException {
    JDBC.openConnection();
    try {
      String sql = "SELECT * FROM first_level_divisions WHERE Division_ID=?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setInt(1, divisionId);
      ResultSet resultSet = statement.executeQuery();

      if (resultSet.next()) {
        return new firstLevelDiv(resultSet.getInt("division_id"), resultSet.getString("division"),
            resultSet.getInt("country_id"));
      } else {
        return null; // Division not found
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Error retrieving div by division ID");
      return null;
    }
  }

  /**
   * Retrieves all firstLevelDiv records from the database.
   *
   * @return An ObservableList of firstLevelDiv objects.
   */
  public ObservableList<firstLevelDiv> listAll() {
    JDBC.openConnection();
    ObservableList<firstLevelDiv> divs = FXCollections.observableArrayList();
    try {
      String sql = "SELECT * FROM first_level_divisions";
      PreparedStatement statement = connection.prepareStatement(sql);
      ResultSet resultSet = statement.executeQuery();

      while (resultSet.next()) {
        divs.add(new firstLevelDiv(resultSet.getInt("division_id"), resultSet.getString("division"),
            resultSet.getInt("country_id")));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("Error retrieving divs");
    }
    return divs;
  }
}

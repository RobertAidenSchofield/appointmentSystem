package com.robertSchofield.JAVAdb.DAOs;

import com.robertSchofield.JAVAdb.helper.JDBC;
import com.robertSchofield.JAVAdb.model.user;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import static com.robertSchofield.JAVAdb.helper.JDBC.connection;

/**
 * This class provides methods for interacting with the 'users' table in the database.
 *
 * @Author Robert Schofield
 */
public class userDAO {
  private static user loggedInUser;

  /**
   * Returns the username of the currently logged-in user.
   *
   * @return The username of the logged-in user.
   */
  public static String getLoggedInUser() {
    String userName = loggedInUser.getUserName();
    return userName;
  }

  /**
   * Retrieves all users from the 'users' table and returns them as an ObservableList.
   *
   * @return An ObservableList containing all users.
   */
  public static ObservableList<user> listAll() {
    ObservableList<user> userList = FXCollections.observableArrayList();
    try {
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM users");
      ResultSet resultSet = statement.executeQuery();

      while (resultSet.next()) {
        userList.add(new user(resultSet.getInt("User_ID"), resultSet.getString("User_Name"),
            resultSet.getString("Password")));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return userList;
  }

  /**
   * Attempts to find a user in the 'users' table based on the provided user object.
   *
   * @param user The user object containing the username and password to search for.
   * @return The user object if found, or null if not found.
   * @throws SQLException If an error occurs while executing the SQL query.
   */
  public static user find(user user) throws SQLException {

    PreparedStatement statement =
        connection.prepareStatement("SELECT * FROM users WHERE User_Name =? and Password =?");
    statement.setString(1, user.getUserName());
    statement.setString(2, user.getPassword());

    try {
      ResultSet result = statement.executeQuery();

      if (result.next()) {
        loggedInUser = new user();
        loggedInUser.setUserID(result.getInt("User_ID"));
        loggedInUser.setUserName(result.getString("User_Name"));
        loggedInUser.setPassword(result.getString("Password"));
        return loggedInUser;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return loggedInUser;
  }

  /**
   * Finds a user in the 'users' table based on the provided user ID.
   *
   * @param userID The ID of the user to search for.
   * @return The user object if found, or null if not found.
   */
  public user findById(int userID) {
    JDBC.openConnection();
    for (user user : listAll()) {
      if (user.getUserID() == userID) {
        return user;
      }
    }
    return null; // User not found
  }
}

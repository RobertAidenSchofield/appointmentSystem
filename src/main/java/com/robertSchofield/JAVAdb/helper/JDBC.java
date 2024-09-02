package com.robertSchofield.JAVAdb.helper;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * The type Jdbc.
 *
 * @Author Robert Schofield
 */
public abstract class JDBC {

  private static final String protocol = "jdbc";
  private static final String vendor = ":mysql:";
  private static final String location = "//localhost/";
  private static final String databaseName = "client_schedule";
  private static final String jdbcUrl =
      protocol + vendor + location + databaseName + "?connectionTimeZone = UTC"; // LOCAL
  private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
  private static final String userName = "sqlUser"; // Username
  private static final String password = "Passw0rd!"; // Password
  /**
   * The constant connection.
   */
  public static Connection connection; // Connection Interface

  /**
   * Open connection.
   */
  public static void openConnection() {
    try {
      Class.forName(driver); // Locate Driver
      connection =
          DriverManager.getConnection(jdbcUrl, userName, password); // Reference Connection object
    } catch (Exception e) {
      System.out.println("Error:" + e.getMessage());
    }
  }

  /**
   * Close connection.
   */
  public static void closeConnection() {
    try {
      connection.close();
      System.out.println("Connection closed!");
    } catch (Exception e) {
      System.out.println("Error:" + e.getMessage());
    }
  }

  /**
   * Gets connection.
   *
   * @return the connection
   */
  public static Connection getConnection() {
    return connection;
  }
}

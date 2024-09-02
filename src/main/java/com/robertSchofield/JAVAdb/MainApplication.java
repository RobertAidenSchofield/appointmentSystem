package com.robertSchofield.JAVAdb;

import com.robertSchofield.JAVAdb.helper.JDBC;
import com.robertSchofield.JAVAdb.helper.fxmlHelper;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The type Main application.
 */
public class MainApplication extends Application {

  /**
   * The entry point of application.
   *
   * @param args the input arguments
   */
  public static void main(String[] args) {
    try {
      JDBC.openConnection(); // Open Database Connection
      launch(args);
    } finally {
      JDBC.closeConnection(); // Close Database Connection
    }
  }

  @Override
  public void start(Stage stage) {
    // Declare a Stage variable at the class level
    try {
      fxmlHelper.loadFXMLScene("/com/robertSchofield/JAVAdb/login.fxml", "Login", stage);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}/**/


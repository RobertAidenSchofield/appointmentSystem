package com.robertSchofield.JAVAdb.helper;

import com.robertSchofield.JAVAdb.DAOs.appointmentDAO;
import com.robertSchofield.JAVAdb.model.appointments;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

/**
 * The type Alert handler.
 *
 * @Author Robert Schofield
 */
public class alertHandler {
  /**
   * A ResourceBundle for retrieving localized strings.
   */
  static ResourceBundle langBundle = ResourceBundle.getBundle("lang");

  /**
   * Displays an alert based on the provided alert type.
   *
   * @param alertType The type of alert to display.
   *                  <ul>
   *                  <li>1: Displays an alert with upcoming appointments within 15 minutes.</li>
   *                  <li>2: Displays an alert indicating incorrect password.</li>
   *                  <li>3: Displays an alert indicating incorrect username.</li>
   *                  <li>4: Displays an alert indicating an error with the username.</li>
   *                  <li>5: Displays an alert indicating an error with the password.</li>
   *                  <li>6: Displays an alert indicating incorrect username and password.</li>
   *                  <li>7: Displays an alert indicating an error with the username and password.</li>
   *                  <li>8: Displays an alert indicating a blank username.</li>
   *                  <li>9: Displays an alert indicating a blank password.</li>
   *                  <li>10: Displays an alert indicating no appointments within the next 15 minutes.</li>
   *                  <li>11: Displays an alert indicating a successful login.</li>
   *                  <li>12: Displays an alert asking for confirmation to exit.</li>
   *                  </ul>
   */
  public static void errorType(int alertType) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    switch (alertType) {
      case 1:
        // Check for upcoming appointments within 15 minutes
        ObservableList<appointments> upcomingAppointments =
            appointmentDAO.getUpcomingAppointments(15);
        // Format the date and time using the DateTimeFormatter
        String startDateTime = upcomingAppointments.get(0).toDate();
        alert.setContentText(langBundle.getString("appointmentswithinthenext15minutes.")
            + "\nAppointment ID: "
            + upcomingAppointments.get(0).getAppointmentID()
            + "\nDate | Time: "
            + startDateTime);
        alert.showAndWait();
        break;
      case 2:
        alert.setContentText(langBundle.getString("incorrectPassword"));
        alert.showAndWait();
        break;
      case 3:
        alert.setContentText(langBundle.getString("incorrectUsername"));
        alert.showAndWait();
        break;
      case 4:
        alert.setContentText(langBundle.getString("ErrorUsername"));
        alert.showAndWait();
        break;
      case 5:
        alert.setContentText(langBundle.getString("ErrorPassword"));
        alert.showAndWait();
        break;
      case 6:
        alert.setContentText(langBundle.getString("incorrectEverything"));
        alert.showAndWait();
        break;
      case 7:
        alert.setContentText(langBundle.getString("ErrorUserPass"));
        alert.showAndWait();
        break;
      case 8:
        alert.setContentText(langBundle.getString("blankUserName"));
        alert.showAndWait();
        break;
      case 9:
        alert.setContentText(langBundle.getString("blankPassword"));
        alert.showAndWait();
        break;
      case 10:
        alert.setContentText(langBundle.getString("Noappointmentswithinthenext15minutes."));
        alert.showAndWait();
        break;
      case 11:
        alert.setContentText(langBundle.getString("loggedInMessage"));
        alert.showAndWait();
        break;
      case 12:
        alert.setContentText(langBundle.getString("Pleaseconfirmexit"));
        alert.showAndWait();
        break;
      default:
    }
  }
}


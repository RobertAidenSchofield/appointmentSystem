package com.robertSchofield.JAVAdb.DAOs;

import com.robertSchofield.JAVAdb.helper.JDBC;
import com.robertSchofield.JAVAdb.model.appointments;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import static com.robertSchofield.JAVAdb.helper.JDBC.connection;

/**
 * This class is responsible for managing and interacting with appointment data in the database.
 * It provides methods for retrieving, adding, updating, and deleting appointments.
 * It also provides methods for retrieving appointment statistics and data.
 *
 * @Author Robert Schofield
 */
public class appointmentDAO {
  /**
   * The constant startOfWeek.
   */
  // Get the current date and the month add one month to it to get the next month's date.
  static LocalDate startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY);
  /**
   * The One week.
   */
  static LocalDate oneWeek = LocalDate.now().plusDays(7);

  /**
   * Retrieves a list of upcoming appointments based on the specified number of minutes.
   *
   * @param minutes The number of minutes in the future to consider as upcoming appointments.
   * @return An observable list of upcoming appointments.
   */
  public static ObservableList<appointments> getUpcomingAppointments(int minutes) {
    ObservableList<appointments> upcomingAppointments = FXCollections.observableArrayList();
    try {
      LocalDateTime currentTime = LocalDateTime.now();
      LocalDateTime futureTime = currentTime.plusMinutes(minutes);

      // Get the system's local time zone
      ZoneId localTimeZone = ZoneId.systemDefault();
      PreparedStatement c =
          connection.prepareStatement("SELECT * FROM appointments WHERE start >= ? AND start <= ?");
      c.setTimestamp(1,
          java.sql.Timestamp.valueOf(currentTime.atZone(localTimeZone).toLocalDateTime()));
      c.setTimestamp(2,
          java.sql.Timestamp.valueOf(futureTime.atZone(localTimeZone).toLocalDateTime()));
      ResultSet rs = c.executeQuery();
      while (rs.next()) {
        appointments appointment =
            new appointments(rs.getInt("Appointment_ID"), rs.getString("Title"),
                rs.getString("Description"), rs.getString("Location"), rs.getString("Type"),
                rs.getTimestamp("Start").toLocalDateTime(),
                rs.getTimestamp("End").toLocalDateTime(), rs.getInt("Customer_ID"),
                rs.getInt("User_ID"), rs.getInt("Contact_ID"));
        upcomingAppointments.add(appointment);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return upcomingAppointments;
  }

  /**
   * Deletes a specific appointment from the database based on the appointment ID.
   *
   * @param appointment The appointment object containing the appointment ID to delete.
   */
  public static void delete(appointments appointment) {
    JDBC.openConnection();
    try {
      PreparedStatement c =
          connection.prepareStatement("DELETE FROM appointments WHERE Appointment_ID =?");
      c.setInt(1, appointment.getAppointmentID());
      c.executeUpdate();
      System.out.println("Appointment with ID " + appointment.getAppointmentID() + " deleted");
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Error deleting appointment");
    }
  }

  /**
   * Retrieves a list of all appointments in the database.
   *
   * @return An observable list of all appointments.
   */
  public ObservableList<appointments> listAll() {
    ObservableList<appointments> appList = FXCollections.observableArrayList();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT * FROM appointments JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID ORDER BY appointments.Appointment_ID");

      ResultSet result = statement.executeQuery();

      while (result.next()) {
        appList.add(new appointments(result.getInt("Appointment_ID"), result.getString("Title"),
            result.getString("Description"), result.getString("Location"), result.getString("Type"),
            result.getTimestamp("Start").toLocalDateTime(),
            result.getTimestamp("End").toLocalDateTime(), result.getInt("Customer_ID"),
            result.getInt("User_ID"), result.getInt("Contact_ID")));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return appList;
  }

  /**
   * Retrieves a list of appointments associated with a specific contact ID.
   *
   * @param contactId The unique identifier of the contact.
   * @return An observable list of appointments associated with the given contact ID.
   */
  public ObservableList<appointments> listByContactId(int contactId) {
    ObservableList<appointments> appList = FXCollections.observableArrayList();
    try {
      PreparedStatement statement =
          connection.prepareStatement("SELECT * FROM appointments WHERE Contact_ID=?");
      statement.setInt(1, contactId);

      ResultSet result = statement.executeQuery();

      while (result.next()) {
        appList.add(new appointments(result.getInt("Appointment_ID"), result.getString("Title"),
            result.getString("Description"), result.getString("Location"), result.getString("Type"),
            result.getTimestamp("Start").toLocalDateTime(),
            result.getTimestamp("End").toLocalDateTime(), result.getInt("Customer_ID"),
            result.getInt("User_ID"), result.getInt("Contact_ID")));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return appList;
  }

  /**
   * Retrieves a list of appointments associated with a specific customer ID.
   *
   * @param customerID The unique identifier of the customer.
   * @return An observable list of appointments associated with the given customer ID. If no appointments are found, an empty observable list is returned. If an error occurs during the database query, the error is caught and printed, and an empty observable list is returned.
   */
  public ObservableList<appointments> listByCustomerId(int customerID) {
    ObservableList<appointments> appList = FXCollections.observableArrayList();
    try {
      PreparedStatement statement =
          connection.prepareStatement("SELECT * FROM appointments WHERE Customer_ID=?");
      statement.setInt(1, customerID);

      ResultSet result = statement.executeQuery();

      while (result.next()) {
        appList.add(new appointments(result.getInt("Appointment_ID"), result.getString("Title"),
            result.getString("Description"), result.getString("Location"), result.getString("Type"),
            result.getTimestamp("Start").toLocalDateTime(),
            result.getTimestamp("End").toLocalDateTime(), result.getInt("Customer_ID"),
            result.getInt("User_ID"), result.getInt("Contact_ID")));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return appList;
  }

  /**
   * Finds and retrieves a specific appointment based on the appointment ID.
   *
   * @param appointment The appointment object containing the appointment ID to find.
   * @return The found appointment object, or a new appointment object if no appointment is found.
   */
  public appointments find(appointments appointment) {
    appointments foundAppointment = new appointments();
    try {
      PreparedStatement statement = connection.prepareStatement("select * from appointments as a "
          + "left join contacts as c on a.Contact_ID = c.Contact_ID where Appointment_ID = ?;");
      statement.setInt(1, appointment.getAppointmentID());
      ResultSet result = statement.executeQuery();

      if (result.next()) {
        foundAppointment =
            new appointments(result.getInt("Appointment_ID"), result.getString("Title"),
                result.getString("Description"), result.getString("Location"),
                result.getString("Type"), result.getTimestamp("Start").toLocalDateTime(),

                result.getTimestamp("End").toLocalDateTime(),

                result.getInt("Customer_ID"), result.getInt("User_ID"),
                result.getInt("Contact_ID"));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return foundAppointment;
  }

  /**
   * Retrieves a specific appointment based on the appointment ID.
   *
   * @param appointmentId The unique identifier of the appointment.
   * @return The found appointment object, or null if no appointment is found.
   */
  public appointments getAppointment(int appointmentId) {
    try {
      String sql = "SELECT * FROM appointments WHERE Appointment_ID=?";
      PreparedStatement ps = connection.prepareStatement(sql);
      ps.setInt(1, appointmentId);

      ResultSet result = ps.executeQuery();
      appointments apptResult = null;
      if (result.next()) {
        appointmentId = result.getInt("Appointment_ID");
        int customerId = result.getInt("Customer_ID");
        int userId = result.getInt("User_ID");
        int contactId = result.getInt("Contact_ID");
        String title = result.getString("Title");
        String description = result.getString("Description");
        String location = result.getString("Location");
        String type = result.getString("Type");
        LocalDateTime startDateTime = result.getTimestamp("Start").toLocalDateTime();
        LocalDateTime endDateTime = result.getTimestamp("End").toLocalDateTime();

        apptResult =
            new appointments(appointmentId, title, description, location, type, startDateTime,
                endDateTime, customerId, userId, contactId);
      }
      return apptResult;
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Updates an existing appointment in the database based on the appointment object provided.
   *
   * @param appointment The appointment object containing the updated appointment data.
   */
  public void update(appointments appointment) {
    JDBC.openConnection();

    try (PreparedStatement statement = connection.prepareStatement(
        "UPDATE appointments SET Title =?, Description =?, Location =?, Type =?, Start =?, End =?, Last_Update =?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?")) {
      statement.setString(1, appointment.getTitle());
      statement.setString(2, appointment.getDescription());
      statement.setString(3, appointment.getLocation());
      statement.setString(4, appointment.getType());
      statement.setTimestamp(5, Timestamp.valueOf(appointment.getStart()));
      statement.setTimestamp(6, Timestamp.valueOf(appointment.getEnd()));
      statement.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
      statement.setString(8, userDAO.getLoggedInUser());
      statement.setInt(9, appointment.getCustomerID());
      statement.setInt(10, appointment.getUserID());
      statement.setInt(11, appointment.getContactID());
      statement.setInt(12, appointment.getAppointmentID());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Adds a new appointment to the database based on the appointment object provided.
   *
   * @param appointment The appointment object containing the new appointment data.
   */
  public void add(appointments appointment) {
    JDBC.openConnection();
    try {
      // Add the appointment to the database
      PreparedStatement c = connection.prepareStatement(
          "INSERT INTO appointments (Title, Description, Location, Type, Start, End,Create_Date,Created_By,Last_Update,Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)");
      c.setString(1, appointment.getTitle());
      c.setString(2, appointment.getDescription());
      c.setString(3, appointment.getLocation());
      c.setString(4, appointment.getType());
      c.setTimestamp(5, Timestamp.valueOf(appointment.getStart()));
      c.setTimestamp(6, Timestamp.valueOf(appointment.getEnd()));
      c.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
      c.setString(8, userDAO.getLoggedInUser());
      c.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
      c.setString(10, userDAO.getLoggedInUser());
      c.setInt(11, appointment.getCustomerID());
      c.setInt(12, appointment.getUserID());
      c.setInt(13, appointment.getContactID());

      c.executeUpdate();
      System.out.println("New appointment added");
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Error adding appointment");
    }
  }

  /**
   * Retrieves a list of all appointment types and their respective counts from the database.
   *
   * @return An observable list of appointment types and their counts.
   */
  public ObservableList<appointments> getAllAppointmentTypes() {
    JDBC.openConnection();
    ObservableList<appointments> appointmentTypes = FXCollections.observableArrayList();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT Type, COUNT(*) AS Total FROM appointments GROUP BY Type");
      ResultSet result = statement.executeQuery();

      while (result.next()) {
        String type = result.getString("Type");
        int total = result.getInt("Total");
        appointmentTypes.add(new appointments(type, total));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return appointmentTypes;
  }

  /**
   * Retrieves a list of appointments scheduled for the current month from the database.
   *
   * @return An observable list of appointments scheduled for the current month.
   */
  public ObservableList<appointments> getAppointmentsByMonth() {
    JDBC.openConnection();
    ObservableList<appointments> appointmentsByMonth = FXCollections.observableArrayList();

    try {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT * FROM appointments WHERE MONTH(Start) = MONTH(NOW()) ORDER BY appointments.Appointment_ID");
      ResultSet result = statement.executeQuery();

      while (result.next()) {
        appointments appointment =
            new appointments(result.getInt("Appointment_ID"), result.getString("Title"),
                result.getString("Description"), result.getString("Location"),
                result.getString("Type"), result.getTimestamp("Start").toLocalDateTime(),
                result.getTimestamp("End").toLocalDateTime(), result.getInt("Customer_ID"),
                result.getInt("User_ID"), result.getInt("Contact_ID"));
        appointmentsByMonth.add(appointment);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return appointmentsByMonth;
  }

  /**
   * Retrieves a list of appointment counts for each month from the database.
   *
   * @return An observable list of appointment counts for each month.
   */
  public ObservableList<appointments> getAppointmentTotalByMonth() {
    JDBC.openConnection();
    ObservableList<appointments> appointmentsByMonth = FXCollections.observableArrayList();

    try {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT DISTINCT(MONTHNAME(Start)) AS Month, Count(*) AS NUM FROM appointments GROUP BY Month");
      ResultSet result = statement.executeQuery();

      while (result.next()) {
        appointments appointment =
            new appointments(result.getString("Month"), result.getInt("NUM"));
        appointmentsByMonth.add(appointment);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return appointmentsByMonth;
  }

  /**
   * Retrieves a list of appointments scheduled for the current week from the database.
   *
   * @return An observable list of appointments scheduled for the current week.
   */
  public ObservableList<appointments> getAppointmentsByWeek() {
    ObservableList<appointments> appointmentsByWeek = FXCollections.observableArrayList();

    try {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT * FROM appointments AS a INNER JOIN contacts AS c ON a.Contact_ID=c.Contact_ID WHERE Start >= ? AND Start < ?;");
      statement.setDate(1, java.sql.Date.valueOf(startOfWeek));
      statement.setDate(2, java.sql.Date.valueOf(oneWeek));
      ResultSet result = statement.executeQuery();

      while (result.next()) {
        appointments appointment =
            new appointments(result.getInt("Appointment_ID"), result.getString("Title"),
                result.getString("Description"), result.getString("Location"),
                result.getString("Type"), result.getTimestamp("Start").toLocalDateTime(),
                result.getTimestamp("End").toLocalDateTime(), result.getInt("Customer_ID"),
                result.getInt("User_ID"), result.getInt("Contact_ID"));
        appointmentsByWeek.add(appointment);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return appointmentsByWeek;
  }
}

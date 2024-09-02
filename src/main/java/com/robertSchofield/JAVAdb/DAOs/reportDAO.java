package com.robertSchofield.JAVAdb.DAOs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static com.robertSchofield.JAVAdb.helper.JDBC.connection;

/**
 * This class contains methods for generating reports and schedules.
 *
 * @Author Robert Schofield
 */
public class reportDAO {

  /**
   * Retrieves a report of appointment types and their monthly totals.
   *
   * @return A string representation of the report. Each line contains the appointment type, month, and total count.
   */
  public String getAllAppointments() {
    StringBuilder report = new StringBuilder();
    try {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT Type, MONTH(Start) AS Month, COUNT(*) AS Total "
              + "FROM appointments "
              + "GROUP BY Type, MONTH(Start) "
              + "ORDER BY Month;");
      ResultSet result = statement.executeQuery();

      while (result.next()) {
        String type = result.getString("Type");
        int month = result.getInt("Month");
        int total = result.getInt("Total");

        report.append("Type: ")
            .append(type)
            .append(", Month: ")
            .append(month)
            .append(", Total: ")
            .append(total)
            .append("\n");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return report.toString();
  }

  /**
   * A nested class for generating a schedule for a specific contact.
   */
  public class contactSchedule {

    /**
     * Retrieves a schedule for a contact based on their contact ID.
     *
     * @param contactId The ID of the contact.
     * @return A list of strings representing the schedule. Each string contains appointment details.
     */
    public List<String> generateSchedule(int contactId) {
      List<String> schedule = new ArrayList<>();
      try {
        PreparedStatement statement = connection.prepareStatement(
            "SELECT Appointment_ID, Title, Type, Description, Start, End, Customer_ID "
                + "FROM appointments "
                + "WHERE Customer_ID = ? "
                + "ORDER BY Start;");
        statement.setInt(1, contactId);
        ResultSet result = statement.executeQuery();

        while (result.next()) {
          int appointmentId = result.getInt("Appointment_ID");
          String title = result.getString("Title");
          String type = result.getString("Type");
          String description = result.getString("Description");
          String startDate = result.getTimestamp("Start").toString();
          String endDate = result.getTimestamp("End").toString();
          int customerId = result.getInt("Customer_ID");

          schedule.add("Appointment ID: " + appointmentId);
          schedule.add("Title: " + title);
          schedule.add("Type: " + type);
          schedule.add("Description: " + description);
          schedule.add("Start Date and Time: " + startDate);
          schedule.add("End Date and Time: " + endDate);
          schedule.add("Customer ID: " + customerId);
          schedule.add("----------------------------------");
        }
      } catch (Exception e) {
        e.printStackTrace();
      }

      return schedule;
    }
  }
}

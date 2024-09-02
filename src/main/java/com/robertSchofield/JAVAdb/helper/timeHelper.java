package com.robertSchofield.JAVAdb.helper;

import com.robertSchofield.JAVAdb.DAOs.appointmentDAO;
import com.robertSchofield.JAVAdb.model.appointments;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import javafx.collections.ObservableList;

/**
 * A utility class for time-related operations.
 *
 * @Author Robert Schofield
 */
public class timeHelper {

  /**
   * Converts a given LocalDateTime to Eastern Time (America/New_York).
   *
   * @param toConvert The LocalDateTime to convert.
   * @return The converted ZonedDateTime in Eastern Time.
   */
  public static ZonedDateTime toEasternTime(LocalDateTime toConvert) {
    return ZonedDateTime.of(toConvert, ZoneId.of("America/New_York"));
  }

  /**
   * Checks if a new appointment time overlaps with existing appointments for a specific customer.
   *
   * @param appointmentId The ID of the appointment to be checked.
   * @param customerId    The ID of the customer for whom the appointment is being scheduled.
   * @param startTime     The start time of the new appointment.
   * @param endTime       The end time of the new appointment.
   * @return True if the new appointment time does not overlap with existing appointments for the customer;         False otherwise.
   */
  public static boolean overlapCheck(int appointmentId, int customerId, LocalDateTime startTime,
      LocalDateTime endTime) {
    appointmentDAO appointmentDAO = new appointmentDAO();
    ObservableList<appointments> allAppointments = appointmentDAO.listByCustomerId(customerId);

    for (appointments appointment : allAppointments) {
      if (appointment.getAppointmentID() != appointmentId
          && appointment.getCustomerID() == customerId) {
        LocalDateTime appointmentStart = appointment.getStart();
        LocalDateTime appointmentEnd = appointment.getEnd();

        // Check if the new appointment time overlaps with the existing appointment time
        if (startTime.isEqual(appointmentStart) && endTime.isEqual(appointmentEnd) ||
            startTime.isEqual(appointmentStart) && endTime.isAfter(appointmentEnd) ||
            startTime.isAfter(appointmentStart) && startTime.isBefore(appointmentEnd) ||
            endTime.isAfter(appointmentStart) && endTime.isBefore(appointmentEnd) ||
            startTime.isBefore(appointmentStart) && endTime.isAfter(appointmentEnd)) {
          return false; // Overlapping appointment found
        }
      }
    }

    return true; // No overlapping appointment found
  }
}


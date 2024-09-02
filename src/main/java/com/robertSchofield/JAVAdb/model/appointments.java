package com.robertSchofield.JAVAdb.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The type Appointments.
 *
 * @Author Robert Schofield
 */
public class appointments {

  private int appointmentID;
  private String title;
  private String description;
  private String location;
  private String type;
  private LocalDateTime start;
  private LocalDateTime end;
  private int customerID;
  private int userID;
  private int contactID;
  private int total;

  /**
   * Instantiates a new Appointments.
   *
   * @param appointmentID the appointment id
   * @param title         the title
   * @param description   the description
   * @param location      the location
   * @param type          the type
   * @param start         the start
   * @param end           the end
   * @param customerID    the customer id
   * @param userID        the user id
   * @param contactID     the contact id
   */
  public appointments(
      int appointmentID,
      String title,
      String description,
      String location,
      String type,
      LocalDateTime start,
      LocalDateTime end,

      int customerID,
      int userID,
      int contactID
  ) {
    this.appointmentID = appointmentID;
    this.title = title;
    this.description = description;
    this.location = location;
    this.type = type;
    this.start = start;
    this.end = end;
    this.customerID = customerID;
    this.userID = userID;
    this.contactID = contactID;
  }

  /**
   * Instantiates a new Appointments.
   *
   * @param type  the type
   * @param total the total
   */
  public appointments(String type, int total) {
    this.type = type;
    this.total = total;
  }

  /**
   * Instantiates a new Appointments.
   */
  public appointments() {
  }

  /**
   * Instantiates a new Appointments.
   *
   * @param title       the title
   * @param description the description
   * @param location    the location
   * @param type        the type
   * @param start       the start
   * @param end         the end
   * @param customerID  the customer id
   * @param userID      the user id
   * @param contactID   the contact id
   */
  public appointments(
      String title,
      String description,
      String location,
      String type,
      LocalDateTime start,
      LocalDateTime end,
      int customerID,
      int userID,
      int contactID
  ) {
    this.title = title;
    this.description = description;
    this.location = location;
    this.type = type;
    this.start = start;
    this.end = end;
    this.customerID = customerID;
    this.userID = userID;
    this.contactID = contactID;
  }

  /**
   * Gets appointment id.
   *
   * @return the appointment id
   */
  public int getAppointmentID() {
    return appointmentID;
  }

  /**
   * Sets appointment id.
   *
   * @param appointmentID the appointment id
   */
  public void setAppointmentID(int appointmentID) {
    this.appointmentID = appointmentID;
  }

  /**
   * Gets title.
   *
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * Sets title.
   *
   * @param title the title
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Gets description.
   *
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets description.
   *
   * @param description the description
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Gets location.
   *
   * @return the location
   */
  public String getLocation() {
    return location;
  }

  /**
   * Sets location.
   *
   * @param location the location
   */
  public void setLocation(String location) {
    this.location = location;
  }

  /**
   * Gets type.
   *
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * Sets type.
   *
   * @param type the type
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Gets start.
   *
   * @return the start
   */
  public LocalDateTime getStart() {
    return start;
  }

  /**
   * Sets start.
   *
   * @param start the start
   */
  public void setStart(LocalDateTime start) {
    this.start = start;
  }

  /**
   * Gets end.
   *
   * @return the end
   */
  public LocalDateTime getEnd() {
    return end;
  }

  /**
   * Sets end.
   *
   * @param end the end
   */
  public void setEnd(LocalDateTime end) {
    this.end = end;
  }

  /**
   * Gets customer id.
   *
   * @return the customer id
   */
  public int getCustomerID() {
    return customerID;
  }

  /**
   * Sets customer id.
   *
   * @param customerID the customer id
   */
  public void setCustomerID(int customerID) {
    this.customerID = customerID;
  }

  /**
   * Gets user id.
   *
   * @return the user id
   */
  public int getUserID() {
    return userID;
  }

  /**
   * Sets user id.
   *
   * @param userID the user id
   */
  public void setUserID(int userID) {
    this.userID = userID;
  }

  /**
   * Gets contact id.
   *
   * @return the contact id
   */
  public int getContactID() {
    return contactID;
  }

  /**
   * Sets contact id.
   *
   * @param contactID the contact id
   */
  public void setContactID(int contactID) {
    this.contactID = contactID;
  }

  /**
   * Gets total.
   *
   * @return the total
   */
  public int getTotal() {
    return total;
  }

  /**
   * To date string.
   *
   * @return the string
   */
  public String toDate() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String formattedDateTime = start.format(formatter);
    return formattedDateTime;
  }
}

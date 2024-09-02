package com.robertSchofield.JAVAdb.model;

/**
 * The type Contact.
 *
 * @Author Robert Schofield
 */
public class contact {

  private int contactId;
  private String contactName;
  private String email;

  /**
   * Instantiates a new Contact.
   */
  // Default constructor
  public contact() {
  }

  /**
   * Instantiates a new Contact.
   *
   * @param contactId   the contact id
   * @param contactName the contact name
   * @param email       the email
   */
  // Parameterized constructor
  public contact(int contactId, String contactName, String email) {
    this.contactId = contactId;
    this.contactName = contactName;
    this.email = email;
  }

  /**
   * Instantiates a new Contact.
   *
   * @param contactID the contact id
   */
  public contact(int contactID) {
    this.contactId = contactId;
  }

  /**
   * Gets contact id.
   *
   * @return the contact id
   */
  // Getters and Setters
  public int getContactId() {
    return contactId;
  }

  /**
   * Sets contact id.
   *
   * @param contactId the contact id
   */
  public void setContactId(int contactId) {
    this.contactId = contactId;
  }

  /**
   * Gets contact name.
   *
   * @return the contact name
   */
  public String getContactName() {
    return contactName;
  }

  /**
   * Sets contact name.
   *
   * @param contactName the contact name
   */
  public void setContactName(String contactName) {
    this.contactName = contactName;
  }

  /**
   * Gets email.
   *
   * @return the email
   */
  public String getEmail() {
    return email;
  }

  /**
   * Sets email.
   *
   * @param email the email
   */
  public void setEmail(String email) {
    this.email = email;
  }

  // Optionally, override toString() for better readability
  @Override public String toString() {
    return contactName;
  }
}

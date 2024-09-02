package com.robertSchofield.JAVAdb.model;

import java.time.LocalDateTime;

/**
 * The type Customers.
 *
 * @Author Robert Schofield
 */
public class customers {

  private String divName;
  private int totalCustomers;
  private int customerID;
  private String customerName;
  private String address;
  private String postalCode;
  private String phoneNumber;
  private LocalDateTime createdDate;
  private String createdBy;
  private LocalDateTime lastUpdate;
  private String lastUpdatedBy;
  private int divID;
  private int countryID;
  private String countryName;

  /**
   * Instantiates a new Customers.
   *
   * @param customerID    the customer id
   * @param customerName  the customer name
   * @param address       the address
   * @param postalCode    the postal code
   * @param phoneNumber   the phone number
   * @param createdDate   the created date
   * @param createdBy     the created by
   * @param lastUpdate    the last update
   * @param lastUpdatedBy the last updated by
   * @param divID         the div id
   * @param countryID     the country id
   * @param countryName   the country name
   */
  public customers(
      int customerID,
      String customerName,
      String address,
      String postalCode,
      String phoneNumber,
      LocalDateTime createdDate,
      String createdBy,
      LocalDateTime lastUpdate,
      String lastUpdatedBy,
      int divID,
      int countryID,
      String countryName
  ) {
    this.customerID = customerID;
    this.customerName = customerName;
    this.address = address;
    this.postalCode = postalCode;
    this.phoneNumber = phoneNumber;
    this.createdDate = createdDate;
    this.createdBy = createdBy;
    this.lastUpdate = lastUpdate;
    this.lastUpdatedBy = lastUpdatedBy;
    this.divID = divID;
    this.countryID = countryID;
    this.countryName = countryName;
    this.divName = "";
    this.totalCustomers = 0;
  }

  public customers(String divName, int totalCustomers) {
    this.divName = divName;
    this.totalCustomers = totalCustomers;
  }

  public customers(int customerID, String customerName, String customerAddress,
      String customerPostal,
      String customerPhone, int divID) {
    this.customerID = customerID;
    this.customerName = customerName;
    this.address = customerAddress;
    this.postalCode = customerPostal;
    this.phoneNumber = customerPhone;
    this.divID = divID;
  }

  /**
   * Instantiates a new Customers.
   *
   * @param customerID the customer id
   */
  public customers(int customerID) {
    this.customerID = customerID;
  }

  /**
   * Instantiates a new Customers.
   *
   * @param customerName the customer name
   * @param address      the address
   * @param postalCode   the postal code
   * @param phone        the phone
   * @param divisionId   the division id
   */
  public customers(String customerName, String address, String postalCode, String phone,
      int divisionId) {
    this.customerName = customerName;
    this.address = address;
    this.postalCode = postalCode;
    this.phoneNumber = phone;
    this.divID = divisionId;
  }

 /* public customers(int customerId, String customerName, String address, String postalCode, String phone, LocalDateTime createdDate, String s, LocalDateTime lastUpdate, String s1,
      int divisionId) {
    this.customerID = customerId;
    this.customerName = customerName;
    this.address = address;
    this.postalCode = postalCode;
    this.phoneNumber = phone;
    this.createdDate = createdDate;
    this.lastUpdate = lastUpdate;
    this.lastUpdatedBy = s1;
    this.divID = divisionId;
  }
*/

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
   * Gets customer name.
   *
   * @return the customer name
   */
  public String getCustomerName() {
    return customerName;
  }

  /**
   * Sets customer name.
   *
   * @param customerName the customer name
   */
  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  /**
   * Gets address.
   *
   * @return the address
   */
  public String getAddress() {
    return address;
  }

  /**
   * Sets address.
   *
   * @param address the address
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * Gets postal code.
   *
   * @return the postal code
   */
  public String getPostalCode() {
    return postalCode;
  }

  /**
   * Sets postal code.
   *
   * @param postalCode the postal code
   */
  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  /**
   * Gets phone number.
   *
   * @return the phone number
   */
  public String getPhoneNumber() {
    return phoneNumber;
  }

  /**
   * Sets phone number.
   *
   * @param phoneNumber the phone number
   */
  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  /**
   * Gets created date.
   *
   * @return the created date
   */
  public LocalDateTime getCreatedDate() {
    return createdDate;
  }

  /**
   * Sets created date.
   *
   * @param createdDate the created date
   */
  public void setCreatedDate(LocalDateTime createdDate) {
    this.createdDate = createdDate;
  }

  /**
   * Gets created by.
   *
   * @return the created by
   */
  public String getCreatedBy() {
    return createdBy;
  }

  /**
   * Sets created by.
   *
   * @param createdBy the created by
   */
  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  /**
   * Gets last update.
   *
   * @return the last update
   */
  public LocalDateTime getLastUpdate() {
    return lastUpdate;
  }

  /**
   * Sets last update.
   *
   * @param lastUpdate the last update
   */
  public void setLastUpdate(LocalDateTime lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  /**
   * Gets last updated by.
   *
   * @return the last updated by
   */
  public String getLastUpdatedBy() {
    return lastUpdatedBy;
  }

  /**
   * Sets last updated by.
   *
   * @param lastUpdatedBy the last updated by
   */
  public void setLastUpdatedBy(String lastUpdatedBy) {
    this.lastUpdatedBy = lastUpdatedBy;
  }

  /**
   * Gets div id.
   *
   * @return the div id
   */
  public int getDivID() {
    return divID;
  }

  /**
   * Sets div id.
   *
   * @param divID the div id
   */
  public void setDivID(int divID) {
    this.divID = divID;
  }

  /**
   * Gets country id.
   *
   * @return the country id
   */
  public int getCountryID() {
    return countryID;
  }

  /**
   * Sets country id.
   *
   * @param countryID the country id
   */
  public void setCountryID(int countryID) {
    this.countryID = countryID;
  }

  /**
   * Gets country name.
   *
   * @return the country name
   */
  public String getCountryName() {
    return countryName;
  }

  /**
   * Sets country name.
   *
   * @param countryName the country name
   */
  public void setCountryName(String countryName) {
    this.countryName = countryName;
  }

  /**
   * Gets div name.
   *
   * @return the div name
   */
  public String getDivName() {
    return divName;
  }

  /**
   * Sets div name.
   *
   * @param divName the div name
   */
  public void setDivName(String divName) {
    this.divName = divName;
  }

  /**
   * Gets total customers.
   *
   * @return the total customers
   */
  public int getTotalCustomers() {
    return totalCustomers;
  }

  /**
   * Sets total customers.
   *
   * @param totalCustomers the total customers
   */
  public void setTotalCustomers(int totalCustomers) {
    this.totalCustomers = totalCustomers;
  }

  @Override
  public String toString() {
    return customerName;
  }
}
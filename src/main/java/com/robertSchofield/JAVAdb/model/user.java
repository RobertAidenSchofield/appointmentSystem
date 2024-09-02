package com.robertSchofield.JAVAdb.model;

import java.time.LocalDateTime;

/**
 * The type User.
 *
 * @Author Robert Schofield
 */
public class user {

  private int userID;
  private String userName;
  private String password;
  private LocalDateTime lastUpdate;
  private String lastUpdatedBy;

  /**
   * Instantiates a new User.
   *
   * @param userID        the user id
   * @param userName      the user name
   * @param password      the password
   * @param lastUpdate    the last update
   * @param lastUpdatedBy the last updated by
   */
  public user(int userID, String userName, String password, LocalDateTime lastUpdate,
      String lastUpdatedBy) {
    this.userID = userID;
    this.userName = userName;
    this.password = password;
    this.lastUpdate = lastUpdate;
    this.lastUpdatedBy = lastUpdatedBy;
  }

  /**
   * Instantiates a new User.
   *
   * @param userID   the user id
   * @param userName the user name
   * @param password the password
   */
  public user(int userID, String userName, String password) {
    this.userID = userID;
    this.userName = userName;
    this.password = password;
  }

  /**
   * Instantiates a new User.
   */
  public user() {
  }

  /**
   * Instantiates a new User.
   *
   * @param userID the user id
   */
  public user(int userID) {
    this.userID = userID;
  }

  /**
   * Instantiates a new User.
   *
   * @param username the username
   * @param password the password
   */
  public user(String username, String password) {
    this.userName = username;
    this.password = password;
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
   * Gets user name.
   *
   * @return the user name
   */
  public String getUserName() {
    return userName;
  }

  /**
   * Sets user name.
   *
   * @param userName the user name
   */
  public void setUserName(String userName) {
    this.userName = userName;
  }

  /**
   * Gets password.
   *
   * @return the password
   */
  public String getPassword() {
    return password;
  }

  /**
   * Sets password.
   *
   * @param password the password
   */
  public void setPassword(String password) {
    this.password = password;
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

  public String toString() {
    return userName;
  }
}

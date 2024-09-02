package com.robertSchofield.JAVAdb.model;

/**
 * The type First level div.
 *
 * @Author Robert Schofield
 */

public class firstLevelDiv {

  private int divisionId;
  private String division;
  private int countryId;

  /**
   * Instantiates a new First level div.
   */
  // Default constructor
  public firstLevelDiv() {
  }

  /**
   * Instantiates a new First level div.
   *
   * @param divisionId the division id
   * @param division   the division
   * @param countryId  the country id
   */
  // Parameterized constructor
  public firstLevelDiv(
      int divisionId,
      String division,
      int countryId
  ) {
    this.divisionId = divisionId;
    this.division = division;
    this.countryId = countryId;
  }

  /**
   * Instantiates a new First level div.
   *
   * @param divisionId the division id
   */
  // Constructor for finding divisions based on countryId
  public firstLevelDiv(int divisionId) {
    this.divisionId = divisionId;
  }

  /**
   * Gets division id.
   *
   * @return the division id
   */
  // Getters and Setters
  public int getDivisionId() {
    return divisionId;
  }

  /**
   * Sets division id.
   *
   * @param divisionId the division id
   */
  public void setDivisionId(int divisionId) {
    this.divisionId = divisionId;
  }

  /**
   * Gets division.
   *
   * @return the division
   */
  public String getDivision() {
    return division;
  }

  /**
   * Sets division.
   *
   * @param division the division
   */
  public void setDivision(String division) {
    this.division = division;
  }

  /**
   * Gets country id.
   *
   * @return the country id
   */
  public int getCountryId() {
    return countryId;
  }

  /**
   * Sets country id.
   *
   * @param countryId the country id
   */
  public void setCountryId(int countryId) {
    this.countryId = countryId;
  }
}

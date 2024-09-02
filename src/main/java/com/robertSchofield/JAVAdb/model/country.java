package com.robertSchofield.JAVAdb.model;

/**
 * The type Country.
 *
 * @Author Robert Schofield
 */
public class country {

  private int countryId;
  private String country;

  /**
   * Instantiates a new Country.
   */
  // Default constructor
  public country() {
  }

  /**
   * Instantiates a new Country.
   *
   * @param countryId the country id
   * @param country   the country
   */
  // Parameterized constructor
  public country(int countryId, String country) {
    this.countryId = countryId;
    this.country = country;
  }

  /**
   * Instantiates a new Country.
   *
   * @param countryId the country id
   */
  public country(int countryId) {
    this.countryId = countryId;
  }

  /**
   * Gets country id.
   *
   * @return the country id
   */
  // Getters and Setters
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

  /**
   * Gets country.
   *
   * @return the country
   */
  public String getCountry() {
    return country;
  }

  /**
   * Sets country.
   *
   * @param country the country
   */
  public void setCountry(String country) {
    this.country = country;
  }
}

module com.example.JAVAdb {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.sql;

  opens com.robertSchofield.JAVAdb to javafx.fxml;
  exports com.robertSchofield.JAVAdb;

  exports com.robertSchofield.JAVAdb.controller;
  opens com.robertSchofield.JAVAdb.controller to javafx.fxml;
  exports com.robertSchofield.JAVAdb.model;
  exports com.robertSchofield.JAVAdb.DAOs;

}
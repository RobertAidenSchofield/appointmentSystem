package com.robertSchofield.JAVAdb.helper;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The type Fxml helper.
 *
 * @Author Robert Schofield
 */
public class fxmlHelper {
  /**
   * This function is responsible for loading a new FXML scene and replacing the current one.
   * It takes an FXML file, a title for the new scene, and the current stage as parameters.
   *
   * @param fxmlFile     The path to the FXML file to be loaded.
   * @param title        The title to be set for the new stage.
   * @param currentStage The current stage that will be closed.
   * @throws Exception If there is an error loading the FXML file.
   */
  public static void loadFXMLScene(String fxmlFile, String title, Stage currentStage)
      throws Exception {
    Parent root = FXMLLoader.load(fxmlHelper.class.getResource(fxmlFile));
    Stage stage = new Stage();
    stage.setTitle(title);
    stage.setScene(new Scene(root));
    stage.show();
    currentStage.close();
  }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxwordgame;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

/**
 *
 * @author DEV-18
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private Label label;
    @FXML
    private Button subGame_Animals;
    @FXML
    private Button subGame_Space;
    @FXML
    private Button subGame_BodyParts;
    @FXML
    private Button subGame_Electronics;
    @FXML
    private Button subGame_Foods;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        
    }

    /*
    main game starting module
    */
    @FXML
    private void onSubGameStart(ActionEvent event) {
        Button eventSource = (Button) event.getSource();

        if (eventSource.equals(subGame_Animals)) { // animal button clicked
            GlobalStorage.DictionaryName = "animals.txt";
            GlobalStorage.subGameId = 0; 
        } else if (eventSource.equals(subGame_Space)) { // space button clicked
            GlobalStorage.DictionaryName = "space.txt";
            GlobalStorage.subGameId = 1;
        } else if (eventSource.equals(subGame_BodyParts)) { // body parts button clicked
            GlobalStorage.DictionaryName = "body_parts.txt";
            GlobalStorage.subGameId = 2;
        } else if (eventSource.equals(subGame_Electronics)) { // electronics button clicked
            GlobalStorage.DictionaryName = "electronics.txt";
            GlobalStorage.subGameId = 3;
        } else if (eventSource.equals(subGame_Foods)) { // foods button clicked
            GlobalStorage.DictionaryName = "foods.txt";
            GlobalStorage.subGameId = 4;
        }

        try {

            System.out.println("You clicked subGame button!");

            Pane pane = FXMLLoader.load(getClass().getResource("FXMLSubGame.fxml")); // get Sub game interface
            GlobalStorage.CurrentGameLevel=1;
            label.getScene().setRoot((Pane) pane);

        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /*
        When user click exit button, this event handler is called       
    */
    
    
    @FXML
    private void onExitGame(ActionEvent event) {
        
        TextSpeaker.releaseTalking();
        Platform.exit();
        System.out.println("You clicked Exit button!");

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}

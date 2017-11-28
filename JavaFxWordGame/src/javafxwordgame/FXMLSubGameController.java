/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxwordgame;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author DEV-18
 */
public class FXMLSubGameController implements Initializable {

    /**
     * Initializes the controller class.
     */
    Timer game_Timer;

    @FXML
    private Label label_Time;
    @FXML
    private Label label_Words;
    @FXML
    private Label label_Level;

    @FXML
    private ProgressBar progress_Main;

    @FXML
    private TextField input_Word;
    @FXML
    private Label label_Output;

    @FXML
    private Label label_Main;
    @FXML
    private Button button_Start;
    @FXML
    private Button button_Stop;
    @FXML
    private Button button_Back;
    @FXML
    private Button button_SpeakAgain;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        game_Timer = null;

    }

    /*
    *
    *   start sub game.
    */
    @FXML
    private void onStartGame(ActionEvent event) throws IOException {
        progress_Main.setVisible(true);
        label_Main.setVisible(true);
        input_Word.setVisible(true);
        button_Start.setVisible(false);
        button_Back.setVisible(false);
        button_Stop.setVisible(true);
        button_SpeakAgain.setVisible(true);
        label_Output.setVisible(true);
        button_Stop.setText("Stop Game");
        GlobalStorage.CurrentSelectedWord = "";
        label_Output.setText("");
        game_Timer = new Timer();
        game_Timer.schedule(new counting_Task(), 0, 1000); // run counting_task() function once per 1 second
        onWordInput(new ActionEvent());
        input_Word.setDisable(false);
        input_Word.requestFocus();

    }

    /*
     stop sub game
    */
    @FXML
    private void onStopGame(ActionEvent event) {
        progress_Main.setVisible(false);
        label_Main.setVisible(false);
        label_Output.setVisible(false);
        input_Word.setVisible(false);
        button_Start.setVisible(true);
        button_Back.setVisible(true);
        button_Stop.setVisible(false);
        button_SpeakAgain.setVisible(false);
        try {
            game_Timer.cancel();
            game_Timer.purge();
            game_Timer = null;
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    /*
    back to main interface.
    */
    @FXML
    private void onBackToMain(ActionEvent event) {

        try {
            
            if(game_Timer!=null){
                game_Timer.cancel();
                game_Timer.purge();
                game_Timer = null;
            }
            Pane pane = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

            label_Time.getScene().setRoot((Pane) pane);

        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /*
     repeat talk current word again
    */
    @FXML
    private void talkAgain(){    
        TextSpeaker.startTalking(GlobalStorage.CurrentSelectedWord);
        input_Word.requestFocus();
    }
    
    /*
    generate a word from dictionary and play it.
    */
    @FXML
    private void talkingWord() {
        int levelIndex = (10 * GlobalStorage.subGameId + GlobalStorage.CurrentGameLevel - 1);
        int index_start = 0;
        int index_size = GlobalStorage.wordStoredDataLevels[levelIndex];
        if (GlobalStorage.CurrentGameLevel > 2) {
            index_start = GlobalStorage.wordStoredDataLevels[levelIndex] - GlobalStorage.wordStoredDataLevels[levelIndex - 2];
            index_size -= index_start;
        }
        int index = (int) (index_start + Math.random() * index_size);
        String str = GlobalStorage.wordStoredDatas.get(index);
        GlobalStorage.CurrentSelectedWord = str;
        TextSpeaker.startTalking(str);
    }
    
    /*
    validate processing when user press <return> key.
    */
    @FXML
    private void onWordInput(ActionEvent event) throws IOException {
        if (GlobalStorage.CurrentSelectedWord.equals("")) { // if 1st started word
            GlobalStorage.initialize();
            talkingWord();
            GlobalStorage.CorrectWords = 0;
            GlobalStorage.CurrentInputedWords = 1;
            GlobalStorage.CounterTime = GlobalStorage.LimitTime;
            showHeadStatus();

            System.out.println(GlobalStorage.CurrentSelectedWord);
        } else {

            String str = input_Word.getText();
            if (str != null && str.toLowerCase().trim().equals(GlobalStorage.CurrentSelectedWord.toLowerCase().trim())) { // if word is correct
                label_Output.setText("Good, you are right.");
                System.out.println("Correct!");
                GlobalStorage.CorrectWords++;
            } else { // if word is incorrect, display message and correct word.
                label_Output.setText("No, incorrect!(" + GlobalStorage.CurrentSelectedWord + ")");
            }
            input_Word.setText("");

            GlobalStorage.CurrentInputedWords++;

            if (GlobalStorage.CurrentInputedWords <= GlobalStorage.TotalGameWords && GlobalStorage.CounterTime != 0) { // if not last word
                talkingWord();
            }
            showHeadStatus();
            System.out.println(GlobalStorage.CurrentSelectedWord);
        }
    }
    
    /*
    displaying process on top and message.
    */

    private void showHeadStatus() {
        // show top status.
        label_Time.setText("Time : " + (int) (GlobalStorage.CounterTime / 60) + ":" + (GlobalStorage.CounterTime % 60));
        label_Words.setText("Words : " + GlobalStorage.CurrentInputedWords + "/" + GlobalStorage.TotalGameWords);
        label_Level.setText("Level : " + GlobalStorage.CurrentGameLevel);

        // progress bar status
        progress_Main.setProgress(((double) (GlobalStorage.LimitTime - GlobalStorage.CounterTime) / (double) GlobalStorage.LimitTime));

        if (GlobalStorage.CurrentInputedWords > GlobalStorage.TotalGameWords || GlobalStorage.CounterTime == 0) { // if time over or word limit reached.
            if (GlobalStorage.CorrectWords >= 7) { // if game success
                label_Output.setText("Congratulations! Continue next Level.");
                GlobalStorage.CurrentGameLevel++;
                button_Stop.setText("Continue Game");
            } else { // if game failed
                label_Output.setText("Game Over!");
                button_Stop.setText("Restart Game");
            }

             // initialize configuration variables and display status.
            label_Time.setText("Time : " + (int) (GlobalStorage.CounterTime / 60) + ":" + (GlobalStorage.CounterTime % 60));
            label_Words.setText("Words : " + GlobalStorage.TotalGameWords + "/" + GlobalStorage.TotalGameWords);
            label_Level.setText("Level : " + GlobalStorage.CurrentGameLevel);

            GlobalStorage.CurrentInputedWords = 0;
            GlobalStorage.CurrentSelectedWord = "";
            GlobalStorage.CounterTime = GlobalStorage.LimitTime;
            
            input_Word.setDisable(true);
            
            game_Timer.cancel();
            game_Timer.purge();
            game_Timer = null;
        }
    }

    /*
    Timer class
    */
    class counting_Task extends TimerTask {

        @Override
        public void run() {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    showHeadStatus();
                    GlobalStorage.CounterTime--;
                }
            });

        }
    }

}

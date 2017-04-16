package src.main.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by touhoudoge on 2017/4/9.
 */
public class Timer implements Initializable {

    private Timeline timeline;
    private int time;

    @FXML private Label timeLabel;

    public Timer(){
        timeline = new Timeline();
        timeline.setCycleCount(12);
        timeline.getKeyFrames().addAll(new KeyFrame(Duration.seconds(1),(ActionEvent ae)->{
            --time;
            displayTime();
            if(time==0){
                //player lose?
            }
        }));
    }

    private void displayTime(){
        int minute = time/60;
        int second = time%60;
        timeLabel.setText(String.valueOf(minute)+" : "+String.valueOf(second));
    }

    public void start(){
        timeLabel.setText(2+" : "+0);
        time = 120;
        timeline.playFromStart();
    }

    public void stop(){
        timeline.stop();
    }

    public void pause(){
        timeline.pause();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /************* test ********************/
        timeLabel.setText(2+" : "+0);
        time = 120;
        timeline.play();
        /************* test ********************/
    }
}

package src.main.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by touhoudoge on 2017/4/7.
 */
public class ChessBox implements Initializable{

    private Timer timer;

    @FXML private Pane chessPane;

    @FXML
    private void onClick(MouseEvent event){
        /************* test ********************/
            Circle circle = new Circle();
            circle.setFill(Color.BLACK);
            int x = (int)event.getX();
            int y = (int)event.getY();
            if(x<10 || x>550 || y<10 || y>550)
                return;
            if((x-10)%30<7 && (y-10)%30<7){
                x = (x-10)/30*30+10;
                y = (y-10)/30*30+10;
            }else if((x-10)%30<7 && (y-10)%30>23){
                x = (x-10)/30*30+10;
                y = ((y-10)/30+1)*30+10;
            }else if((x-10)%30>23 && (y-10)%30<7){
                x = ((x-10)/30+1)*30+10;
                y = (y-10)/30*30+10;
            }else if((x-10)%30>23 && (y-10)%30>23){
                x = ((x-10)/30+1)*30+10;
                y = ((y-10)/30+1)*30+10;
            }else{
                return;
            }
            circle.setLayoutX(x);
            circle.setLayoutY(y);
            circle.setRadius(10);
            chessPane.getChildren().add(circle);
            timer.start();
        /************* test ********************/
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Rectangle rec = new Rectangle(0,0,560,560);
        rec.setFill(Color.rgb(249,214,91));
        chessPane.getChildren().add(rec);

        Line line;
        line = new Line(0,570,0,570);
        chessPane.getChildren().add(line);
        for(int i = 0;i<19;++i){
            line = new Line(i*30+10,10,i*30+10,550);
            line.setStroke(Color.BLACK);
            chessPane.getChildren().add(line);
        }
        for(int i = 0;i<19;++i){
            line = new Line(10,i*30+10,550,i*30+10);
            chessPane.getChildren().add(line);
        }
        int x = 3;
        int y;
        Circle circle;
        for(int i = 0;i<3;++i){
            y = 3;
            for(int j = 0;j<3;++j){
                circle = new Circle();
                circle.setFill(Color.BLACK);
                circle.setRadius(3);
                circle.setLayoutX(x*30+10);
                circle.setLayoutY(y*30+10);
                chessPane.getChildren().add(circle);
                y = y + 6;
            }
            x = x + 6;
        }
    }

    public void setTimer(Timer timer){
        this.timer = timer;
    }
}

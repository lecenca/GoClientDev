package src.main;

/**
 * Created by touhoudoge on 2017/3/20.
 */

import javafx.stage.Modality;
import src.main.view.CreateRoomController;
import src.main.view.GameController;
import src.main.view.LobbyController;
import src.main.view.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.stage.Stage;
import src.main.view.SignUpController;

import java.io.IOException;
import java.io.InputStream;

public class Client extends Application
{
    private Stage primaryStage;
    private Stage createRoomStage;
    private Stage gameStage;
    private UserInfo account;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        primaryStage.setTitle("MicroOnlineGo");
        gotoLogin();
    }

    public void gotoLogin() throws Exception{
        LoginController loginController = (LoginController) replaceSceneContent("view/Login.fxml");
        loginController.setClient(this);
    }

    public void gotoLobby() throws Exception{
        LobbyController lobbyController = (LobbyController) replaceSceneContent("view/Lobby.fxml");
        lobbyController.setClient(this);
    }

    public void gotoSignUp() throws Exception{
        SignUpController signUpController = (SignUpController) replaceSceneContent("view/SignUp.fxml");
        signUpController.setClient(this);
    }

    public void backToLobby(){
        createRoomStage.close();
    }

    public void gotoCreateRoom() throws IOException {
        createRoomStage = new Stage();
        createRoomStage.initModality(Modality.APPLICATION_MODAL);
        createRoomStage.initOwner(primaryStage);
        FXMLLoader loader = new FXMLLoader();
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(getClass().getResource("view/CreateRoom.fxml"));
        InputStream in = getClass().getResourceAsStream("view/CreateRoom.fxml");
        createRoomStage.setScene(new Scene(loader.load(in)));
        createRoomStage.show();
        CreateRoomController createRoomController = (CreateRoomController) loader.getController();
        createRoomController.setClient(this);
    }

    public void gotoGame() throws Exception{
        gameStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(getClass().getResource("view/Game.fxml"));
        InputStream in = getClass().getResourceAsStream("view/Game.fxml");
        gameStage.setScene(new Scene(loader.load(in)));
        gameStage.show();
        GameController gameController = (GameController) loader.getController();
        gameController.setClient(this);
    }

    private Initializable replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(getClass().getResource(fxml));
        InputStream in = getClass().getResourceAsStream(fxml);
        primaryStage.close();
        primaryStage = new Stage();
        primaryStage.setScene(new Scene(loader.load(in)));
        primaryStage.show();
        return (Initializable) loader.getController();
    }

    public void setAccount(UserInfo account){
        this.account = account;
    }

    public UserInfo getAccount(){
        return account;
    }

    public static void main(String[] args)
    {
        launch(args);
    }


}

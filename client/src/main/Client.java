package src.main;

import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import src.main.communication.Connect;
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
import src.main.view.SignupController;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Client extends Application {

    private Stage primaryStage;
    private Stage createRoomStage;
    private Stage gameStage;
    private Stage lobbyStage = null;
    private Stage signupStage = null;
    private User account;
    private Connect connect;
    private ArrayList playerList = new ArrayList();
    private LobbyController lobbyController;

    public Client() {
        /********* 这是要的 ***********/
        connect = new Connect();
        /*****************************/
        signupStage = new Stage();
        lobbyStage = new Stage();
        lobbyController = (LobbyController) changeStage("view/Lobby.fxml", lobbyStage);
        lobbyController.setClient(this);
        lobbyStage.close();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("MicroOnlineGo");
        gotoLogin();
        if(connect != null){
            Thread receiveThread = getConnect().getReceiveThread();
            receiveThread.setDaemon(true);
            receiveThread.start();
        }
    }

    public void gotoLogin() throws Exception {
    	LoginController loginController = (LoginController) changeStage("view/Login.fxml", primaryStage);
        loginController.setClient(this);
        loginController.resetAccount();
        loginController.resetPassword();
    }

    public void gotoLobby() throws Exception {
        lobbyStage.show();
        Thread listenPlayerList = lobbyController.getListenPlayerList();
        listenPlayerList.setDaemon(true);
        listenPlayerList.start();
        Thread listenRoomList = lobbyController.getListenRoomList();
        listenRoomList.setDaemon(true);
        listenRoomList.start();
        lobbyController.fetchLobbyInfo();
    }

    public void gotoSignup() throws Exception {
        SignupController signupController = (SignupController) changeStage("view/Signup.fxml", signupStage);
        signupController.setClient(this);
        //getConnect().getReceiveThread().start();
    }

    public void backToLobby() {
        createRoomStage.close();
    }

    public void gotoCreateRoom(TableView<Room> roomList) throws IOException {
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
        createRoomController.setRoomList(roomList);
    }

    public void gotoGame() throws Exception {
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

    private Initializable changeStage(String fxml, Stage stage) {
        //create a new stage
        /*if(stage == null)
    		stage = new Stage();*/
        //create the fxml loader
        FXMLLoader loader = new FXMLLoader();
        //set the location of the fxml
        loader.setLocation(getClass().getResource(fxml));
        //get the pane
        try {
            Pane pane = loader.load();
            //create a new scene with the pane
            Scene scene = new Scene(pane);
            //set the scene
            stage.setScene(scene);
            //show the stage
            stage.show();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            return (Initializable) loader.getController();
        }
    }
    //get the connection

    public Connect getConnect() {
        return connect;
    }

    public void resetConnect(){
        connect = new Connect();
    }

    public void setAccount(User account) {
        this.account = account;
    }

    public User getAccount() {
        return account;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public Stage getCreateRoomStage() {
        return createRoomStage;
    }

    public Stage getGameStage() {
        return gameStage;
    }

    public Stage getLobbyStage() {
        return lobbyStage;
    }

    public Stage getsignupStage() {
        return signupStage;
    }

    public ArrayList getPlayerList() {
        return playerList;
    }

    public static void main(String[] args) {
        launch(args);
    }

}

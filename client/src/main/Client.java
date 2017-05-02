package src.main;

import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.WindowEvent;
import src.main.communication.Connect;
import src.main.view.CreateRoomController;
import src.main.view.GameController;
import src.main.view.LobbyController;
import src.main.view.LoginController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.stage.Stage;
import src.main.view.SignupController;
import src.util.MessageQueue;
import src.util.UserComparator;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Client extends Application {
	private UserComparator comparator = new UserComparator();
	private Stage primaryStage;
	private Stage createRoomStage;
	private Stage gameStage;
	private Stage lobbyStage = null;
	private Stage signupStage = null;
	private User user;
	private Connect connect;
	private ArrayList playerList = new ArrayList();
	private LobbyController lobbyController;
	private static GameController gameController;
	private ObservableList<Room> roomData = FXCollections.observableArrayList();
	private ObservableList<User> playerData = FXCollections.observableArrayList();
	private ObservableList<String> messageData = FXCollections.observableArrayList();
	public static MessageQueue<Room> rooms = new MessageQueue<>();
	public static MessageQueue<User> players = new MessageQueue<>();
	public static MessageQueue<String> chatMessages = new MessageQueue<>();
	private Thread listenPlayerList = new Thread(new Runnable() {

		@Override
		public void run() {

			System.out.println("监听玩家列表线程启动！");
			while (true) {

				if (!players.isEmpty()) {
					playerData.add(players.remove());
				}

				/*
				 * try { Thread.currentThread().sleep(1000); playerData.add(new
				 * User("tom", 10, 100, 60, 1)); playerData.sorted(comparator);
				 * } catch (InterruptedException e) { // TODO Auto-generated
				 * catch block e.printStackTrace(); }
				 */
			}

		}
	});
	private Thread listenRoomList = new Thread(new Runnable() {

		@Override
		public void run() {
			System.out.println("监听房间列表线程启动！");
			// TODO Auto-generated method stub
			while (true) {

				if (!rooms.isEmpty()) {
					roomData.add(rooms.remove());
				}

				/*
				 * roomData.add(new Room(2, "room..", "player1", "player2", 1));
				 * try { Thread.currentThread().sleep(2000); } catch
				 * (InterruptedException e) { // TODO Auto-generated catch block
				 * e.printStackTrace(); }
				 */
			}

		}
	});

	private Thread chatThread = new Thread(new Runnable() {

		@Override
		public void run() {
			System.out.println("聊天线程启动！");
			while (true) {
				if (!chatMessages.isEmpty())
					try {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if (!chatMessages.isEmpty())
									messageData.add(chatMessages.remove());
							}
						});
						
					} catch (Exception e) {

					}
				/*
				 * messageData.add("hello"); try {
				 * Thread.currentThread().sleep(2000); } catch
				 * (InterruptedException e) { // TODO Auto-generated catch block
				 * e.printStackTrace(); }
				 */
			}

		}

		/*
		 * @Override public void run() { System.out.println("聊天线程启动"); // TODO
		 * Auto-generated method stub while (true) {
		 * 
		 * if(!chatMessage.isEmpty()) {
		 * chatBoxController.sendMessage(chatMessage.remove()); }
		 * 
		 * // chatBoxController.sendMessage("hello"); if
		 * (!chatMessages.isEmpty()) messageData.add(chatMessages.remove()); try
		 * { messageData.add("hello"); Thread.currentThread().sleep(2000); }
		 * catch (InterruptedException e) { catch block e.printStackTrace(); } }
		 * }
		 */ });

	public Client() throws IOException {

		/*playerData.add(new User("zhangsan", 8, 30, 10, 1));
		playerData.add(new User("wangwu", 18, 20, 20, 1));
		playerData.add(new User("lisi", 19, 60, 10, 1));
		playerData.add(new User("zhangsan", 8, 40, 40, 1));
		playerData.add(new User("wangwu", 18, 30, 30, 1));
		playerData.add(new User("lisi", 19, 60, 60, 1));*/

		playerData.sort(comparator);

		/*roomData.add(new Room(1, "room1", "player1", "player2", 1));
		roomData.add(new Room(2, "room2", "player1", "player2", 1));
		roomData.add(new Room(3, "room3", "player1", "player2", 1));
		roomData.add(new Room(4, "room4", "player1", "player2", 1));
		roomData.add(new Room(5, "room5", "player1", "player2", 1));
		roomData.add(new Room(6, "room6", "player1", "player2", 1));*/

		/********* 这是要的 ***********/
		connect = new Connect();
		/*****************************/
		signupStage = new Stage();
		lobbyStage = new Stage();
		gameStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("view/Lobby.fxml"));
		Pane lobbyPane = loader.load();
		Scene lobbyScene = new Scene(lobbyPane);
		lobbyStage.setScene(lobbyScene);
		lobbyController = loader.getController();// (LobbyController)
													// changeStage("view/Lobby.fxml",
													// lobbyStage);
		lobbyController.setClient(this);
		lobbyController.setAll();
		FXMLLoader loader2 = new FXMLLoader();
		loader2.setLocation(getClass().getResource("view/Game.fxml"));
		Pane gamePane = loader2.load();
		Scene gameScene = new Scene(gamePane);
		gameStage.setScene(gameScene);
		gameController = loader2.getController();
		gameController.setClient(this);
		gameStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				gameController.clear();
				System.out.println("关闭游戏界面 clear()");
			}
		});
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		primaryStage.setTitle("MicroOnlineGo");
		gotoLogin();
		if (connect != null) {
			Thread receiveThread = getConnect().getReceiveThread();
			receiveThread.setDaemon(true);
			receiveThread.start();
		}
		listenPlayerList.setDaemon(true);
		listenPlayerList.start();
		listenRoomList.setDaemon(true);
		listenRoomList.start();
		chatThread.setDaemon(true);
		chatThread.start();
	}

	public void gotoLogin() throws Exception {
		LoginController loginController = (LoginController) changeStage("view/Login.fxml", primaryStage);
		loginController.setClient(this);
		loginController.resetAccount();
		loginController.resetPassword();
	}

	public void gotoLobby() throws Exception {
		lobbyStage.show();
		/*
		 * Thread listenPlayerList = lobbyController.getListenPlayerList();
		 * listenPlayerList.setDaemon(true); listenPlayerList.start(); Thread
		 * listenRoomList = lobbyController.getListenRoomList();
		 * listenRoomList.setDaemon(true); listenRoomList.start();
		 */
		Thread chatThread = lobbyController.getChatThread();
		/*
		 * chatThread.setDaemon(true); chatThread.start();
		 */
		lobbyController.fetchLobbyInfo();
	}

	public void gotoSignup() throws Exception {
		SignupController signupController = (SignupController) changeStage("view/Signup.fxml", signupStage);
		signupController.setClient(this);
		// getConnect().getReceiveThread().start();
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

	public void gotoGame(Room room) throws Exception {
		/*
		 * gameStage = new Stage(); FXMLLoader loader = new FXMLLoader();
		 * loader.setBuilderFactory(new JavaFXBuilderFactory());
		 * loader.setLocation(getClass().getResource("view/Game.fxml"));
		 * InputStream in = getClass().getResourceAsStream("view/Game.fxml");
		 * gameStage.setScene(new Scene(loader.load(in))); gameStage.show();
		 * GameController gameController = (GameController)
		 * loader.getController(); gameController.setClient(this);
		 */
		gameStage.show();
		gameController.setRoom(room);
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
		// create a new stage
		/*
		 * if(stage == null) stage = new Stage();
		 */
		// create the fxml loader
		FXMLLoader loader = new FXMLLoader();
		// set the location of the fxml
		loader.setLocation(getClass().getResource(fxml));
		// get the pane
		try {
			Pane pane = loader.load();
			// create a new scene with the pane
			Scene scene = new Scene(pane);
			// set the scene
			stage.setScene(scene);
			// show the stage
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			return (Initializable) loader.getController();
		}
	}
	// get the connection

	public Connect getConnect() {
		return connect;
	}

	public void resetConnect() {
		connect = new Connect();
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
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

	public ObservableList<Room> getRoomData() {
		return roomData;
	}

	public ObservableList<User> getPlayerData() {
		return playerData;
	}

	public static MessageQueue<Room> getRooms() {
		return rooms;
	}

	public static MessageQueue<User> getPlayers() {
		return players;
	}

	public ObservableList<String> getMessageData() {
		return messageData;
	}

	public static MessageQueue<String> getChatMessages() {
		return chatMessages;
	}

	public static void main(String[] args) {
		launch(args);
	}

	public static GameController getGameController() {
		return gameController;
	}

}

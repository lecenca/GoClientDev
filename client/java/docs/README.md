# File structure

```
client
    ├─lib        // Dependent package
    │      fastjson-1.2.5.jar
    │      gson-2.8.0.jar
    │      junit-4.9.jar
    │      
    ├─resources  // Resource files
    │  ├─image
    │  │      
    │  └─music
    │          
    └─src        // Source codes
        ├─main
        │  │  Board.java   // Logic board
        │  │  Client.java  // Main class
        │  │  Core.java    // Game logic code
        │  │  Room.java    // Game room
        │  │  Stone.java   // Go stone
        │  │  Type.java    // Type for all action.
        │  │  User.java    // Player's information.
        │  │  
        │  ├─communication  // Internet connection
        │  │      Connect.java        // Connect with server
        │  │      Connect.properties  // Configure for ip and port 
        │  │      Decoder.java        // Decode from Json
        │  │      Encoder.java        // Encdoe to Json
        │  │      
        │  └─view           // .fxml file and its controller
        │          ChatBox.fxml
        │          ChatBox.java               // Manage the chat message
        │          ChessBoard.fxml
        │          ChessBoard.java            // Interface for go board
        │          CreateRoom.fxml
        │          CreateRoomController.java  // Manage the create room process
        │          Game.fxml
        │          GameController.java        // Manage the game process
        │          Lobby.fxml
        │          LobbyController.java       // Manage the game lobby process
        │          Login.fxml
        │          LoginController.java       // Manage the log in process
        │          Signup.fxml
        │          SignupController.java      // Manage the sign up process
        │          Timer.fxml
        │          Timer.java                 // Manage the timer of player
        │          
        ├─test
        │      ConnectTest.java
        │      DecoderTest.java
        │      EncoderTest.java
        │      
        └─util
                MessageQueue.java    // Message queue for player message, room message and chat message
                UserComparator.java  // Player sorted comparators

```
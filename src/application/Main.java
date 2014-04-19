package application;
	
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

class Task {
	
	int id;
	String content;
	boolean done;
	
	public Task(int id, String content, boolean done) {
		this.id = id;
		this.content = content;
		this.done = done;
	}
	
}

public class Main extends Application {
	
	@Override
	public void start(final Stage primaryStage) {
		
		primaryStage.setTitle("TODO");
		primaryStage.setResizable(false);
		
		/* The first scene where user sets up file path */
		BorderPane root1 = new BorderPane();
		
		HBox hbox1 = new HBox(10);
		hbox1.setPadding(new Insets(10));
		
		final TextField textFilePath = new TextField();
		textFilePath.setText("default.txt");
		hbox1.getChildren().add(textFilePath);		
		/* Make the TextField auto-resize to fill all the row */
		HBox.setHgrow(textFilePath, Priority.ALWAYS);
		
		final Button buttonSetUp = new Button("Set");
		hbox1.getChildren().add(buttonSetUp);
		
		buttonSetUp.setOnMouseClicked(new EventHandler<MouseEvent>() {
	        public void handle(MouseEvent me) {
	        	/* When button is pressed, save TextField contents as filePath */
	        	String filePath = textFilePath.getCharacters().toString();
	        	System.out.println(filePath);
	    }
	    });
		
		root1.setTop(hbox1);
		
		Scene scene1 = new Scene(root1, 300, 400, Color.WHITE);
		
		primaryStage.setScene(scene1);
		primaryStage.show();		
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

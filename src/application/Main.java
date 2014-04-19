package application;
	
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Main extends Application {
	
	@Override
	public void start(final Stage primaryStage) {
		
		primaryStage.setTitle("TODO");
		primaryStage.setResizable(false);
		
		/* The first scene where user sets up file path */
		final BorderPane root1 = new BorderPane();
		
		HBox hbox1 = new HBox(10);
		hbox1.setPadding(new Insets(10));
		
		final TextField textFilePath = new TextField();
		textFilePath.setText("default.txt");
		hbox1.getChildren().add(textFilePath);		
		/* Make the TextField auto-resize to fill all the row */
		HBox.setHgrow(textFilePath, Priority.ALWAYS);
		
		final Button buttonSetUp = new Button("Set");
		hbox1.getChildren().add(buttonSetUp);
		
		/* A lot of stuff happens when the button is pressed */
		buttonSetUp.setOnMouseClicked(new EventHandler<MouseEvent>() {
	        
			public void handle(MouseEvent me) {
	        	
	        	/* Saves TextField contents as filePath */
	        	String filePath = textFilePath.getCharacters().toString();
	        	
	        	java.io.File file = new File(filePath);		
	    		
	        	/* If file exists, it reads the file contents */
	        	if (file.exists()) {
	        		
	        		/* Tasks are represented as Text */
	        		final ObservableList<Text> tasks = FXCollections.observableArrayList();
	        		
	        		try {
	        			
						java.util.Scanner scanner1 = new java.util.Scanner(file);
						
						while (scanner1.hasNextLine()) {
						
							String taskLine = scanner1.nextLine();
							
							boolean task_done = Boolean.valueOf(taskLine.split(" ")[0]);
							Text task_content = new Text(taskLine.substring(taskLine.split(" ")[0].length()+1));
							
							if (task_done) {
								task_content.setStrikethrough(true);
							}
							
							tasks.add(task_content);
							
						}
						
						scanner1.close();
						
						final ListView<Text> tasks2 = new ListView<Text>();
						tasks2.setItems(tasks);
						
//						tasks2.getSelectionModel().selectedItemProperty().addListener(
//						        new ChangeListener<Text>() {
//						        public void changed(ObservableValue<? extends Text> ov, Text old_val, Text new_val) {
//						            if (!tasks2.getSelectionModel().getSelectedItem().isStrikethrough()) {
//						            	tasks2.getSelectionModel().getSelectedItem().setStrikethrough(true);
//						            } else {
//						            	tasks2.getSelectionModel().getSelectedItem().setStrikethrough(false);
//						            }
//						        }
//						    }
//						);						
												
						HBox hbox2 = new HBox(10);
						hbox2.setPadding(new Insets(10));
						
						final TextField textAddTask = new TextField();
						hbox2.getChildren().add(textAddTask);		
						/* Make the TextField auto-resize to fill all the row */
						HBox.setHgrow(textAddTask, Priority.ALWAYS);
						
						final Button buttonAddTask = new Button("Add");
						hbox2.getChildren().add(buttonAddTask);
						
						buttonAddTask.setOnMouseClicked(new EventHandler<MouseEvent>() {
					        
							public void handle(MouseEvent me) {
								
								String newTask = textAddTask.getCharacters().toString();
								tasks.add(new Text(newTask));
								
							}
							
						});
						
						final Button buttonDone = new Button("Done");
						hbox2.getChildren().add(buttonDone);
						
						buttonDone.setOnMouseClicked(new EventHandler<MouseEvent>() {
					        
							public void handle(MouseEvent me) {
								
								if (tasks2.getSelectionModel().getSelectedItem().isStrikethrough()) {
					            	tasks2.getSelectionModel().getSelectedItem().setStrikethrough(false);
					            } else {
					            	tasks2.getSelectionModel().getSelectedItem().setStrikethrough(true);
					            }
								
							}
							
						});
						
						final BorderPane root2 = new BorderPane();
						root2.setCenter(tasks2);
						root2.setBottom(hbox2);
						Scene scene2 = new Scene(root2, 310, 410, Color.WHITE);
						primaryStage.setScene(scene2);
						
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
	        		
	    		}
	        	
	        	/* If the file does not exist, it creates a new file */
	        	else {			
	    			try {
						file.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}			
	    		}
	    }
	    });
		
		HBox hbox4 = new HBox(10);
		hbox4.setPadding(new Insets(10));
		Label labelSetUp = new Label("Set the to-do list source file.");
		hbox4.getChildren().add(labelSetUp);
		root1.setTop(hbox4);
		root1.setBottom(hbox1);
		Scene scene1 = new Scene(root1, 300, 400, Color.WHITE);		
		primaryStage.setScene(scene1);
		primaryStage.show();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

package application;
	
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Main extends Application {
	
	String filePath = "default.txt";
	ObservableList<Text> tasks = FXCollections.observableArrayList();
	
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
	        	
	        	/* Get user input and set up a file */
				
	        	filePath = textFilePath.getCharacters().toString();
	        	
	        	java.io.File file = new File(filePath);		
	    		
	        	/* If the file does not exist, just create a new file */
	        	
	        	if(!file.exists()) {
	        		
	    			try {	    				
						file.createNewFile();						
					} catch (IOException e) {						
						e.printStackTrace();						
					}		
	    			
	    		} else {
	    			
	    		/* If the file exists, read contents from the file */
	        	
        		try {
        			
					java.util.Scanner scanner1 = new java.util.Scanner(file);
					
					while (scanner1.hasNextLine()) {
					
						String taskLine = scanner1.nextLine();
						
						/* Split every line in file in two parts: done marker and task content */
						int task_done = Integer.valueOf(taskLine.split(" ")[0]);
						Text task_content = new Text(taskLine.substring(2));
						
						/* If task line starts with 1, it means the task is done, so strike it through */
						
						if (task_done == 1) {
							task_content.setStrikethrough(true);
						}
						
						tasks.add(task_content);
						
					}
					
					scanner1.close();
					
        			} catch (FileNotFoundException e) {        				
    					e.printStackTrace();    					
    				}
        		
	    		}
				
	        	/* Tasks are done, create main view */
				final ListView<Text> tasks2 = new ListView<Text>();
				tasks2.setItems(tasks);
										
				HBox mainBottom = new HBox(10);
				mainBottom.setPadding(new Insets(10));
				
				/* TextField ADD TASK.
				 * Contents are saved when ADD TASK button is pressed.
				 */				
				final TextField textAddTask = new TextField();					
				HBox.setHgrow(textAddTask, Priority.ALWAYS);
				mainBottom.getChildren().add(textAddTask);
				
				/* Button ADD TASK.
				 * Adds a new task to the list (no strikethrough because the task is new)
				 */
				
				Button buttonAddTask = new Button("Add");
								
				buttonAddTask.setOnMouseClicked(new EventHandler<MouseEvent>() {
			        
					public void handle(MouseEvent me) {
						
						String newTask = textAddTask.getCharacters().toString();
						tasks.add(new Text(newTask));
						
					}
					
				});
				
				mainBottom.getChildren().add(buttonAddTask);
				
				/* Button DONE.
				 * Changes the strikethrough property of selected task.
				 */
					
				final Button buttonDone = new Button("Done");
							
				buttonDone.setOnMouseClicked(new EventHandler<MouseEvent>() {
			        
					public void handle(MouseEvent me) {
						
						if (tasks2.getSelectionModel().getSelectedItem().isStrikethrough()) {
			            	tasks2.getSelectionModel().getSelectedItem().setStrikethrough(false);
			            } else {
			            	tasks2.getSelectionModel().getSelectedItem().setStrikethrough(true);
			            }
						
					}
					
				});
				
				mainBottom.getChildren().add(buttonDone);
					
					final BorderPane root2 = new BorderPane();
					root2.setCenter(tasks2);
					root2.setBottom(mainBottom);
					Scene scene2 = new Scene(root2, 310, 410, Color.WHITE);
					primaryStage.setScene(scene2);
					
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
		
		/* On hiding ask to save the file */
		primaryStage.setOnHiding(new EventHandler<WindowEvent>() {
			
	        public void handle(WindowEvent event) {
	        	
	        	final Stage exit = new Stage();
	        	Label exitQuestion = new Label("Do you want to update " + filePath + "?");
	        	Button exitYes = new Button("Yes");
	        	Button exitNo = new Button("No");
	        	
	        	/* Exit and save changes */
	        	exitYes.setOnAction(new EventHandler<ActionEvent>() {
	                public void handle(ActionEvent event) {
	                	
	                	java.io.File file = new File(filePath);
	                	
	                	/* Need to clean the old file somehow */
	                	if (file.exists()) {
	                		file.delete();
	                	}	
	                	
	                	try {
							
	                		file.createNewFile();
							java.io.PrintWriter pw = new java.io.PrintWriter(file);
							
							for(Text t: tasks) {
								
								if (t.isStrikethrough()) {
									pw.print("1 ");
								} else {
									pw.print("0 ");
								}
								
								pw.println(t.getText());
								
							}
							
							pw.close();
							
						} catch (IOException e) {
							e.printStackTrace();
						}
	                	
	                    exit.hide();
	                    primaryStage.hide();	                    
	                    
	                }
	            });
	        	
	        	/* Exit with no changes saved */
	        	exitNo.setOnAction(new EventHandler<ActionEvent>() {
	                public void handle(ActionEvent event) {
	                    exit.hide();
	                    primaryStage.hide();
	                }
	            });
	        	
	        	VBox exitVBox = new VBox(10);
	        	exitVBox.setPadding(new Insets(10));
                HBox exitHBox = new HBox(10);
                exitHBox.setAlignment(Pos.CENTER);
                exitHBox.setPadding(new Insets(10));
                exitHBox.getChildren().addAll(exitYes, exitNo);
                exitVBox.getChildren().addAll(exitQuestion, exitHBox);
                Scene exitQuestionScene = new Scene(exitVBox, Color.WHITE);
                exit.setScene(exitQuestionScene);
                exit.show();
	        	
	        }
		});
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

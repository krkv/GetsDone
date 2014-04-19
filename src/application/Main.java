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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Main extends Application {
	
	public static String filePath = "default.txt";
	public static File file = new File(filePath);
	public static ObservableList<Text> tasks = FXCollections.observableArrayList();
	public static ListView<Text> listMainTasks = new ListView<Text>();
	public static Text textMainFile = new Text(filePath);
	
	public void onStart() {
		
		listMainTasks.setItems(tasks);
		if(!file.exists()) {    		
			try {	    				
				file.createNewFile();						
			} catch (IOException e) {						
				e.printStackTrace();						
			}			
		} else {
			try {    			
				java.util.Scanner scanner = new java.util.Scanner(file);				
				while (scanner.hasNextLine()) {				
					String taskLine = scanner.nextLine();
					int task_done = Integer.valueOf(taskLine.split(" ")[0]);
					Text task_content = new Text(taskLine.substring(2));
					if (task_done == 1) {
						task_content.setStrikethrough(true);
					}
					tasks.add(task_content);
				}
				scanner.close();				
    			} catch (FileNotFoundException e) {        				
					e.printStackTrace();    					
				}
		}				
	}
			
	public void openSettings() {
		
		final Stage stageSettings = new Stage();
		stageSettings.setResizable(false);
		Label labelSettings = new Label("Set the source file");
		final TextField fieldSettings = new TextField();
		fieldSettings.setText(filePath);
		Button buttonSettingsChange = new Button("Change");
		Button buttonSettingsCancel = new Button("Cancel");
		HBox boxSettingsButtons = new HBox(10);
		boxSettingsButtons.setAlignment(Pos.CENTER);
		boxSettingsButtons.getChildren().addAll(buttonSettingsChange, buttonSettingsCancel);
		VBox boxSettingsRoot = new VBox(10);
		boxSettingsRoot.setPadding(new Insets(10));
		boxSettingsRoot.setAlignment(Pos.CENTER);
		boxSettingsRoot.getChildren().addAll(labelSettings, fieldSettings, boxSettingsButtons);
		Scene sceneSettings = new Scene(boxSettingsRoot,300,150);
		stageSettings.setScene(sceneSettings);
		stageSettings.show();
		
		/* SETTINGS: CANCEL */
		buttonSettingsCancel.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                stageSettings.hide();
            }
        });
		
		/* SETTINGS: CHANGE */
		buttonSettingsChange.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {                
                filePath = fieldSettings.getCharacters().toString();
                file = new File(filePath);
                textMainFile.setText(filePath);
                tasks.clear();
                onStart();
                stageSettings.hide();
            }
        });
		
	}
	
	public void onExit() {
		
		final Stage stageExit = new Stage();
		stageExit.setResizable(false);
		Label labelExit = new Label("Do you want to update " + filePath + "?");
		Button buttonExitYes = new Button("Yes");
		Button buttonExitNo = new Button("No");
		HBox boxExitButtons = new HBox(10);
		boxExitButtons.setPadding(new Insets(10));
		boxExitButtons.setAlignment(Pos.CENTER);
		boxExitButtons.getChildren().addAll(buttonExitYes, buttonExitNo);
		VBox boxExitRoot = new VBox();
		boxExitRoot.setPadding(new Insets(10));
		boxExitRoot.setAlignment(Pos.CENTER);
		boxExitRoot.getChildren().addAll(labelExit, boxExitButtons);
		Scene sceneExit = new Scene(boxExitRoot,300,100);
		stageExit.setScene(sceneExit);
		stageExit.show();
		
		/* UPDATE: NO */
		buttonExitNo.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                stageExit.hide();
            }
        });
		
		/* UPDATE: YES */
		buttonExitYes.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
            	File file = new File(filePath);
            	if(file.exists()){
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
                stageExit.hide();
            }
        });
		
	}
		
	@Override
	public void start(final Stage stageMain) {
		
		onStart();		
		stageMain.setTitle("TODO");
		stageMain.setResizable(false);
		BorderPane paneMainRoot = new BorderPane();
		/* BOX MAIN TOP */		
		Button buttonMainSettings = new Button("Settings");					
		HBox boxMainTop = new HBox(10);
		boxMainTop.setPadding(new Insets(10));
		boxMainTop.setAlignment(Pos.CENTER_RIGHT);
		boxMainTop.getChildren().addAll(textMainFile, buttonMainSettings);
		textMainFile.setFill(Color.GRAY);
		paneMainRoot.setTop(boxMainTop);
		/* MAIN: SETTINGS */
		buttonMainSettings.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                openSettings();
            }
        });
		/* BOX MAIN CENTER */		
		paneMainRoot.setCenter(listMainTasks);
		/* BOX MAIN BOTTOM */
		final TextField fieldMainAdd = new TextField();
		HBox.setHgrow(fieldMainAdd, Priority.ALWAYS);
		Button buttonMainAdd = new Button("Add");
		Button buttonMainDone = new Button("Done");
		HBox boxMainBottom = new HBox(10);
		boxMainBottom.setPadding(new Insets(10));
		boxMainBottom.getChildren().addAll(fieldMainAdd, buttonMainAdd, buttonMainDone);
		paneMainRoot.setBottom(boxMainBottom);
		/* MAIN: ADD */
		buttonMainAdd.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {				
				String newTaskContent = fieldMainAdd.getCharacters().toString();
				tasks.add(new Text(newTaskContent));
				fieldMainAdd.setText("");
			}			
		});
		/* MAIN: DONE */
		buttonMainDone.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {				
				if (listMainTasks.getSelectionModel().getSelectedItem().isStrikethrough()) {
					listMainTasks.getSelectionModel().getSelectedItem().setStrikethrough(false);
	            } else {
	            	listMainTasks.getSelectionModel().getSelectedItem().setStrikethrough(true);
	            }				
			}			
		});
		Scene sceneMain = new Scene(paneMainRoot, 300, 410);		
		stageMain.setScene(sceneMain);
		stageMain.show();		
		/* MAIN: CLOSE */
		stageMain.setOnHiding(new EventHandler<WindowEvent>() {			
	        public void handle(WindowEvent event) {
	        	onExit();	        	
	        }
		});		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

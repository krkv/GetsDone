package application;
	
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

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
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Main extends Application {
	
	public static String filePath = "tasks.txt";
	public static File file = new File(filePath);
	public static ObservableList<Text> tasks = FXCollections.observableArrayList();
	public static ListView<Text> listMainTasks = new ListView<Text>();
	public static boolean changed = false;
	
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
					String stringTask = scanner.nextLine();
					int intTaskDone = Integer.valueOf(stringTask.split(" ")[0]);
					Text textTask = new Text(stringTask.substring(2));
					textTask.setFont(new Font(20.0));
					textTask.setWrappingWidth(290);
					if (intTaskDone == 1) {
						textTask.setStrikethrough(true);
						textTask.setFill(Color.GRAY);
					}
					tasks.add(textTask);
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
		Label labelSettings = new Label("Import tasks from a text file");
		final TextField fieldSettings = new TextField();
		fieldSettings.setText("path to file");
		final Button buttonSettingsImport = new Button("Import");
		Button buttonSettingsClose = new Button("Close settings");
		Button buttonSettingsRemoveAllDone = new Button("Remove all tasks that are done");
		HBox boxSettingsImport = new HBox(10);
		boxSettingsImport.setAlignment(Pos.CENTER);
		boxSettingsImport.getChildren().addAll(fieldSettings, buttonSettingsImport);
		VBox boxSettingsRoot = new VBox(10);
		boxSettingsRoot.setPadding(new Insets(10));
		boxSettingsRoot.setAlignment(Pos.CENTER);
		boxSettingsRoot.getChildren().addAll(labelSettings, boxSettingsImport, buttonSettingsRemoveAllDone, buttonSettingsClose);
		Scene sceneSettings = new Scene(boxSettingsRoot,300,150);
		stageSettings.setScene(sceneSettings);
		stageSettings.show();
		
		/* FIELD SETTINGS */
		fieldSettings.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
            	buttonSettingsImport.fire();
            }
		});
		
		/* BUTTON SETTINGS REMOVE ALL DONE */
		buttonSettingsRemoveAllDone.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
            	ArrayList<Text> remove = new ArrayList<Text>();
            	for(Text t: tasks) {
            		if (t.isStrikethrough()) {
            			remove.add(t);
					}
            	}
            	tasks.removeAll(remove);
            	changed = true;
                stageSettings.hide();
            }
        });
		
		/* BUTTON SETTINGS CLOSE */
		buttonSettingsClose.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                stageSettings.hide();
            }
        });
		
		/* BUTTON SETTINGS IMPORT */
		buttonSettingsImport.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {                
                String importPath = fieldSettings.getCharacters().toString();
                File importFile = new File(importPath);
                try {    			
    				java.util.Scanner scanner = new java.util.Scanner(importFile);				
    				while (scanner.hasNextLine()) {				
    					String stringTask = scanner.nextLine();
    					Text textTask = new Text(stringTask);
    					textTask.setFont(new Font(20.0));
    					textTask.setWrappingWidth(290);
    					tasks.add(textTask);
    				}
    				scanner.close();				
        			} catch (FileNotFoundException e) {
        				// file not found error here?
    					e.printStackTrace();    					
    				} finally {
    					changed = true;
    					stageSettings.hide();
    				}
            }
        });
		
	}
	
	public void onExit() {
		
		final Stage stageExit = new Stage();
		stageExit.setResizable(false);
		Label labelExit = new Label("Do you want to update your list?");
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
		
		/* BUTTON UPDATE NO */
		buttonExitNo.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                stageExit.hide();
            }
        });
		
		/* BUTTON UPDATE YES */
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
		boxMainTop.getChildren().addAll(buttonMainSettings);
		paneMainRoot.setTop(boxMainTop);
		
		/* BUTTON MAIN SETTINGS */
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
		final Button buttonMainAdd = new Button("Add");
		Button buttonMainDone = new Button("Done");
		Button buttonMainRemove = new Button("Remove");
		HBox boxMainBottom = new HBox(10);
		boxMainBottom.setPadding(new Insets(10));
		boxMainBottom.getChildren().addAll(fieldMainAdd, buttonMainAdd, buttonMainDone, buttonMainRemove);
		paneMainRoot.setBottom(boxMainBottom);
		
		/* FIELD MAIN ADD */
		fieldMainAdd.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
            	buttonMainAdd.fire();
            }
		});
		
		/* BUTTON MAIN ADD */
		buttonMainAdd.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {				
				String stringNewTask = fieldMainAdd.getCharacters().toString();
				if (!stringNewTask.isEmpty()) {
					Text textNewTask = new Text(stringNewTask);
					textNewTask.setFont(new Font(20.0));
					textNewTask.setWrappingWidth(390);
					tasks.add(0, textNewTask);
					fieldMainAdd.setText("");
					changed = true;
				}
			}			
		});
		
		/* BUTTON MAIN DONE */
		buttonMainDone.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
            	Text textSelected = listMainTasks.getSelectionModel().getSelectedItem();
				if (textSelected.isStrikethrough()) {
					textSelected.setStrikethrough(false);
					tasks.remove(textSelected);
					tasks.add(0, textSelected);
					textSelected.setFill(Color.BLACK);
	            } else {
	            	textSelected.setStrikethrough(true);
	            	tasks.remove(textSelected);
	            	tasks.add(tasks.size(), textSelected);
	            	textSelected.setFill(Color.GRAY);
	            }
				changed = true;
			}			
		});
		
		/* BUTTON MAIN REMOVE */
		buttonMainRemove.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {				
            	tasks.remove(listMainTasks.getSelectionModel().getSelectedItem());
            	changed = true;
			}			
		});
		
		Scene sceneMain = new Scene(paneMainRoot, 400, 410);		
		stageMain.setScene(sceneMain);
		stageMain.show();		
		
		/* MAIN ON CLOSE */
		stageMain.setOnHiding(new EventHandler<WindowEvent>() {			
	        public void handle(WindowEvent event) {
	        	if (changed) onExit();	        	
	        }
		});
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

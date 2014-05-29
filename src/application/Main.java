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
	public static String settingsPath = "settings.gsd";
	
	public static File file = new File(filePath);
	public static File settings = new File(settingsPath);
	
	public static ObservableList<Text> tasks = FXCollections.observableArrayList();
	public static ListView<Text> listMainTasks = new ListView<Text>();
	
	public static boolean changed = false;
	public static double taskTextSize = 20.0;
	
	public void onStart() {
		
		listMainTasks.setItems(tasks);
		
		/* READ SETTINGS */
		if(settings.exists()) {
			try {    			
				java.util.Scanner scanner = new java.util.Scanner(settings);				
				while (scanner.hasNextLine()) {				
					String string = scanner.nextLine();
					System.out.println(string);
					if (string.equals("14.0")) taskTextSize = 14.0;
					else if (string.equals("26.0")) taskTextSize = 26.0;
				}
				scanner.close();				
    			} catch (FileNotFoundException e) {        				
					e.printStackTrace();    					
				}
		}
		
		/* READ TASKS */
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
					String stringTaskDone = stringTask.split(" ")[0];
					Text textTask;
					if (stringTaskDone.equals("[done]")) {
						textTask = new Text(stringTask.substring(7));
						textTask.setStrikethrough(true);
						textTask.setFill(Color.GRAY);
					} else {
						textTask = new Text(stringTask);
					}
					textTask.setFont(new Font(taskTextSize));
					textTask.setWrappingWidth(290);
					
					tasks.add(textTask);
				}
				scanner.close();				
    			} catch (FileNotFoundException e) {        				
					e.printStackTrace();    					
				}
		}				
	}
	
	/* METHOD OPEN IMPORT */
	public void openImport() {
		
		final Stage stageImport = new Stage();
		stageImport.setResizable(false);
		
		Label labelImport = new Label("Import tasks from a text file:");
		final Label labelImportError = new Label("Can't find this file!");
		labelImportError.setTextFill(Color.RED);
		
		final TextField fieldImportPath = new TextField();
		fieldImportPath.setText("Path to file");
		
		final Button buttonImportImport = new Button("Import");
		Button buttonImportClose = new Button("Close");
		
		HBox boxImport = new HBox(10);
		boxImport.setAlignment(Pos.CENTER);
		boxImport.getChildren().addAll(fieldImportPath, buttonImportImport);
		
		final VBox boxImportRoot = new VBox(10);
		boxImportRoot.setPadding(new Insets(10));
		boxImportRoot.setAlignment(Pos.CENTER);
		boxImportRoot.getChildren().addAll(labelImport, boxImport, buttonImportClose);
		
		Scene sceneImport = new Scene(boxImportRoot,300,150);
		
		stageImport.setScene(sceneImport);
		stageImport.show();
		
		/* FIELD IMPORT PATH */
		fieldImportPath.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
            	buttonImportImport.fire();
            }
		});
		
		/* BUTTON IMPORT IMPORT */
		buttonImportImport.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {                
                String importPath = fieldImportPath.getCharacters().toString();
                File importFile = new File(importPath);
                try {    			
    				java.util.Scanner scanner = new java.util.Scanner(importFile);
    				while (scanner.hasNextLine()) {				
    					String stringTask = scanner.nextLine();
    					Text textTask = new Text(stringTask);
    					textTask.setFont(new Font(taskTextSize));
    					textTask.setWrappingWidth(290);
    					tasks.add(textTask);
    				}
    				scanner.close();
    				changed = true;
    				stageImport.hide();				
        			} catch (FileNotFoundException e) {
        				if (boxImportRoot.getChildren().get(2) != labelImportError) {
        					boxImportRoot.getChildren().add(2, labelImportError);
        				}
        				fieldImportPath.setText(importPath);    					
    				}
            }
        });
				
		/* BUTTON IMPORT TASKS CLOSE */
		buttonImportClose.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                stageImport.hide();
            }
        });		
		
	}		
		
	/* METHOD ON EXIT */
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
							pw.print("[done] ");
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
		stageMain.setTitle("Get Shit Done");
		stageMain.setResizable(false);
		BorderPane paneMainRoot = new BorderPane();
		
		/* BOX MAIN TOP */
		Button buttonMainMore = new Button("...");
		Button buttonMainDone = new Button("Done");
		Button buttonMainDeleteSelected = new Button("Delete");
		Button buttonMainDeleteDone = new Button("Delete all done");
		Button buttonMainImport = new Button("Import");
		Button buttonMainFont = new Button("Font");
		
		final VBox boxMainTop = new VBox(0);
		
		HBox boxMainTopLevel1 = new HBox(10);
		boxMainTopLevel1.setPadding(new Insets(10));
		boxMainTopLevel1.setAlignment(Pos.CENTER_LEFT);
		
		boxMainTopLevel1.getChildren().addAll(buttonMainDone, buttonMainMore);
		
		final HBox boxMainTopLevel2 = new HBox(10);
		boxMainTopLevel2.setPadding(new Insets(10));
		
		boxMainTopLevel2.getChildren().addAll(buttonMainDeleteSelected, buttonMainDeleteDone, buttonMainImport, buttonMainFont);
		
		boxMainTop.getChildren().add(boxMainTopLevel1);
		
		paneMainRoot.setTop(boxMainTop);
		
		/* BUTTON MAIN MORE */
		buttonMainMore.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                if (boxMainTop.getChildren().size() == 1) {
                	boxMainTop.getChildren().add(boxMainTopLevel2);
                } else boxMainTop.getChildren().remove(boxMainTopLevel2);
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
		
		/* BUTTON MAIN DELETE SELECTED */
		buttonMainDeleteSelected.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
            	tasks.remove(listMainTasks.getSelectionModel().getSelectedItem());
            	changed = true;
			}			
		});
		
		/* BUTTON MAIN DELETE DONE */
		buttonMainDeleteDone.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
            	ArrayList<Text> remove = new ArrayList<Text>();
            	for(Text t: tasks) {
            		if (t.isStrikethrough()) {
            			remove.add(t);
					}
            	}
            	tasks.removeAll(remove);
            	changed = true;
            }
        });
		
		/* BUTTON MAIN IMPORT */
		buttonMainImport.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                openImport();
            }
        });
		
		/* BUTTON MAIN FONT */
		buttonMainFont.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
            	if (taskTextSize == 14.0) {
            		taskTextSize = 20.0;
            		for (Text t: tasks) {
            			t.setFont(new Font(20.0));
            		}
            	} else if (taskTextSize == 20.0) {
            		taskTextSize = 26.0;
            		for (Text t: tasks) {
            			t.setFont(new Font(26.0));
            		}
            	} else if (taskTextSize == 26.0) {
            		taskTextSize = 14.0;
            		for (Text t: tasks) {
            			t.setFont(new Font(14.0));
            		}
            	}
			}			
		});
		
		/* BOX MAIN CENTER */		
		paneMainRoot.setCenter(listMainTasks);
		
		/* BOX MAIN BOTTOM */
		final TextField fieldMainAdd = new TextField();
		final Button buttonMainAdd = new Button("Add");
		
		HBox boxMainBottom = new HBox(10);
		boxMainBottom.setPadding(new Insets(10));
		boxMainBottom.getChildren().addAll(fieldMainAdd, buttonMainAdd);
		HBox.setHgrow(fieldMainAdd, Priority.ALWAYS);
		
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
				if (stringNewTask.isEmpty() || stringNewTask.substring(0,6).equals("[done]")) {
					fieldMainAdd.setText("");
				} else {
					Text textNewTask = new Text(stringNewTask);
					textNewTask.setFont(new Font(taskTextSize));
					textNewTask.setWrappingWidth(290);
					tasks.add(0, textNewTask);
					fieldMainAdd.setText("");
					changed = true;
				}
			}			
		});
		
		Scene sceneMain = new Scene(paneMainRoot, 300, 410);		
		stageMain.setScene(sceneMain);
		stageMain.show();		
		
		/* MAIN ON CLOSE */
		stageMain.setOnHiding(new EventHandler<WindowEvent>() {			
	        public void handle(WindowEvent event) {
	        	
	        	File settings = new File(settingsPath);
	        	if(settings.exists()){
	        		settings.delete();
	        	}
	        	try {
	        		settings.createNewFile();
	    			java.io.PrintWriter pw = new java.io.PrintWriter(settings);
	    			pw.print(taskTextSize);
	    			pw.close();				
	    		} catch (IOException e) {
	    			e.printStackTrace();
	    		}
	        	
	        	if (changed) onExit();	        	
	        }
		});
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

package application;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Driver extends Application {
	TextArea TA = new TextArea ();
	DLL MajorList = new DLL();
	Snode Snode = new Snode();
	SLL SLL = new SLL ();
	Dnode dnode = new Dnode ();
	ObservableList<Snode> observableList1 = FXCollections.observableArrayList();
	ObservableList<Dnode> observableList2 = FXCollections.observableArrayList();

	TableView<Snode> tableView1 = new TableView<>();
	TableView<Dnode> tableView2 = new TableView<>();

	TableView<Snode> rejectionTable = new TableView<>(); 
	private ComboBox<String> majorComboBox = new  ComboBox <> (); 

	TextArea StatTA = new TextArea ();

	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("Load Files");
		Button load = new Button("Load student File");
		Button load2 = new Button("Load criteria File");
		Label lb = new Label("Load the criteria file first then the student file");
		lb.setFont(Font.font("Times New Roman", 20)); 
		HBox hbox$ = new HBox(lb);
		hbox$.setAlignment(Pos.CENTER);
		load.setStyle("-fx-background-color: Black; -fx-text-fill: white;");
		load2.setStyle("-fx-background-color: Black; -fx-text-fill: white;");
		Image image = new Image("file:///C:/Users/user/Desktop/uni.jpg");
		ImageView imageView = new ImageView(image);
		imageView.setFitWidth(1000);
		imageView.setPreserveRatio(true);
		HBox  hbox = new HBox(5,load,load2);
		hbox.setAlignment(Pos.BOTTOM_CENTER);
		VBox vbox$ = new VBox(10, imageView, hbox$, hbox );
		vbox$.setAlignment(Pos.TOP_CENTER);

		Scene scene1 = new Scene(vbox$, 1000, 500);
		primaryStage.setScene(scene1);
		primaryStage.show();
		load.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open student File");
			File studentFile = fileChooser.showOpenDialog(primaryStage);
			if (studentFile != null) {
				loadStudent(studentFile);
				primaryStage.setScene(Scene2(primaryStage));
			} else {
				showAlert1("No file selected for student data.");
			}
		});
		//******************************************************************************************************		
		load2.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();

			fileChooser.setTitle("Open criteria File"); 
			File criteriaFile = fileChooser.showOpenDialog(primaryStage);
			if (criteriaFile != null) {
				loadCriteriaData(criteriaFile);
			} else {
				showAlert1("No file selected for criteria data.");

			}
		});

	}
	//********************************************************************************************************

	private void loadCriteriaData(File criteriaFile) {
	    try (Scanner scanner = new Scanner(criteriaFile)) {
	        if (scanner.hasNextLine()) {
//	            scanner.nextLine(); // Skip header
	        }
	        while (scanner.hasNextLine()) {
	            String line = scanner.nextLine().trim();
	            if (line.isEmpty()) {
	                continue;
	            }
	            String[] parts = line.split("\\|");

	            if (parts.length == 4) {
	                String major = parts[3].trim();
	                int acceptanceGrade = Integer.parseInt(parts[2].trim());
	                double tawjihiWeight = Double.parseDouble(parts[1].trim());
	                double placementWeight = Double.parseDouble(parts[0].trim());

	                Dnode majorNode = findMajorNode(MajorList, major);
	                if (majorNode == null) {
	                    majorNode = new Dnode(major);
	                    majorNode.setAcceptanceGrade(acceptanceGrade);
	                    majorNode.setTawjihiWeight(tawjihiWeight);
	                    majorNode.setPlacementWeight(placementWeight);
	                    MajorList.add(majorNode);
	                    observableList2.add(majorNode);
	                    majorComboBox.getItems().add(major);
	                } else {
	                    showAlert1("Major already exists: " + major);
	                }
	            } else {
	                showAlert1("Invalid data line: " + line);
	            }
	        }
	    } catch (FileNotFoundException ex) {
	        showAlert1("Criteria file not found: " + ex.getMessage());
	    } catch (NumberFormatException ex) {
	        showAlert1("Invalid number format in criteria file: " + ex.getMessage());
	    }
	}


	public void loadStudent(File filePath) {
	    try (Scanner scanner = new Scanner(filePath)) {
	        if (scanner.hasNextLine()) {
	            String headerLine = scanner.nextLine().trim(); 
	        }
	        while (scanner.hasNextLine()) {
	            String line = scanner.nextLine().trim();
	            String[] parts = line.split("\\|");
	            if (parts.length == 5) {
	                int id = Integer.parseInt(parts[0].trim());
	                String name = parts[1].trim();
	                double tawjihiGrade = Double.parseDouble(parts[2].trim());
	                double placementGrade = Double.parseDouble(parts[3].trim());
	                String major = parts[4].trim();

	                if (SLL.search(id) != null) {
	                    showAlert3("Student with ID " + id + " already exists, skipping: " + line);
	                    continue;
	                }

	                Snode newStudent = new Snode(id, name, tawjihiGrade, placementGrade, major);
	                Dnode majorNode = findMajorNode(MajorList, major);
	                if (majorNode == null) {
	                    majorNode = new Dnode(major, tawjihiGrade, placementGrade);
	                    MajorList.add(majorNode);
	                }
	                majorNode.getStudentList().add(newStudent, majorNode);
	                observableList1.add(newStudent);
	            } else {
	                showAlert3("Line format is incorrect, skipping: " + line);
	            }
	        }
	    } catch (FileNotFoundException e) {
	        showAlert3("File not found: " + e.getMessage());
	    } catch (NumberFormatException e) {
	        showAlert3("Error in number format: " + e.getMessage());
	    }
	}

	//**********************************************************************************************************

	private void showAlert1(String message) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle("Warning");
		alert.setContentText(message);
		alert.showAndWait();
	}


	private void showAlert2(String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setContentText(message);
		alert.showAndWait();
	}

	private void showAlert3(String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");
		alert.setContentText(message);
		alert.showAndWait();
	}
	//***********************************************************************************************************
	private Dnode findMajorNode(DLL MajorList, String majorName) {
		Dnode currentMajor = MajorList.getFirst();
		if (currentMajor == null) {
			return null; 
		}

		do {
			if (currentMajor.getName().equalsIgnoreCase(majorName)) {
				return currentMajor; 
			}
			currentMajor = currentMajor.getNext();
		} while (currentMajor != MajorList.getFirst());

		return null; 
	}


	//*********************************************************************************************************
	public Snode findStudentNode(SLL studentList, int id) {
		if (studentList.getFirst() == null) {
			return null; 
		}

		Snode current = studentList.getFirst();
		do {
			if (current.getId() == id) {
				return current; 
			}
			current = current.getNext();
		} while (current != studentList.getFirst()); 

		return null; 
	}

	//*********************************************************************************************************
	public Scene Scene2(Stage primaryStage) {
		HBox hbox = new HBox (TA);
		hbox.setAlignment(Pos.CENTER);
		MenuBar Menu = new MenuBar ();
		Menu Major = new Menu ("Major Double linked list");
		Major.setStyle("-fx-background-color: pink; ");
		Menu Student = new Menu ("Students Single linked list");
		Menu TableViewOfStudent = new Menu (" Student tableview");
		TableViewOfStudent.setStyle("-fx-background-color: pink; ");
		Menu checkAdmission = new Menu (" Check Admission");
		checkAdmission.setStyle("-fx-background-color: pink; ");
		Menu Stat = new Menu (" Statistics & save to files");
		Menu TableviewOfMajor = new Menu (" Major tableview");


		MenuItem Insert_Student = new MenuItem ("Insert Student");
		MenuItem DeleteStudent = new MenuItem ("Delete Student");
		MenuItem updateStudent = new MenuItem ("update Student");
		MenuItem searchStudent = new MenuItem ("search Student");
		MenuItem InsertMajor  = new MenuItem ("Insert Major");
		MenuItem DeleteMajor = new MenuItem ("Delete Major");
		MenuItem updateMajor = new MenuItem ("update Major");
		MenuItem searchMajor = new MenuItem ("search Major");
		MenuItem StudentTableview = new MenuItem ("Student tableview");
		MenuItem checkAdmission$ = new MenuItem (" Check Admission");
		MenuItem stat = new MenuItem (" Statistics");
		MenuItem save = new MenuItem (" Save updates");
		MenuItem MajorTableview = new MenuItem (" Major tableview");



		Major.getItems().addAll(InsertMajor,DeleteMajor,updateMajor,searchMajor);

		Student.getItems().addAll(Insert_Student,DeleteStudent,updateStudent,searchStudent);
		TableViewOfStudent.getItems().add(StudentTableview);
		checkAdmission.getItems().add(checkAdmission$);
		Stat.getItems().addAll(stat ,save);
		TableviewOfMajor.getItems().add(MajorTableview);


		Menu.getMenus().addAll(Major,Student,TableViewOfStudent ,TableviewOfMajor ,checkAdmission,Stat );
		InsertMajor.setOnAction(e ->  InsertMajor ( primaryStage));
		DeleteMajor.setOnAction(e ->  deleteMajor ( primaryStage));
		updateMajor.setOnAction(e ->  updateMajor ( primaryStage));
		searchMajor.setOnAction(e ->  searchMajor ( primaryStage));
		TableViewOfStudent.setOnAction(e -> {Studenttableview ( primaryStage);});

		Insert_Student.setOnAction(e -> insertStudent( primaryStage));
		updateStudent.setOnAction(e -> updateStudent( primaryStage));
		DeleteStudent.setOnAction(e -> 		deleteStudent( primaryStage));
		searchStudent.setOnAction(e ->		searchStudent( primaryStage));
		checkAdmission$.setOnAction(e -> checkAdmission( primaryStage));
		stat.setOnAction(e ->showStatistics( primaryStage) );
		save.setOnAction( e -> saveupdates( primaryStage));
		MajorTableview.setOnAction(e -> MajorTableview ( primaryStage));
		Button next = new Button ("Next");
		next.setStyle("-fx-background-color: green; -fx-text-fill: #FFFFFF; -fx-font-weight: bold;");
		next.setOnAction(e -> {
			NextMajor();

		});

		Button Pre = new Button ("Previous");
		Pre.setStyle("-fx-background-color: #F44336; -fx-text-fill: #FFFFFF; -fx-font-weight: bold;");
		Pre.setOnAction(e -> {

			PreviousMajor(); 

		});

		HBox hbox_button = new HBox (60,Pre,next);
		hbox_button.setAlignment(Pos.CENTER);
		dnode = MajorList.getFirst(); 
		updateMajorInfo();
		VBox vbox = new VBox (30,Menu ,hbox,hbox_button);
		Scene scene = new Scene (vbox,1000, 500);

		return scene;

	}
	//**********************************************************************************************
	public void Studenttableview(Stage primaryStage) {
		tableView1.getColumns().clear();

		TableColumn<Snode, Integer> IDColumn = new TableColumn<>("ID");
		TableColumn<Snode, String> NameColumn = new TableColumn<>("Name");
		TableColumn<Snode, Double> TWColumn = new TableColumn<>("Tawjihi Grade");
		TableColumn<Snode, Double> PTColumn = new TableColumn<>("Placement Test Grade");
		TableColumn<Snode, String> MajorColumn = new TableColumn<>("Chosen Major");
		TableColumn<Snode, Double> Admissionmark = new TableColumn<>("Admission Mark");


		IDColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
		NameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
		TWColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTawjihiGrade()).asObject());
		PTColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPlacementTestGrade()).asObject());
		MajorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMajor()));
		Admissionmark.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getAdmissionMark()).asObject());


		tableView1.getColumns().addAll(IDColumn, NameColumn, TWColumn, PTColumn, MajorColumn, Admissionmark );

		tableView1.setItems(observableList1);

		Button back = new Button("Back");
		back.setStyle("-fx-background-color: Black; -fx-text-fill: white;");

		VBox vbox$ = new VBox(15, tableView1, back);
		vbox$.setAlignment(Pos.TOP_CENTER);

		back.setOnAction(e -> {
			primaryStage.setScene(Scene2(primaryStage));
		});

		Scene scene1 = new Scene(vbox$, 1000, 500);
		primaryStage.setScene(scene1);
		primaryStage.show();
	}
	//***************************************************************************************************
	public void InsertMajor(Stage primaryStage) {
		tableView2.getColumns().clear();

		primaryStage.setTitle("Insert Major");
		Label lb = new Label("Insert The New Major information ");
		lb.setFont(new Font(20));

		TextField TF = new TextField();
		TextField TF2 = new TextField();
		TextField TF3 = new TextField();
		TextField TF4 = new TextField();

		HBox hbox1 = new HBox(TF);
		hbox1.setAlignment(Pos.CENTER);
		HBox hbox2 = new HBox(TF2);
		hbox2.setAlignment(Pos.CENTER);
		HBox hbox3 = new HBox(TF3);
		hbox3.setAlignment(Pos.CENTER);
		HBox hbox4 = new HBox(TF4);
		hbox4.setAlignment(Pos.CENTER);

		VBox vbox = new VBox(5, hbox1, hbox2, hbox3, hbox4);
		TF.setPromptText("New Major");
		TF2.setPromptText("Acceptance grade");
		TF3.setPromptText("Tawjihi Weight");
		TF4.setPromptText("Placement Test Weight");

		TableColumn<Dnode, String> NameColumn = new TableColumn<>("Name");
		TableColumn<Dnode, Integer> AGColumn = new TableColumn<>("Acceptance Grade");
		TableColumn<Dnode, Double> TWColumn = new TableColumn<>("Tawjihi Weight");
		TableColumn<Dnode, Double> PTColumn = new TableColumn<>("Placement Test Weight");

		NameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
		AGColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAcceptanceGrade()).asObject());
		TWColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTawjihiWeight()).asObject());
		PTColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPlacementWeight()).asObject());

		tableView2.getColumns().addAll(NameColumn, AGColumn, TWColumn, PTColumn);


		tableView2.setItems(observableList2);

		Button insert = new Button("Insert");
		insert.setStyle("-fx-background-color: green; -fx-text-fill: #FFFFFF; -fx-font-weight: bold;");
		insert.setOnAction(e -> {
		    try {
		        String input1 = TF.getText();
		        String input2 = TF2.getText();
		        String input3 = TF3.getText();
		        String input4 = TF4.getText();

		        Dnode currmajor = MajorList.search(input1);
		        if (currmajor != null) {
		            showAlert1("This Major is already taken. Please enter a different major.");
		            return; 
		        }

		        int acceptanceGrade = Integer.parseInt(input2);
		        double tawjihiWeight = Double.parseDouble(input3);
		        double placementTestWeight = Double.parseDouble(input4);

		        // Check if the acceptance grade is less than 60
		        if (acceptanceGrade < 60 || acceptanceGrade > 100) {
		            showAlert1("Acceptance grade must be at least 60 and less than 100.");
		            return;
		        }

		        // Check if the sum of Tawjihi weight and Placement Test weight equals 1
		        if (tawjihiWeight + placementTestWeight != 1.0) {
		            showAlert1("The sum of Tawjihi weight and Placement Test weight must equal 1.");
		            return;
		        }

		        Dnode newNode = new Dnode(input1, acceptanceGrade, tawjihiWeight, placementTestWeight);
		        MajorList.add(newNode);
		        observableList2.add(newNode);
		        showAlert2("Major inserted successfully");


		        tableView2.setItems(observableList2);
		        TF.clear();
		        TF2.clear();
		        TF3.clear();
		        TF4.clear();
		    } catch (NumberFormatException ex) {
		        showAlert1("Please enter valid numbers in the fields");
		    }
		});

		Button back = new Button("Back");
		back.setStyle("-fx-background-color: #F44336; -fx-text-fill: #FFFFFF; -fx-font-weight: bold;");
		back.setOnAction(e -> primaryStage.setScene(Scene2(primaryStage)));

		HBox hbox$ = new HBox(60, back, insert);
		hbox$.setAlignment(Pos.CENTER);

		VBox vbox7 = new VBox(10, lb, vbox, tableView2, hbox$);
		vbox7.setAlignment(Pos.CENTER);

		Scene scene = new Scene(vbox7, 1000, 500);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	//*****************************************************************************************************
	private void updateMajor(Stage primaryStage) {
		tableView2.getColumns().clear();

		Label lb = new Label("Update Major ");
		lb.setFont(new Font(20));

		TextField TF1 = new TextField();
		TF1.setPromptText("Current Major");
		TextField TF2 = new TextField();
		TF2.setPromptText("Current Acceptance grade");
		TextField TF3 = new TextField();
		TF3.setPromptText("Current Tawjihi Weight");
		TextField TF4 = new TextField();
		TF4.setPromptText("Current Placement Test Weight");

		TextField TF5 = new TextField();
		TF5.setPromptText("New Major");
		TextField TF6 = new TextField();
		TF6.setPromptText("New Acceptance grade");
		TextField TF7 = new TextField();
		TF7.setPromptText("New Tawjihi Weight");
		TextField TF8 = new TextField();
		TF8.setPromptText("New Placement Test Weight");

		HBox hbox$ = new HBox(20, TF1, TF5);
		hbox$.setAlignment(Pos.CENTER);
		HBox hbox2 = new HBox(20, TF2, TF6);
		hbox2.setAlignment(Pos.CENTER);

		HBox hbox3 = new HBox(20, TF3, TF7);
		hbox3.setAlignment(Pos.CENTER);

		HBox hbox4 = new HBox(20, TF4, TF8);
		hbox4.setAlignment(Pos.CENTER);

		VBox Vbox = new VBox(hbox$,hbox2,hbox3,hbox4);

		TableColumn<Dnode, String> NameColumn = new TableColumn<>("Name");
		TableColumn<Dnode, Integer> AGColumn = new TableColumn<>("Acceptance Grade");
		TableColumn<Dnode, Double> TWColumn = new TableColumn<>("Tawjihi Weight");
		TableColumn<Dnode, Double> PTColumn = new TableColumn<>("Placement Test Weight");

		NameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
		AGColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAcceptanceGrade()).asObject());
		TWColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTawjihiWeight()).asObject());
		PTColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPlacementWeight()).asObject());

		tableView2.getColumns().addAll(NameColumn, AGColumn, TWColumn, PTColumn);


		tableView2.setItems(observableList2);

		Button update = new Button("Update");
		update.setStyle("-fx-background-color: green; -fx-text-fill: #FFFFFF; -fx-font-weight: bold;");

		update.setOnAction(e -> {
		    try {
		        String currentMajor = TF1.getText();
		        String newMajor = TF5.getText();
		        int newAcceptanceGrade = Integer.parseInt(TF6.getText());
		        double newTawjihiWeight = Double.parseDouble(TF7.getText());
		        double newPlacementTestWeight = Double.parseDouble(TF8.getText());

		        Dnode n = MajorList.search(currentMajor);
		        if (n != null) {

		            // Check if the new acceptance grade is less than 60
		            if (newAcceptanceGrade < 60) {
		                showAlert3("Acceptance grade must be at least 60.");
		                return;
		            }

		            // Check if the sum of new Tawjihi weight and Placement Test weight equals 1
		            if (newTawjihiWeight + newPlacementTestWeight != 1.0) {
		                showAlert3("The sum of Tawjihi weight and Placement Test weight must equal 1.");
		                return;
		            }

		            MajorList.update(currentMajor, newMajor, newAcceptanceGrade, newTawjihiWeight, newPlacementTestWeight);
		            tableView2.refresh();
		            showAlert2("Updated successfully");

		        } else {
		            showAlert3("Update failed, " + currentMajor + " not found.");
		        }

		        TF1.clear();
		        TF2.clear();
		        TF3.clear();
		        TF4.clear();
		        TF5.clear();
		        TF6.clear();
		        TF7.clear();
		        TF8.clear();
		    } catch (NumberFormatException ex) {
		        showAlert3("Please enter valid numbers in the fields.");
		    }
		});

		Button back = new Button("Back");
		back.setStyle("-fx-background-color: #F44336; -fx-text-fill: #FFFFFF; -fx-font-weight: bold;");
		back.setOnAction(e -> {
			primaryStage.setScene(Scene2(primaryStage));
		});

		HBox hbox = new HBox(60, back, update);
		hbox.setAlignment(Pos.BOTTOM_CENTER);
		VBox vbox = new VBox(10, lb, Vbox,tableView2, hbox);
		vbox.setAlignment(Pos.TOP_CENTER);
		Scene scene = new Scene(vbox, 1000, 500);

		primaryStage.setScene(scene);
		primaryStage.show();
	}
	//**************************************************************************************************
	private void deleteMajor(Stage primaryStage) {
		tableView2.getColumns().clear();

		Label lb = new Label("Delete Major ");
		lb.setFont(new Font(20));

		Label lb2 = new Label("Select the Major to delete");
		lb2.setFont(new Font(15));

		TableColumn<Dnode, String> NameColumn = new TableColumn<>("Name");
		TableColumn<Dnode, Integer> AGColumn = new TableColumn<>("Acceptance Grade");
		TableColumn<Dnode, Double> TWColumn = new TableColumn<>("Tawjihi Weight");
		TableColumn<Dnode, Double> PTColumn = new TableColumn<>("Placement Test Weight");

		NameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
		AGColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAcceptanceGrade()).asObject());
		TWColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTawjihiWeight()).asObject());
		PTColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPlacementWeight()).asObject());

		tableView2.getColumns().addAll(NameColumn, AGColumn, TWColumn, PTColumn);
		tableView2.setItems(observableList2);

		Button delete = new Button("Delete");
		delete.setStyle("-fx-background-color: green; -fx-text-fill: #FFFFFF; -fx-font-weight: bold;");
		delete.setOnAction(e -> {
			Dnode selectedMajor = tableView2.getSelectionModel().getSelectedItem();  
			if (selectedMajor != null) {
				MajorList.remove(selectedMajor.getName());  
				observableList2.remove(selectedMajor);
				tableView2.refresh(); 

			} else {
				showAlert1("Please select a major to delete.");
			}
		});

		Button back = new Button("Back");
		back.setStyle("-fx-background-color: #F44336; -fx-text-fill: #FFFFFF; -fx-font-weight: bold;");
		back.setOnAction(e -> {
			primaryStage.setScene(Scene2(primaryStage));
		});

		HBox hbox$ = new HBox(60, back, delete);
		hbox$.setAlignment(Pos.BOTTOM_CENTER);
		VBox vbox = new VBox(10, lb, lb2, tableView2, hbox$);
		vbox.setAlignment(Pos.TOP_CENTER);
		Scene scene = new Scene(vbox, 1000, 500);

		primaryStage.setScene(scene);
		primaryStage.show();
	}
	//***************************************************************************************************

	private void searchMajor(Stage primaryStage) {
		Label lb = new Label("Search for a major");
		lb.setFont(new Font(20));
		TextField TF1 = new TextField();
		TF1.setPromptText("Major name");

		Label lb2 = new Label("Enter the Major name");
		lb2.setFont(new Font(15));

		TextArea TA = new TextArea();

		HBox hbox$ = new HBox(TA);
		hbox$.setAlignment(Pos.TOP_CENTER);

		HBox hbox_ = new HBox(TF1);
		hbox_.setAlignment(Pos.TOP_CENTER);

		Button search = new Button("Search");
		search.setStyle("-fx-background-color: green; -fx-text-fill: #FFFFFF; -fx-font-weight: bold;");
		search.setOnAction(e -> {
			String major = TF1.getText();

			if (major.isEmpty()) {
				showAlert1("Please enter the major name.");
				TA.clear();
			} else {
				Dnode n = MajorList.search(major);
				if (n != null) {
					TA.setText(n.toString());
					showAlert2("Major Found");
				} else {
					showAlert2("Failed to find major " + major);
					TA.clear();
				}
			}

			TF1.clear();
		});

		Button back = new Button("Back");
		back.setStyle("-fx-background-color: #F44336; -fx-text-fill: #FFFFFF; -fx-font-weight: bold;");
		back.setOnAction(e -> {
			primaryStage.setScene(Scene2(primaryStage));
		});

		HBox hbox = new HBox(60, back, search);
		hbox.setAlignment(Pos.BOTTOM_CENTER);
		VBox vbox = new VBox(10, lb, lb2, hbox_, hbox$, hbox);
		vbox.setAlignment(Pos.TOP_CENTER);

		Scene scene = new Scene(vbox, 1000, 500);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	//***************************************************************************************************
	private void insertStudent(Stage primaryStage) {
		Label lb = new Label("Insert a new Student");
		lb.setFont(new Font(20));
		HBox hboxlb = new HBox(lb);
		hboxlb.setAlignment(Pos.CENTER);

		TextField TF1 = new TextField();
		TF1.setPromptText("Student id");
		HBox hbox1 = new HBox(TF1);
		hbox1.setAlignment(Pos.TOP_CENTER);

		TextField TF2 = new TextField();
		TF2.setPromptText("Student Name");
		HBox hbox2 = new HBox(TF2);
		hbox2.setAlignment(Pos.TOP_CENTER);

		TextField TF3 = new TextField();
		TF3.setPromptText("Tawjihi Grade");
		HBox hbox3 = new HBox(TF3);
		hbox3.setAlignment(Pos.TOP_CENTER);

		TextField TF4 = new TextField();
		TF4.setPromptText("Placement Test Grade");
		HBox hbox4 = new HBox(TF4);
		hbox4.setAlignment(Pos.TOP_CENTER);

		TextField TF5 = new TextField();
		TF5.setPromptText("Chosen Major");
		HBox hbox5 = new HBox(TF5);
		hbox5.setAlignment(Pos.TOP_CENTER);

		VBox vbox = new VBox(hbox1, hbox2, hbox3, hbox4, hbox5);
		vbox.setAlignment(Pos.TOP_CENTER);

		ComboBox<String> majorSuggestions = new ComboBox<>();
		majorSuggestions.setPromptText("Choose major");

		Button insert = new Button("Insert");
		insert.setStyle("-fx-background-color: green; -fx-text-fill: #FFFFFF; -fx-font-weight: bold;");
		insert.setOnAction(e -> {
		    if (TF1.getText().isEmpty() || TF2.getText().isEmpty() || TF3.getText().isEmpty() || TF4.getText().isEmpty() || TF5.getText().isEmpty()) {
		        showAlert1("Please fill in all fields.");
		        return;
		    }
		    try {
		        int ID = Integer.parseInt(TF1.getText());

		        boolean idExists = false;
		        for (Snode student : observableList1) {
		            if (student.getId() == ID) {
		                idExists = true;
		                break;
		            }
		        }

		        if (idExists) {
		            showAlert3("This ID is already taken. Please enter a different ID.");
		            return; 
		        }

		        String Name = TF2.getText();
		        double TawjihiGrade = Double.parseDouble(TF3.getText());
		        double PlacementTestGrade = Double.parseDouble(TF4.getText());
		        String Major = TF5.getText();

		        // Validate Tawjihi Grade
		        if (TawjihiGrade <= 0 || TawjihiGrade >= 100) {
		            showAlert1("Tawjihi grade must be greater than 0 and less than 100.");
		            return;
		        }

		        // Validate Placement Test Grade
		        if (PlacementTestGrade <= 0 || PlacementTestGrade >= 100) {
		            showAlert1("Placement test grade must be greater than 0 and less than 100.");
		            return;
		        }

		        Snode newStudent = new Snode(ID, Name, TawjihiGrade, PlacementTestGrade, Major);
		        Dnode majorNode = MajorList.search(Major);

		        // Check if the major exists
		        if (majorNode != null) {
		            double admissionMark = newStudent.calculateAdmissionMark(majorNode.getTawjihiWeight(), majorNode.getPlacementWeight());
		            newStudent.setAdmissionMark(admissionMark);

		            String suggestedMajors = suggestMajorForStudent(newStudent);
		            majorSuggestions.getItems().clear();

		            if (!suggestedMajors.equals("No majors available based on the student's marks.")) {
		                for (String major : suggestedMajors.split("\n")) {
		                    majorSuggestions.getItems().add(major.trim());
		                }
		                observableList1.add(newStudent);            
		                tableView1.refresh();
		            } else {
		                showAlert1("There is no major that matches your admission mark.");
		                majorSuggestions.getItems().add("No suitable majors found.");
		            }
		        } else {
		            showAlert1("Major Not Found: choose from suggested majors.");
		            String suggestedMajors = suggestMajorForStudent(newStudent);
		            observableList1.add(newStudent);            
		            majorSuggestions.getItems().clear();

		            if (!suggestedMajors.equals("No majors available based on the student's marks.")) {
		                for (String major : suggestedMajors.split("\n")) {
		                    majorSuggestions.getItems().add(major.trim());
		                }
		            } else {
		                majorSuggestions.getItems().add("No suitable majors found.");
		            }
		        }
		    } catch (NumberFormatException ex) {
		        showAlert1("Please enter valid grades.");
		    }
		});

		Button back = new Button("Back");
		back.setStyle("-fx-background-color: #F44336; -fx-text-fill: #FFFFFF; -fx-font-weight: bold;");
		back.setOnAction(e -> {
			primaryStage.setScene(Scene2(primaryStage));
		});

		Button confirmButton = new Button("Confirm");
		confirmButton.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
		confirmButton.setOnAction(e -> {
			try {
				int ID = Integer.parseInt(TF1.getText());
				String chosenMajor = majorSuggestions.getValue(); 

				if (chosenMajor != null && !chosenMajor.isEmpty()) {
					// Find the confirmed student in the observable list
					Snode confirmedStudent = null;
					for (Snode student : observableList1) {
						if (student.getId() == ID) {
							confirmedStudent = student;
							break;
						}
					}

					if (confirmedStudent != null) {
						confirmedStudent.setMajor(chosenMajor);
						Dnode confirmedMajorNode = MajorList.search(chosenMajor);

						if (confirmedMajorNode != null) {
							confirmedMajorNode.getStudentList().add(confirmedStudent, confirmedMajorNode);
							tableView1.refresh(); 
							showAlert2("Major confirmed and updated to: " + chosenMajor);
						} else {
							showAlert3("Major not found in the major list.");
						}
					} else {
						showAlert3("Student not found.");
					}
				} else {
					showAlert1("Please select a major from the suggested list.");
				}
				TF1.clear();
				TF2.clear();
				TF3.clear();
				TF4.clear();
				TF5.clear();
				majorSuggestions.getSelectionModel().clearSelection();
			} catch (NumberFormatException ex) {
				showAlert1("Please enter a valid student ID.");
			}
		});

		HBox hbox_Insert = new HBox (insert);
		hbox_Insert.setAlignment(Pos.BOTTOM_CENTER);

		HBox hboxButtons = new HBox(60, back, confirmButton);
		hboxButtons.setAlignment(Pos.BOTTOM_CENTER);

		VBox mainLayout = new VBox(20, hboxlb, vbox, hbox_Insert, majorSuggestions ,hboxButtons);
		mainLayout.setAlignment(Pos.TOP_CENTER);

		Scene scene = new Scene(mainLayout, 1000, 500);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	//*****************************************************************************************************
	public void checkAdmission(Stage primaryStage) {
		Label lb = new Label("Check Admission");
		HBox hboxlb = new HBox(lb);
		lb.setFont(Font.font("Times New Roman", 20)); 
		hboxlb.setAlignment(Pos.CENTER);

		TextField studentIdField = new TextField();
		studentIdField.setPromptText("Enter Student ID");

		Button checkButton = new Button("Check");
		ComboBox<String> majorSuggestions = new ComboBox<>();
		majorSuggestions.setPromptText("Suggested Majors");

		checkButton.setOnAction(e -> {
			try {
				int id = Integer.parseInt(studentIdField.getText().trim());
				Snode student = searchInAllMajors(MajorList, id);

				if (student != null) {
					Dnode currentMajorNode = findMajorNode(MajorList, student.getMajor()); // Find the major node by major name

					if (currentMajorNode != null) {
						double admissionMark = student.calculateAdmissionMark(currentMajorNode.getTawjihiWeight(), currentMajorNode.getPlacementWeight());

						if (admissionMark >= currentMajorNode.getAcceptanceGrade()) {
							showAlert2("The student has been accepted into their chosen major: " + currentMajorNode.getName());
						} else {
							showAlert1("The student has not been accepted into their chosen major.");
						}

						String suggestedMajors = suggestMajorForStudent(student);
						majorSuggestions.getItems().clear();

						if (!suggestedMajors.equals("No majors available based on the student's marks.")) {
							for (String major : suggestedMajors.split("\n")) {
								majorSuggestions.getItems().add(major);
							}
						} else {
							majorSuggestions.getItems().add("No suitable majors found.");
						}
					} else {
						showAlert3("The student's major was not found in the major list.");
					}
				} else {
					showAlert3("Student not found.");
					majorSuggestions.getItems().clear();
					majorSuggestions.getItems().add("Student not found.");
				}
			} catch (NumberFormatException ex) {
				showAlert1("Invalid input. Please enter a valid Student ID.");
			}
		});
		HBox hbox = new HBox(10, studentIdField, checkButton);
		hbox.setAlignment(Pos.CENTER);

		Button confirmButton = new Button("Confirm");
		confirmButton.setStyle("-fx-background-color: green; -fx-text-fill: #FFFFFF; -fx-font-weight: bold;");
		confirmButton.setOnAction(e -> {
			try {
				int ID = Integer.parseInt(studentIdField.getText());
				String chosenMajor = majorSuggestions.getValue(); 

				if (chosenMajor != null && !chosenMajor.isEmpty()) {
					// Find the confirmed student in the observable list
					Snode confirmedStudent = null;
					for (Snode student : observableList1) {
						if (student.getId() == ID) {
							confirmedStudent = student;
							break;
						}
					}

					if (confirmedStudent != null) {
						confirmedStudent.setMajor(chosenMajor);
						Dnode confirmedMajorNode = MajorList.search(chosenMajor);

						if (confirmedMajorNode != null) {
							confirmedMajorNode.getStudentList().add(confirmedStudent, confirmedMajorNode);
							tableView1.refresh(); 
							showAlert2("Major confirmed and updated to: " + chosenMajor);

						} else {
							showAlert3("Major not found in the major list.");
						}
					} else {
						showAlert3("Student not found.");
					}
				} else {
					showAlert1("Please select a major from the suggested list.");
				}

				studentIdField.clear();
				majorSuggestions.getSelectionModel().clearSelection();
			} catch (NumberFormatException ex) {
				showAlert1("Please enter a valid student ID.");
			}
		});

		Button backButton = new Button("Back");
		backButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: #FFFFFF; -fx-font-weight: bold;");
		backButton.setOnAction(e -> primaryStage.setScene(Scene2(primaryStage)));

		HBox hboxButtons = new HBox(40, backButton, confirmButton);
		hboxButtons.setAlignment(Pos.CENTER);

		VBox vbox = new VBox(35, hboxlb, hbox, majorSuggestions, hboxButtons);
		vbox.setAlignment(Pos.CENTER);
		Scene scene = new Scene(vbox, 1000, 500);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	//******************************************************************************************************
	private void updateStudent(Stage primaryStage) {
		tableView1.getColumns().clear();

		Label lb = new Label("Update Student info");
		lb.setFont(new Font(20));
		HBox hboxlb = new HBox(lb);

		TextField TF1 = new TextField();
		TF1.setPromptText("Current Student id");

		TextField TF2 = new TextField();
		TF2.setPromptText("Current Student name");

		TextField TF3 = new TextField();
		TF3.setPromptText("Current Tawjihi grade");

		TextField TF4 = new TextField();
		TF4.setPromptText("Current Placement Test grade");

		TextField TF5 = new TextField();
		TF5.setPromptText("Current Chosen major");

		TextField TF6 = new TextField();
		TF6.setPromptText("New Student id");

		TextField TF7 = new TextField();
		TF7.setPromptText("New Student name");

		TextField TF8 = new TextField();
		TF8.setPromptText("New Tawjihi grade");

		TextField TF9 = new TextField();
		TF9.setPromptText("New Placement Test grade");

		TextField TF10 = new TextField();
		TF10.setPromptText("New chosen major");

		HBox hbox1 = new HBox(10, TF1, TF6);
		hbox1.setAlignment(Pos.TOP_CENTER);

		HBox hbox2 = new HBox(10, TF2, TF7);
		hbox2.setAlignment(Pos.TOP_CENTER);

		HBox hbox3 = new HBox(10, TF3, TF8);
		hbox3.setAlignment(Pos.TOP_CENTER);

		HBox hbox4 = new HBox(10, TF4, TF9);
		hbox4.setAlignment(Pos.TOP_CENTER);

		HBox hbox5 = new HBox(10, TF5, TF10);
		hbox5.setAlignment(Pos.TOP_CENTER);

		VBox vbox$ = new VBox(hbox1, hbox2, hbox3, hbox4, hbox5);

		TableColumn<Snode, Integer> IDColumn = new TableColumn<>("ID");
		TableColumn<Snode, String> NameColumn = new TableColumn<>("Name");
		TableColumn<Snode, Double> TWColumn = new TableColumn<>("Tawjihi Grade");
		TableColumn<Snode, Double> PTColumn = new TableColumn<>("Placement Test Grade");
		TableColumn<Snode, String> MajorColumn = new TableColumn<>("Chosen Major");
		TableColumn<Snode, Double> Admissionmark = new TableColumn<>("Admission mark");


		IDColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
		NameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
		TWColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTawjihiGrade()).asObject());
		PTColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPlacementTestGrade()).asObject());
		MajorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMajor()));
		Admissionmark.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getAdmissionMark()).asObject());


		tableView1.getColumns().addAll(IDColumn, NameColumn, TWColumn, PTColumn, MajorColumn ,Admissionmark );

		tableView1.setItems(observableList1);

		Button update = new Button("Update");
		update.setStyle("-fx-background-color: green; -fx-text-fill: #FFFFFF; -fx-font-weight: bold;");
		update.setAlignment(Pos.BOTTOM_RIGHT);
		update.setOnAction(e -> {
		    try {
		        int id = Integer.parseInt(TF1.getText().trim());
		        String currentName = TF2.getText().trim();
		        String tawjihiGradeStr = TF3.getText().trim();
		        String placementTestGradeStr = TF4.getText().trim();
		        String CurrMajor = TF5.getText().trim(); 
		        int newId = Integer.parseInt(TF6.getText().trim()); 
		        double tawjihiGrade = Double.parseDouble(TF8.getText());
		        double placementTestGrade = Double.parseDouble(TF9.getText());

		        if (TF1.getText().trim().isEmpty() || currentName.isEmpty() || tawjihiGradeStr.isEmpty() || 
		                placementTestGradeStr.isEmpty() || CurrMajor.isEmpty() || TF6.getText().trim().isEmpty()) {
		            showAlert1("Please fill all current information fields."); 
		            return;
		        }

		        // Validate Tawjihi Grade
		        if (tawjihiGrade <= 0 || tawjihiGrade >= 100) {
		            showAlert1("New Tawjihi grade must be greater than 0 and less than 100.");
		            return;
		        }

		        // Validate Placement Test Grade
		        if (placementTestGrade <= 0 || placementTestGrade >= 100) {
		            showAlert1("New Placement test grade must be greater than 0 and less than 100.");
		            return;
		        }

		        // Search for the student in all majors
		        Snode studentToUpdate = searchInAllMajors(MajorList, id);
		        if (studentToUpdate != null) {
		            // Check for new ID duplication
		            boolean idExists = searchInAllMajors(MajorList, newId) != null;
		            if (idExists) {
		                showAlert3("This ID is already taken. Enter a different ID.");
		                return; 
		            }

		            Dnode majorNode = findMajorNode(MajorList, TF10.getText());
		            if (majorNode != null) {
		                double tawjihiWeight = majorNode.getTawjihiWeight();
		                double placementWeight = majorNode.getPlacementWeight();

		                double admissionMark = (tawjihiGrade * tawjihiWeight) + (placementTestGrade * placementWeight);

		                studentToUpdate.setId(newId);
		                studentToUpdate.setName(TF7.getText().trim());
		                studentToUpdate.setTawjihiGrade(tawjihiGrade);
		                studentToUpdate.setPlacementTestGrade(placementTestGrade);
		                studentToUpdate.setMajor(TF10.getText().trim());
		                studentToUpdate.setAdmissionMark(admissionMark); 
		                tableView1.refresh();
		                showAlert2("Student info updated. Check the student admission.");

		            } else {
		                showAlert3("Major not found. The update will not be performed.");
		                TF10.clear();
		                return; 
		            }
		        } else {
		            showAlert3("Student not found with the given ID.");
		        }

		    } catch (NumberFormatException ex) {
		        showAlert1("Invalid input. Please enter valid numbers.");
		    }
		});


		Button back = new Button("Back");
		back.setStyle("-fx-background-color: #F44336; -fx-text-fill: #FFFFFF; -fx-font-weight: bold;");
		back.setAlignment(Pos.BOTTOM_LEFT);
		back.setOnAction(e -> {
			primaryStage.setScene(Scene2(primaryStage)); 
		});

		HBox hbox = new HBox(60, back, update);
		hbox.setAlignment(Pos.BOTTOM_CENTER);
		VBox vbox = new VBox(10, lb, hboxlb, vbox$,tableView1 ,hbox);
		vbox.setAlignment(Pos.TOP_CENTER);
		Scene scene = new Scene(vbox, 1000, 500);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	//****************************************************************************************************
	private void deleteStudent(Stage primaryStage) {
		Label lb = new Label("Delete Student");
		lb.setFont(new Font(30));

		Label lb1 = new Label("Insert student ID");
		lb1.setFont(new Font(20));
		TextField TF1 = new TextField();
		TF1.setPromptText("Student ID");
		HBox hbox1 = new HBox(TF1);
		hbox1.setAlignment(Pos.CENTER);
		tableView1.getColumns().clear();

		TableColumn<Snode, Integer> IDColumn = new TableColumn<>("ID");
		TableColumn<Snode, String> NameColumn = new TableColumn<>("Name");
		TableColumn<Snode, Double> TWColumn = new TableColumn<>("Tawjihi Grade");
		TableColumn<Snode, Double> PTColumn = new TableColumn<>("Placement Test Grade");
		TableColumn<Snode, String> MajorColumn = new TableColumn<>("Chosen Major");
		TableColumn<Snode, Double> Admissionmark = new TableColumn<>("Admission Mark");


		IDColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
		NameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
		TWColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTawjihiGrade()).asObject());
		PTColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPlacementTestGrade()).asObject());
		MajorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMajor()));
		Admissionmark.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getAdmissionMark()).asObject());


		tableView1.getColumns().addAll(IDColumn, NameColumn, TWColumn, PTColumn, MajorColumn, Admissionmark );

		tableView1.setItems(observableList1);

		Button delete = new Button("Delete");
		delete.setStyle("-fx-background-color: green; -fx-text-fill: #FFFFFF; -fx-font-weight: bold;");

		delete.setOnAction(e -> {
			try {
				int id = Integer.parseInt(TF1.getText().trim());
				boolean studentFound = false;

				Snode studentToRemove = searchInAllMajors(MajorList, id); 

				if (studentToRemove != null) {
					Dnode currentMajorNode = MajorList.getFirst();

					while (currentMajorNode != null) {
						Snode removedStudent = currentMajorNode.getStudentList().remove(studentToRemove);

						if (removedStudent != null) { 
							studentFound = true;
							break;
						}

						currentMajorNode = currentMajorNode.getNext();
					}

					if (studentFound) {
						showAlert2("Student deleted successfully.");
						observableList1.removeIf(student -> student.getId() == id);
						tableView1.refresh();
					} else {
						showAlert3("Student ID not found in the major.");
					}
				} else {
					showAlert3("Student ID not found.");
				}
			} catch (NumberFormatException ex) {
				showAlert3("Invalid student ID. Please enter a valid number.");
			}

			TF1.clear();
		});


		Button back = new Button("Back");
		back.setStyle("-fx-background-color: #F44336; -fx-text-fill: #FFFFFF; -fx-font-weight: bold;");
		back.setOnAction(e -> {
			primaryStage.setScene(Scene2(primaryStage));
		});

		HBox hbox$ = new HBox(60, back, delete);
		hbox$.setAlignment(Pos.CENTER);
		VBox vbox = new VBox(10, lb, lb1, hbox1,tableView1, hbox$);
		vbox.setAlignment(Pos.CENTER);
		Scene scene = new Scene(vbox, 1000, 500);

		primaryStage.setScene(scene);
		primaryStage.show();
	}
	//******************************************************************************************************
	private void searchStudent(Stage primaryStage) {
		Label lb = new Label("Search for a student");
		lb.setFont(new Font(30));
		Label lb1 = new Label("Insert the student ID");
		lb1.setFont(new Font(20));
		TextField TF1 = new TextField();
		TextArea TA = new TextArea();
		TA.setEditable(false); 
		HBox hbox$ = new HBox(TA);
		hbox$.setAlignment(Pos.CENTER);
		TF1.setPromptText("Student ID");

		HBox HBOX = new HBox(TF1);
		HBOX.setAlignment(Pos.CENTER);

		Button Search = new Button("Search");
		Search.setStyle("-fx-background-color: green; -fx-text-fill: #FFFFFF; -fx-font-weight: bold;");
		Search.setOnAction(e -> {
			StringBuilder studentInfo = new StringBuilder(); 
			boolean studentFound = false;

			try {
				int id = Integer.parseInt(TF1.getText().trim()); 

				Snode student = searchInAllMajors(MajorList, id);   

				if (student != null) {
					studentFound = true;
					studentInfo.append("Student ID: ").append(student.getId()).append("\n")
					.append("Name: ").append(student.getName()).append("\n")
					.append("Tawjihi Grade: ").append(student.getTawjihiGrade()).append("\n")
					.append("Placement Test Grade: ").append(student.getPlacementTestGrade()).append("\n")
					.append("Chosen Major: ").append(student.getMajor()).append("\n")
					.append("Admission Mark: ").append(student.getAdmissionMark()).append("\n");
				}

				if (studentFound) {
					TA.setText(studentInfo.toString()); 
				} else {
					showAlert3("The student with ID " + id + " was not found.");
					TF1.clear();
					TA.clear(); 
				}

			} catch (NumberFormatException ex) {
				showAlert1("Please enter a valid student ID.");
			}
		});

		Button back = new Button("Back");
		back.setStyle("-fx-background-color: #F44336; -fx-text-fill: #FFFFFF; -fx-font-weight: bold;");
		back.setOnAction(e -> primaryStage.setScene(Scene2(primaryStage)));

		HBox hbox = new HBox(60, back, Search);
		hbox.setAlignment(Pos.BOTTOM_CENTER);
		VBox vbox = new VBox(10, lb, lb1, HBOX, hbox$, hbox);
		vbox.setAlignment(Pos.CENTER);
		Scene scene = new Scene(vbox, 1000, 500);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	//*************************************************************************************************
	public String suggestMajorForStudent(Snode student) {
		StringBuilder majorsList = new StringBuilder();
		Dnode currentMajorNode = MajorList.getFirst(); 

		if (currentMajorNode != null) {
			do {
				double admissionMark = student.calculateAdmissionMark(currentMajorNode.getTawjihiWeight(), currentMajorNode.getPlacementWeight());

				if (admissionMark >= currentMajorNode.getAcceptanceGrade()) {
					if (majorsList.length() == 0) {
						majorsList.append("The major that suit your admission mark is:\n");
					}
					majorsList.append(currentMajorNode.getName()).append("\n");
				}

				currentMajorNode = currentMajorNode.getNext();
			} while (currentMajorNode != MajorList.getFirst()); 
		}

		return majorsList.length() > 0 ? majorsList.toString() : "No majors available based on the student's marks.";
	}
	//*****************************************************************************************************
	public void NextMajor() {
		if (dnode != null) {
			dnode = dnode.getNext(); // Move to the next node
			if (dnode == null) { // If at the end, loop to the first node
				dnode = MajorList.getFirst(); // Ensure MajorList has a method to get the first node
			}
			updateMajorInfo(); // Update TextArea with the current major info
		}
	}


	public void PreviousMajor() {
		if (dnode != null) {
			dnode = dnode.getPrev(); // Move to the previous node
			if (dnode == null) { // If at the beginning, loop to the last node
				dnode = MajorList.getLastDoubleNode(); // Ensure MajorList has a method to get the last node
			}
			updateMajorInfo(); // Update TextArea with the current major info
		}
	}
	//******************************************************************************************************
	private void updateMajorInfo() {
		if (dnode != null) {
			StringBuilder majorInfo = new StringBuilder();
			majorInfo.append("Major: ").append(dnode.getName()).append("\n")
			.append("Acceptance Grade: ").append(dnode.getAcceptanceGrade()).append("\n")
			.append("Tawjihi Weight: ").append(dnode.getTawjihiWeight()).append("\n")
			.append("Placement Weight: ").append(dnode.getPlacementWeight()).append("\n")
			.append("*******************************************\n")
			.append(">>Students in this Major<<\n")
			.append("                           \n");
			Snode currentStudent = dnode.getStudentList().getFirst();

			if (currentStudent == null) {
				majorInfo.append("No students in this major.\n");
			} else {
				Snode startStudent = currentStudent;

				do {
					majorInfo.append("ID: ").append(currentStudent.getId()).append(", ")
					.append("Name: ").append(currentStudent.getName()).append(", ")
					.append("Tawjihi Grade: ").append(currentStudent.getTawjihiGrade()).append(", ")
					.append("Placement Test Grade: ").append(currentStudent.getPlacementTestGrade()).append("\n");

					currentStudent = currentStudent.getNext();

					if (currentStudent == null) {
						currentStudent = dnode.getStudentList().getFirst();
					}

				} while (currentStudent != startStudent); 
			}

			TA.setText(majorInfo.toString()); 
		}
	}
	//***************************************************************************************************

	public void saveupdates(Stage primaryStage) {
		primaryStage.setTitle("Save updates");
		Image image = new Image("file:///C:/Users/user/Desktop/uni.jpg");
		ImageView imageView = new ImageView(image);
		imageView.setFitWidth(1000);
		imageView.setPreserveRatio(true);

		Button saveMajors = new Button("Save updates to Criteria file");
		saveMajors.setOnAction(e -> {
			   MajorList.saveMajorsToFile();
		});

		Button saveStudents = new Button("Save updates to Students file");
		saveStudents.setOnAction(e -> {
			   MajorList.saveStudentsToFile();
		});

		Button Back = new Button("Back");
		Back.setOnAction(e -> primaryStage.setScene(Scene2(primaryStage)));

		HBox hbox = new HBox(15, Back, saveStudents, saveMajors);
		hbox.setAlignment(Pos.CENTER);
		VBox vbox = new VBox(10, imageView, hbox);
		Scene scene = new Scene(vbox, 1000, 500);
		primaryStage.setScene(scene);
		primaryStage.show();
	} 
	//*************************************************************************************************
	public Snode searchInAllMajors(DLL majorList, int id) {
		Dnode currentMajorNode = majorList.getFirst(); 

		if (currentMajorNode == null) {
			return null; 
		}

		do {
			Snode student = findStudentNode(currentMajorNode.getStudentList(), id);
			if (student != null) {
				return student; 
			}
			currentMajorNode = currentMajorNode.getNext(); 
		} while (currentMajorNode != majorList.getFirst()); 

		return null; 
	}

	//*************************************************************************************************

	public void MajorTableview ( Stage primaryStage) {
		tableView2.getColumns().clear();

		TableColumn<Dnode, String> NameColumn = new TableColumn<>("Name");
		TableColumn<Dnode, Integer> AGColumn = new TableColumn<>("Acceptance Grade");
		TableColumn<Dnode, Double> TWColumn = new TableColumn<>("Tawjihi Weight");
		TableColumn<Dnode, Double> PTColumn = new TableColumn<>("Placement Test Weight");

		NameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
		AGColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAcceptanceGrade()).asObject());
		TWColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTawjihiWeight()).asObject());
		PTColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPlacementWeight()).asObject());

		tableView2.getColumns().addAll(NameColumn, AGColumn, TWColumn, PTColumn);
		tableView2.setItems(observableList2);

		Button back = new Button("Back");
		back.setStyle("-fx-background-color: Black; -fx-text-fill: white;");

		VBox vbox$ = new VBox(15, tableView2, back);
		vbox$.setAlignment(Pos.TOP_CENTER);

		back.setOnAction(e -> {
			primaryStage.setScene(Scene2(primaryStage));
		});

		Scene scene1 = new Scene(vbox$, 1000, 500);
		primaryStage.setScene(scene1);
		primaryStage.show();
	}
	//***********************************************************************************************
	public void showStatistics(Stage primaryStage) {
		Label titleLabel = new Label("General Statistics of Majors");
		titleLabel.setFont(new Font(24));
		HBox titleBox = new HBox(titleLabel);
		titleBox.setAlignment(Pos.CENTER);

		StatTA.setEditable(false);
		StatTA.setWrapText(true);
		StatTA.setPrefSize(600, 200);

		Button loadStatsButton = new Button("Load Statistics");
		loadStatsButton.setOnAction(e -> loadStatistics());

		// Button to save statistics to file
		Button saveToFileButton = new Button("Print to File");
		HBox saveBox = new HBox(saveToFileButton);
		saveBox.setAlignment(Pos.CENTER);
		saveToFileButton.setOnAction(e -> {
			printToFile();
		});

		Button backButton = new Button("Back");
		backButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: #FFFFFF; -fx-font-weight: bold;");
		backButton.setOnAction(e -> primaryStage.setScene(Scene2(primaryStage)));

		Label topNLabel = new Label("Top N Accepted Students:");
		TextField topNField = new TextField();
		topNField.setPrefWidth(50);

		Button loadTopNButton = new Button("Get Top N");
		loadTopNButton.setOnAction(e -> loadTopNStudents(topNField.getText()));

		HBox topNBox = new HBox(10, topNLabel, topNField, loadTopNButton);
		topNBox.setAlignment(Pos.CENTER);

		HBox buttonBox = new HBox(10, backButton, loadStatsButton);
		buttonBox.setAlignment(Pos.CENTER);

		VBox mainLayout = new VBox(20, titleBox, majorComboBox, buttonBox, topNBox, StatTA, saveBox);
		mainLayout.setAlignment(Pos.TOP_CENTER);
		mainLayout.setPadding(new Insets(20));

		Scene scene = new Scene(mainLayout, 1000, 500);
		primaryStage.setScene(scene);
		primaryStage.setTitle("General Statistics");
		primaryStage.show();
	}
	//**********************************************************************************************
	private void loadStatistics() {
		String selectedMajor = majorComboBox.getValue();
		StringBuilder statsBuilder = new StringBuilder();

		if (selectedMajor != null) {
			String majorStatistics = MajorList.calculateStatisticsForEachMajor(selectedMajor);
			statsBuilder.append("Statistics for Major: ").append(selectedMajor).append("\n")
			.append(majorStatistics).append("\n");
		} else {
			statsBuilder.append("No major selected.\n");
		}

		String generalStatistics = MajorList.calculateAllStatistics();
		statsBuilder.append("General Statistics:\n").append(generalStatistics);

		StatTA.setText(statsBuilder.toString());
	}
//****************************************************************************************************
	private void loadTopNStudents(String nValue) {
		try {
			int topN = Integer.parseInt(nValue.trim());
			String selectedMajor = majorComboBox.getValue();

			if (selectedMajor == null) {
				StatTA.setText("Please select a major."); 
				return;
			}

			Dnode majorNode =MajorList .search(selectedMajor); 
			if (majorNode == null) {
				StatTA.setText("Major not found."); 
				return;
			}

			String topNStudents = MajorList.getTopNStudentsForMajor(topN, selectedMajor);
			StatTA.setText("Top " + topN + " Accepted Students for Major: " + selectedMajor + "\n" + topNStudents);
		} catch (NumberFormatException e) {
			StatTA.setText("Please enter a valid number for Top N.");
		}
	}
//*****************************************************************************************************	
	//print statistics to file 
	public void printToFile() {
		String filePath = "C:\\Users\\user\\Desktop\\project\\Statistics.txt";

		try (PrintWriter writer = new PrintWriter(new File(filePath))) {
			String content = StatTA.getText(); 

			writer.println(content);

			showAlert2("Statistics printed to the file ");
		} catch (IOException e) {
			showAlert3("Error writing to file: " + e.getMessage());
		}
	}

	//*************************************************************************************************
	public static void main(String[] args) {
		launch(args);
	}
}

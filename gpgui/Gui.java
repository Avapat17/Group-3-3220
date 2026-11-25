package com.groupproject.gpgui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.concurrent.Task;
import javafx.scene.control.Label;


import java.io.IOException;


public class Gui extends Application {

    private final ReceptionistDatabase db = new ReceptionistDatabase(); // call to create table
    private final Receptionist receptionist = new Receptionist(db);

    Scene scene;
    TextField Patient_name = new TextField();
    TextField Patient_phone = new TextField();
    TextField Patient_email = new TextField();
    TextField Patient_address = new TextField();
    TextField Doctor_name = new TextField();
    TextField Patient_ID = new TextField();


   private final Label requiredLabel = new Label("Ready");
    private final Label resultLabel = new Label();

    Button add = new Button("Add New Patient");//add patient
    Button update = new Button("Update Patient");//update patient info
    Button view= new Button ("View Patient"); //View patient stats
    Button search = new Button ("Search Patient");
    Button exit = new Button("Exit");

    private void handleAdd(){
        //read the fields
        //returns user input as a string with whitespaces removed
        String name = Patient_name.getText().trim();
        String phone = Patient_phone.getText().trim();
        String email = Patient_email.getText().trim();
        String address = Patient_address.getText().trim();
        String doctor = Doctor_name.getText().trim();



        //Validating name and phone entry
        //check to see if name and phone field are empty, if so send text requiring it
        if(name.isEmpty()|| phone.isEmpty()){
            requiredLabel.setText("Name and phone are required");
            return;

        }

        //calls Reciptionist.addPatient inside a task
        Task<Integer> task = new Task<Integer>() {
            @Override protected Integer call(){
                return receptionist.registerPatient(
                    name, phone, email, address, doctor);
            }
        };

        //Patients are successfully added , we clear the boxes for the next one
        task.setOnSucceeded(e ->{
            requiredLabel.setText("Patient added. ID: " + task.getValue());
            Patient_name.clear();
            Patient_phone.clear();
            Patient_email.clear();
            Patient_address.clear();
            Doctor_name.clear();
        });

        task.setOnFailed(e->{
            requiredLabel.setText("Error" + task.getException().getMessage());
            task.getException().printStackTrace();
        });

        //run the task
        new Thread(task).start();
    }

    private void handleSearch(){

        String ID = Patient_ID.getText().trim();

        //Validating ID entry
        //check to see if name and phone field are empty, if so send text requiring it
        if(ID.isEmpty()){
            requiredLabel.setText(" ID is required");
            return;

        }

        //validate ID is a number
        int number_ID;
        try {
            number_ID = Integer.parseInt(ID);
        } catch (NumberFormatException e) {
            requiredLabel.setText("ID must be a number.");
            return;
        }

        //calls Receptionist.searchPatient inside a task
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                receptionist.searchPatient(number_ID);
                return null;
            }
        };

        task.setOnSucceeded(e ->{
            requiredLabel.setText("Patient found.");
            Patient_ID.clear();
        });

        task.setOnFailed(e->{
            requiredLabel.setText("Error" + task.getException().getMessage());
            task.getException().printStackTrace();
        });

        new Thread(task).start();

    }

    private void handleUpdate(){

        //read the fields
        //returns user input as a string with whitespaces removed
        String name = Patient_name.getText().trim();
        String ID = Patient_ID.getText().trim();
        String phone = Patient_phone.getText().trim();
        String email = Patient_email.getText().trim();
        String address = Patient_address.getText().trim();
        String doctor = Doctor_name.getText().trim();


        //identify patient by ID, then we can update information
        if(ID.isEmpty()) {
            requiredLabel.setText("ID is required");
            return;
        }

        //validate ID is a number
        int number_ID;
        try {
            number_ID = Integer.parseInt(ID);
        } catch (NumberFormatException e) {
            requiredLabel.setText("ID must be a number.");
            return;
        }

        //calls Receptionist.updatePatient inside a task
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                receptionist.updatePatient(number_ID, name, phone, email, address, doctor);
                return null;
            }
        };

        //Patients are successfully added , we clear the boxes for the next one
        task.setOnSucceeded(e ->{
            requiredLabel.setText("Patient updated.");
            Patient_name.clear();
            Patient_phone.clear();
            Patient_email.clear();
            Patient_address.clear();
            Doctor_name.clear();
        });

        task.setOnFailed(e->{
            requiredLabel.setText("Error" + task.getException().getMessage());
            task.getException().printStackTrace();
        });


        new Thread(task).start();

    }

    @Override
    public void start(Stage stage) throws IOException {

        BorderPane root = new BorderPane();


        //Ui element arrangements
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.add(new Label ("Name:"), 0,0);
        form.add(Patient_name, 1, 0);
        form.add(new Label ("Phone:"), 0,1);
        form.add(Patient_phone, 1, 1);
        form.add(new Label ("Address:"), 0,2);
        form.add(Patient_address, 1, 2);
        form.add(new Label ("ID:"), 0,3);
        form.add(Patient_ID, 1, 3);
        form.add(new Label ("email:"), 0,4);
        form.add(Patient_email, 1, 4);
        form.add(new Label ("Doctor name:"), 0,5);
        form.add(Doctor_name, 1, 5);


        //button and label location
       HBox buttonRow = new HBox(10,add,update,search,exit);
       VBox bottom = new VBox(10, requiredLabel,buttonRow);

        //placing everything
        root.setCenter(form);
       root.setBottom(bottom);

        Scene scene = new Scene(root, 400,300);
        stage.setTitle("Patient Database");
        stage.setScene(scene);

        //Window size Manipulation
        stage.setMaxHeight(1000);
        stage.setMinHeight(200);
        stage.setMaxWidth(900);
        stage.setMinWidth(200);
        //gui controls

        //adds new patient to profile
        add.setOnAction(e -> handleAdd());

        //update Patient info
        update.setOnAction(e -> handleUpdate());

        //search for patient in database
        search.setOnAction(e-> handleSearch());

        //close Window
        exit.setOnAction(e -> stage.close());

       stage.show();
    }

}
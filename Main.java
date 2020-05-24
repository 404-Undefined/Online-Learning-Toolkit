package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.io.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main extends Application {
    Stage window;
    Scene scene1, scene2, scene3;
    String[] meetLinks;

    String classroom="students.csv"; //default classroom
    final int defaultGroupNum = 3;
    int groupNum = defaultGroupNum;

    String groupingPreference = "";

    List<List<String>> groups;
    List<List<Student>> studentsList;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("HomePage");

        //Scene 1 ----------------------------------------------------------------------------------------
        File folder = new File("src/sample/classrooms");
        File[] listOfFiles = folder.listFiles((dir, name) -> !name.equals(".DS_Store")); //add all files except .DS_Store

        ComboBox<String> classrooms = new ComboBox<>();
        classrooms.setPromptText("Please select your classroom");
        for (File file : listOfFiles) {
            classrooms.getItems().add(file.getName());
        }

        //initialise the ListView with the default classroom, students.csv
        ListView<String> listOfStudents = new ListView<>();
        listOfStudents.setPadding(new Insets(8, 0, 0, 8));
        VBox t = new VBox();
        t.getChildren().addAll(listOfStudents);
        t.setPadding(new Insets(0,10,0,10));

        classrooms.setPrefSize(180,10);

        ReadFile p = new ReadFile("src/sample/classrooms/" + classroom);
        for (int i = 1; i < p.readLine(); i++) {
            String studentName = p.read(i, 0);
            String studentGender = p.read(i, 1);
            String studentNationality = p.read(i, 2);
            String studentHomeroom = p.read(i, 3);
            String studentEmail = p.read(i, 4);

            String studentInfo = studentName + " " + studentGender + " " + studentNationality + " " + studentHomeroom + " " + studentEmail;
            listOfStudents.getItems().add(studentInfo); //add the student to the list
        }

        //run if user changes classroom in the dropdown
        classrooms.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            String filePath = "src/sample/classrooms/" + newValue;
            listOfStudents.getItems().clear();
            ReadFile q = new ReadFile(filePath);
            for (int i = 1; i < q.readLine(); i++) { //for each student (excluding the first line because that's the header line name,gender,natinality, etc)
                String studentName = q.read(i, 0);
                String studentGender = q.read(i, 1);
                String studentNationality = q.read(i, 2);
                String studentHomeroom = q.read(i, 3);
                String studentEmail = q.read(i, 4);

                String studentInfo = studentName + " " + studentGender + " " + studentNationality + " " + studentHomeroom + " " + studentEmail;
                listOfStudents.getItems().add(studentInfo); //add the student to the list
            }
        });

        //Label 1 and Button
        Label label1 = new Label("Make Groups!");
        label1.setFont(new Font("Arial", 40));
        label1.setPadding(new Insets(0,0,15,0));



        label1.setAlignment(Pos.CENTER);




        TextField numGroupsTextField = new TextField();
        TextFormatter<Integer> formatter = new TextFormatter<>(new IntegerStringConverter(), defaultGroupNum, c -> Pattern.matches("\\d*", c.getText()) ? c : null );
        numGroupsTextField.setTextFormatter(formatter);
        numGroupsTextField.setPrefSize(65,10);
        HBox hb = new HBox();

        numGroupsTextField.setPromptText("#G");

        hb.setPadding(new Insets(0,0,-10,39));

        HBox justForNum = new HBox();
        justForNum.getChildren().add(numGroupsTextField);

        justForNum.setPadding(new Insets(0,0,0,5));
        hb.getChildren().addAll(classrooms, justForNum);


        RadioButton gender = new RadioButton("Gender Mix");
        RadioButton nationality = new RadioButton("Nationality Mix");
        RadioButton homeroom = new RadioButton("Homeroom Mix");
        RadioButton random = new RadioButton("Random Mix");
        RadioButton manual = new RadioButton("Manual");

//        random.setSelected(true);//set random as default

        Label prefN = new Label("Customization");
        ToggleGroup tg = new ToggleGroup();

        gender.setToggleGroup(tg);
        nationality.setToggleGroup(tg);
        homeroom.setToggleGroup(tg);
        random.setToggleGroup(tg);
        manual.setToggleGroup(tg);

        VBox preference= new VBox();
        preference.setSpacing(20);
        preference.getChildren().addAll(prefN, gender, nationality, homeroom, random, manual);
        preference.setPadding(new Insets(10, 10, 10, 30));


        Button selectClassroomButton = new Button("Generate");

        selectClassroomButton.setPrefSize(250, 50);


        selectClassroomButton.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white; -fx-font-size:24");



        selectClassroomButton.setOnAction(e -> {
            classroom = classrooms.getValue(); //get the value of the combo box
            if(classroom == null) {
                AlertBox.display("Warning", "Please select a classroom!");
                return;
            }
            if(gender.isSelected()){
                groupingPreference = "gender";
            }else if(nationality.isSelected()){
                groupingPreference = "nationality";
            }else if(homeroom.isSelected()){
                groupingPreference = "homeroom";
            }else if(random.isSelected()){
                groupingPreference = "random";
            }else if (manual.isSelected()){
                groupingPreference = "manual";
            }

//            System.out.println(groupingPreference);//printing groupingpreference string
            groupNum = Integer.parseInt(numGroupsTextField.getText());//sending value to groupnum
            changeToScene2(); //change scene on button click
        });

        // -------------------- Layouts --------------------
        //HBox for inner layout - selectClassroomButton & numGroupsTextField
        HBox selectandNum = new HBox();
        selectandNum.setAlignment(Pos.CENTER);
        selectandNum.setSpacing(30);
        selectandNum.getChildren().addAll(selectClassroomButton);
        selectandNum.setPadding(new Insets(0,0,-50,0));

        //VBox for outer layout
        VBox layout1 = new VBox(20);
        layout1.setPadding(new Insets(0,0,15,-10));

//        layout1.setAlignment(Pos.CENTER);

        //stack all objects on top of each other in a column, spaced out by 20 px
        layout1.getChildren().addAll(label1, hb, selectandNum);
        layout1.setAlignment(Pos.CENTER);

        Button getPastGroups = new Button("Use Past Groups");
        getPastGroups.setPrefSize(130,20);
        getPastGroups.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");

        ComboBox<String> pastFiles = new ComboBox<>();
        pastFiles.setPromptText("Access Past Files");

        classrooms.setPromptText("Select past groups");
        try{
            FileInputStream fstream = new FileInputStream("pastgroups.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;
            while ((strLine = br.readLine()) != null)   {
                // Print the content on the console
                System.out.println (strLine);
                pastFiles.getItems().add(strLine);
            }
            fstream.close();
        }catch(IOException e){

        }
        getPastGroups.setOnAction(e -> {
            if(pastFiles.getSelectionModel().getSelectedItem() == null) {
                return;
            }

            String currentFile = pastFiles.getSelectionModel().getSelectedItem();
            groups = new ReadFile().deserialise(currentFile);
            groupingPreference = "random";
            changeToScene2();
            });

        HBox Outter = new HBox();
        Outter.getChildren().addAll(layout1, preference);//top half
        preference.setPadding(new Insets(0, 0,0,0));
        layout1.setPrefWidth(320);


        Outter.setPadding(new Insets(10, 10, 0,10));
        VBox veryOutter = new VBox();
        veryOutter.setSpacing(20);
        HBox bottom = new HBox();
        bottom.getChildren().addAll(getPastGroups, pastFiles);
        bottom.setPadding(new Insets(10,10,10,10));
        bottom.setSpacing(20);
        bottom.setAlignment(Pos.CENTER);
        veryOutter.getChildren().addAll(Outter, t, bottom);
        veryOutter.setStyle("-fx-background-color: BEIGE;");
        scene1 = new Scene(veryOutter, 450, 610);


        window.setScene(scene1);
        window.show();
    }
        //---------------------------------------- Scene 2 ------------------------------------------------------------//
    private List<List<Student>> manualMode(int groupNum, String classroomFileName) {
        Stage manualWindow = new Stage();
        manualWindow.initModality(Modality.APPLICATION_MODAL);
        manualWindow.setTitle("Manual mode");
        manualWindow.setMinWidth(250);

        List<List<Student>> t = new ArrayList<>(); //return: List of List of Student objects
        HashMap<String, Student> nameAndObject = new HashMap<>(); //HashMap of Student Name:Student obj

        ReadFile p = new ReadFile(classroomFileName);
        int numStudents = p.readLine()-1; //-1 because the first line is the CSV header
        String[] studentNames = new String[numStudents];
        for(int i = 1; i < numStudents+1; i++) {
            studentNames[i-1] = p.read(i, 0);

            Student student = new Student(
                    p.read(i, 0),
                    p.read(i, 1),
                    p.read(i, 2),
                    p.read(i, 3),
                    p.read(i, 4)
            );

            nameAndObject.put(p.read(i, 0), student); //addut name:Student key:value pair
        }

        //Layout and scenes
        ChoiceBox<String> studentChoices = new ChoiceBox<>();
        studentChoices.setPrefWidth(110);
        System.out.println("STUDENT NAMES");
        System.out.println(Arrays.toString(studentNames));
        studentChoices.getItems().addAll(studentNames);

        ArrayList<Integer> numPerGroup = new ArrayList<>(); //e.g. for 6 people with 4 groups, [2, 2, 1, 1]
        int lowest = Math.floorDiv(numStudents, groupNum);
        for(int i = 0; i < groupNum; i++) {
            numPerGroup.add(lowest);
        }

        //for 6 people with 4 groups, it's currently [1, 1, 1, 1,] so make [2, 2, 1, 1]
        int remainder = numStudents % groupNum;
        for(int i = 0; i < remainder; i++) {
            numPerGroup.set(i, numPerGroup.get(i)+1); //increment
        }

        Label instruction = new Label("Please choose your student for group #1");
        Button chooseButton = new Button("Choose");

        HBox topp = new HBox();
        topp.getChildren().addAll(studentChoices, chooseButton);

        AtomicInteger groupNumber = new AtomicInteger(0);
        for(int i = 0; i < groupNum; i++) {
            t.add(new ArrayList<>());
        }


        Label[] groupLabels = new Label[groupNum];

        VBox v = new VBox();

        VBox groupsList = new VBox(10);
        for(int i = 0; i < groupNum; i++) {
            Label groupText = new Label("Group #" + (i+1));

            Label membersLabel= new Label("");

            groupsList.getChildren().addAll(groupText, membersLabel);
            groupLabels[i] = membersLabel;
        }



        chooseButton.setOnAction(e -> {
            if(studentChoices.getValue() == null) {
                return;
            }
            String studentName = studentChoices.getValue();

            t.get(groupNumber.get()).add(nameAndObject.get(studentName));

            String currentText = groupLabels[groupNumber.get()].getText();
            groupLabels[groupNumber.get()].setText(currentText + studentName + "\t");

            if(t.get(groupNumber.get()).size() == numPerGroup.get(groupNumber.get())) {
                groupNumber.incrementAndGet(); //move on to the next group
            }

            //update groupLabels

            studentChoices.getItems().remove(studentName); //remove this student from the dropdown

            if(studentChoices.getItems().isEmpty()) { //all students in the dropdown have been added
                manualWindow.close(); //close the window
            } else { //still more students to put
                instruction.setText("Please choose your student for group #" + (groupNumber.get()+1));
                studentChoices.setValue(null); //clear the value of the choicebox
            }
        });

        topp.setPadding(new Insets(10,10,10,10));


        VBox manualVBox = new VBox(10);
        topp.setSpacing(15);
        instruction.setPadding(new Insets(0,0,0,15));
        groupsList.setPadding(new Insets(10,10,10,10));
        Line line = new Line(0, 10, 10000, 10);

        manualVBox.getChildren().addAll(topp, instruction, line, groupsList);
        manualVBox.setStyle("-fx-background-color: BEIGE;");

        Scene manualScene = new Scene(manualVBox, 250, 350);
        manualWindow.setScene(manualScene);
        manualWindow.showAndWait();

        return t;
    }

    public void changeToScene2() {
        String classroomFileName = "src/sample/classrooms/" + classroom;
        OneVariableMix oneVariableMix = new OneVariableMix(classroomFileName);

        if(groupingPreference.equals("manual")) {
            studentsList = manualMode(groupNum, classroomFileName);
        } else {
            studentsList = oneVariableMix.sort(groupingPreference, groupNum);
        }
        groups = oneVariableMix.toEmail(studentsList); //TODO: replace type:
        System.out.println(studentsList);

        //TODO: switch groupnum available on GUI
        TextField teacherEmailField = new TextField();
        teacherEmailField.setPromptText("Enter your email: ");

        PasswordField teacherPasswordField = new PasswordField(); //password
        teacherPasswordField.setPromptText("Enter your password: ");


        //Video call provider checkbox
        RadioButton googleMeet = new RadioButton("Google Meet");
        RadioButton zoom = new RadioButton("Zoom");
        googleMeet.setSelected(true); //select by default


        ToggleGroup group = new ToggleGroup();
        googleMeet.setToggleGroup(group);
        zoom.setToggleGroup(group);//grouping the radio button so that only one can be selected

        TextArea announcement = new TextArea();
        announcement.setPromptText("Type your announcement");
        announcement.setPrefSize(200, 200);
        announcement.setText("");

        //Submit button
        Button submitButton = new Button("Click to generate meets and send invites");
        submitButton.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");//button color test
        submitButton.setPrefSize(250, 40);

        submitButton.setOnAction(e -> {//when button is clicked, create new link and then generate it
            String teacherAddress = teacherEmailField.getText();
            String teacherPassword = teacherPasswordField.getText();

            Email teacherEmail = new Email(teacherAddress, teacherPassword);
            if (teacherEmail.authentiate()) { //check email address and password is valid

                if (googleMeet.isSelected()) { //if the user selected the google mt checklist
                    meetLinks = new MeetingHost(teacherAddress, teacherPassword).generateGoogleMeetLinks(groups.size());
                    for (int i = 0; i < groups.size(); i++) {  //send email to each group
                        System.out.println("Running");
                        teacherEmail.sendEmail(groups.get(i).toArray(new String[0]), "<h1> Please join the <a href='" + meetLinks[i] + "'> Google Meet call </a> </h1>" + "\n" + announcement.getText()); //send each to each group their corresponding meet link
                    }
                    changeToScene3();
                }

                else if (zoom.isSelected()) {
                    meetLinks = new MeetingHost(teacherAddress, teacherPassword).generateZoomLinks(groups.size());
                    for (int i = 0; i < groups.size(); i++) {  //send email to each group
                        teacherEmail.sendEmail(groups.get(i).toArray(new String[0]), "<h1> Please join the <a href='" + meetLinks[i] + "'> Zoom call </a> </h1>" + "\n" + announcement.getText()); //send each to each group their corresponding meet link
                    }
                    changeToScene3();
                }

                else { //user did not select anything
                    AlertBox.display("Warning", "Please select a call option!");
                }
            } else {
                AlertBox.display("Warning", "Invalid email or password");
            }
        });
        //TODO: put it on another thread
        //TODO: make type and groupNum on the GUI

        Button resetClassroomButton = new Button("Choose a different classroom");
        resetClassroomButton.setOnAction(e -> window.setScene(scene1)); //change scene on button click

        resetClassroomButton.setPrefWidth(250);

        VBox layout = new VBox(10);
        layout.setStyle("-fx-background-color: BEIGE;"); //background color
        layout.setPadding(new Insets(30, 10, 20, 10));
        layout.setAlignment(Pos.CENTER); //centre the elements

        teacherEmailField.prefWidth(20);
        teacherEmailField.prefHeight(80);
        teacherPasswordField.prefWidth(20);
        teacherPasswordField.prefHeight(80);

        Label title = new Label("Send Meet Links");

        title.setStyle("-fx-color: #ff6347;");

        title.setAlignment(Pos.CENTER);

        Button viewGroups = new Button("View Groups without sending emails");
        viewGroups.setOnAction(e -> { changeToScene3(); });
        viewGroups.setPrefWidth(250);

        HBox checkboxLayer = new HBox(10);
        checkboxLayer.setAlignment(Pos.CENTER);
        checkboxLayer.getChildren().addAll(googleMeet, zoom);
        checkboxLayer.setPadding(new Insets(10, 10, 10, 10));

        layout.getChildren().addAll(title, teacherEmailField, teacherPasswordField, checkboxLayer, announcement, submitButton, resetClassroomButton, viewGroups);

        scene2 = new Scene(layout, 400, 500);
        window.setScene(scene2); //change scene on button click
    }
        // ------------------------------------------------------------------------------------------------------

    public void changeToScene3() {
        Button goBack = new Button("Home");
        goBack.setOnAction(e -> {
            start(window);
        });

        Label sceneLabel = new Label("Groups");
        sceneLabel.setStyle("-fx-text-fill: white; -fx-font-size:24");
        HBox xx = new HBox();
        xx.getChildren().add(sceneLabel);
        xx.setAlignment(Pos.CENTER);
        Button saveButton = new Button("Save these groups");

        HBox top = new HBox();
        top.getChildren().addAll(saveButton, goBack);

        ListView<String> groupsList = new ListView<>();
        HBox x = new HBox();
        x.getChildren().add(groupsList);
        x.setPadding(new Insets(0,10,0,10));

        
        ArrayList<List<String>> groupSave = new ArrayList<List<String>>();

        for(List<Student> group: studentsList) {
            List<String> studentNames = group.stream().map(s -> s.getName()).collect(Collectors.toList());
            String s = String.join(" ", studentNames);
            groupsList.getItems().add(s);

            groupSave.add(studentNames);
        }

        saveButton.setOnAction(e -> {
            new ReadFile().storeGroups(studentsList, Instant.now().toString());//using the current time
            System.out.println(studentsList);
        });

        sceneLabel.setAlignment(Pos.CENTER);
        top.setAlignment(Pos.CENTER);
        top.setSpacing(20);
        top.setPadding(new Insets(10,10,10,10));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(xx, groupsList, top);
        layout.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
        scene3 = new Scene(layout, 400, 400);
        window.setScene(scene3);
    }
}
package com.example.myapp.controllers;
import com.fasterxml.jackson.core.type.TypeReference;

import com.example.myapp.models.Flashcard;
import com.example.myapp.models.Subject;
import com.example.myapp.models.Topic;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FlashcardMainController {
    @FXML
    private BorderPane root;
    @FXML
    private VBox sidebar;

    @FXML
    private AnchorPane ContentArea;

    @FXML
    private Button addSubjectButton;
    @FXML
    private Button deleteSubjectButton;

    @FXML
    private Button addTopicButton;
    @FXML
    private Button deleteTopicButton;

    @FXML
    private Button subjectButton1;

    @FXML
    private Button subjectButton2;
    @FXML
    private TreeView<String> sidebarTreeView;

    @FXML
    private Button addFlashcardButton;
    @FXML
    private Button deleteFlashcardButton;


    @FXML
    private Button shuffleFlashcardsButton;

    @FXML
    private Button goToHomeButton;

    private List<Subject> subjects = new ArrayList<>();

    private Topic currentTopic;
    private int currentFlashcardIndex = -1;

    public void initialize() {
//        Subject math = new Subject("Math");
//        math.addTopic(new Topic("Algebra"));
//        math.addTopic(new Topic("Geometry"));
//
//        Subject physics = new Subject("Physics");
//        physics.addTopic(new Topic("Mechanics"));
//        physics.addTopic(new Topic("Optics"));
//
//        subjects.clear();
//        subjects.add(math);
//        subjects.add(physics);
        loadData();
        loadSidebarTree();

        addSubjectButton.setOnAction(e -> handleAddSubject());
        deleteSubjectButton.setOnAction(e-> handleDeleteSubject());


        addTopicButton.setOnAction(e -> handleAddTopic());
        deleteTopicButton.setOnAction(e->handleDeleteTopic());


        addFlashcardButton.setOnAction(e -> handleAddFlashcard());
        deleteFlashcardButton.setOnAction(e->handleDeleteFlashcard());

        shuffleFlashcardsButton.setOnAction(e -> handleShuffleFlashcards());

        goToHomeButton.setOnAction(e-> {
            try {
                goToHomePage(e);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        sidebarTreeView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                //topic node gular child nai
                if(newVal.isLeaf()) {
                    System.out.println("Topic selected "+ newVal.getValue());

                    Topic clickedTopic = findTopicByName(newVal.getValue());

                    if(clickedTopic != null) {
                        showFlashcards(clickedTopic);
                    }
                }
            }
            else{
                //child ase, so its a subject
                System.out.println("Subject selected "+ newVal.getValue());
            }
        });
    }

    private void loadSidebarTree() {
        String imagePathContentArea = getClass().getResource("/com/example/myapp/images/mainFlashcardBackground.png").toExternalForm();

        ContentArea.setStyle(
                "-fx-background-image: url('"   + imagePathContentArea + "');" +
                        "-fx-background-repeat: no-repeat;"              +
                        "-fx-background-position: center center;"        +
                        "-fx-background-size: cover;"
        );

        String imagePathSidebar = getClass().getResource("/com/example/myapp/images/sidebarBackground.png").toExternalForm();
        sidebar.setStyle(
                "-fx-background-image: url('"   + imagePathSidebar + "');" +
                        "-fx-background-repeat: no-repeat;"              +
                        "-fx-background-position: center center;"        +
                        "-fx-background-size: cover;"
        );

        TreeItem<String> rootItem = new TreeItem<>("Subjects");
        rootItem.setExpanded(true);

        for (Subject subject : subjects) {
            TreeItem<String> subjectItem = new TreeItem<>(subject.getName());

            for (Topic topic : subject.getTopics()) {
                TreeItem<String> topicItem = new TreeItem<>(topic.getName());
                subjectItem.getChildren().add(topicItem);
            }

            rootItem.getChildren().add(subjectItem);
        }

        sidebarTreeView.setRoot(rootItem);
        //root "subjects" dekhabe na
        sidebarTreeView.setShowRoot(false);
    }

    private void handleAddSubject() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add New Subject");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter subject name:");

        dialog.showAndWait().ifPresent(name -> {
            if (!name.trim().isEmpty()) {
                Subject newSubject = new Subject(name);
                subjects.add(newSubject);
                loadSidebarTree();
                saveData();
            }
        });
    }

    private void handleDeleteSubject() {
        TreeItem<String> selected = sidebarTreeView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("No Selection", "Please select a subject to delete.");
            return;
        }

        if (selected.getParent() != sidebarTreeView.getRoot()) {
            showAlert("Invalid Selection", "Please select a subject to delete.");
            return;
        }

        String subjectName = selected.getValue();
        Subject subjectToDelete = findSubjectByName(subjectName);

        if (subjectToDelete == null) return;

        if (currentTopic != null && subjectToDelete.hasTopic(currentTopic)) {
            clearContentView();
        }

        subjects.remove(subjectToDelete);
        loadSidebarTree();
        saveData();
    }


    private void handleAddTopic() {
        TreeItem<String> selectedItem = sidebarTreeView.getSelectionModel().getSelectedItem();

        if (selectedItem == null || selectedItem.getParent() == null) {
            showAlert("Invalid selection","Please select a subject first.");
            return;
        }

        String parentName = selectedItem.getValue();
        Subject parentSubject = findSubjectByName(parentName);

        if (parentSubject == null) {
            showAlert("Invalid selection","Please select a subject first.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add New Topic");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter topic name:");

        dialog.showAndWait().ifPresent(name -> {
            if (!name.trim().isEmpty()) {
                parentSubject.addTopic(new Topic(name));
                loadSidebarTree();
                saveData();
            }
        });
    }

    private void handleDeleteTopic() {
        TreeItem<String> selected = sidebarTreeView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("No Selection", "Please select a topic to delete.");
            return;
        }

        if (!selected.isLeaf() || selected.getParent() == null ||
                selected.getParent().getParent() != sidebarTreeView.getRoot()) {
            showAlert("Invalid Selection", "Please select a topic to delete.");
            return;
        }

        String topicName = selected.getValue();
        Topic topicToDelete = findTopicByName(topicName);
        Subject parentSubject = findSubjectByName(selected.getParent().getValue());

        if (topicToDelete == null || parentSubject == null) return;

        if (currentTopic == topicToDelete) {
            clearContentView();
        }

        parentSubject.removeTopic(topicToDelete);
        loadSidebarTree();
        saveData();
    }

    private void handleAddFlashcard() {
        TreeItem<String> selectedItem = sidebarTreeView.getSelectionModel().getSelectedItem();

        if(selectedItem == null || !selectedItem.isLeaf()) {
            showAlert("Invalid selection","Please select a topic first.");
            return;
        }

        Topic parentTopic = findTopicByName(selectedItem.getValue());
        if (parentTopic == null) {
            showAlert("Invalid selection","Please select a topic first.");
            return;
        }


        TextInputDialog frontDialog = new TextInputDialog();
        frontDialog.setTitle("Add New Flashcard");
        frontDialog.setHeaderText(null);
        frontDialog.setContentText("Enter flashcard front text:");
        frontDialog.showAndWait().ifPresent(front -> {
            if(!front.trim().isEmpty()) {
                TextInputDialog backDialog = new TextInputDialog();
                backDialog.setTitle("Add New Flashcard");
                backDialog.setHeaderText(null);
                backDialog.setContentText("Enter flashcard back text:");

                backDialog.showAndWait().ifPresent(back -> {
                    if(!back.trim().isEmpty()) {
                        parentTopic.addFlashcard(new Flashcard(front, back));
                        System.out.println("Flashcard added " + front + "/" + back);
                        showFlashcards(parentTopic);
                    }
                });
            }
        });

        saveData();
    }

    private void handleDeleteFlashcard() {
        if (currentTopic == null) {
            showAlert("No Topic Selected", "Please select a topic first.");
            return;
        }

        List<Flashcard> flashcards = currentTopic.getFlashcards();
        if (flashcards.isEmpty() || currentFlashcardIndex < 0) {
            showAlert("No Flashcards", "No flashcards to delete.");
            return;
        }

        flashcards.remove(currentFlashcardIndex);

        if (flashcards.isEmpty()) {
            clearContentView();
        } else {
            if (currentFlashcardIndex >= flashcards.size()) {
                currentFlashcardIndex = flashcards.size() - 1;
            }
            showFlashcards(currentTopic);
        }

        saveData();
    }

    private void clearContentView() {
        ContentArea.getChildren().clear();
        currentTopic = null;
        currentFlashcardIndex = -1;
    }

    private Subject findSubjectByName(String name) {
        for (Subject subject : subjects) {
            if (subject.getName().equals(name)) {
                return subject;
            }
        }
        return null;
    }

    private Topic findTopicByName(String name) {
        for(Subject subject : subjects) {
            for(Topic topic : subject.getTopics()) {
                if(topic.getName().equals(name)) {
                    return topic;
                }
            }
        }
        return null;
    }


    private void showFlashcards(Topic topic) {
        ContentArea.getChildren().clear();
        currentTopic = topic;
        currentFlashcardIndex = 0;

        if (topic.getFlashcards().isEmpty()) {
            showAlert("Empty Display","No flashcards for: " + topic.getName());
            return;
        }


        HBox navigationBar = new HBox(10);
        navigationBar.setAlignment(Pos.CENTER);

        Button prevButton = new Button("Previous");
        Button nextButton = new Button("Next");
        Label counterLabel = new Label();

        navigationBar.getChildren().addAll(prevButton, counterLabel, nextButton);

        StackPane flashcardContainer = new StackPane();
        flashcardContainer.setPrefSize(400, 250);

        VBox mainContent = new VBox(20, navigationBar, flashcardContainer);
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setPadding(new Insets(20));

        AnchorPane.setTopAnchor(mainContent, 0.0);
        AnchorPane.setBottomAnchor(mainContent, 0.0);
        AnchorPane.setLeftAnchor(mainContent, 0.0);
        AnchorPane.setRightAnchor(mainContent, 0.0);

        ContentArea.getChildren().add(mainContent);

        loadCurrentFlashcard(flashcardContainer, counterLabel);
        saveData();

        prevButton.setOnAction(e -> {
            if (currentFlashcardIndex > 0) {
                currentFlashcardIndex--;
                loadCurrentFlashcard(flashcardContainer, counterLabel);
            }
        });

        nextButton.setOnAction(e -> {
            if (currentFlashcardIndex < currentTopic.getFlashcards().size() - 1) {
                currentFlashcardIndex++;
                loadCurrentFlashcard(flashcardContainer, counterLabel);
            }
        });
    }

    private void loadCurrentFlashcard(StackPane container, Label counterLabel) {
        container.getChildren().clear();

        Flashcard flashcard = currentTopic.getFlashcards().get(currentFlashcardIndex);

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/myapp/fxml/flashcard-single-view.fxml")
            );
            StackPane cardPane = loader.load();


            FlashcardViewController controller = loader.getController();
            controller.setFlashcard(flashcard.getFrontText(), flashcard.getBackText());

            cardPane.setOnMouseClicked(e -> controller.flipCard());
            container.getChildren().add(cardPane);

            counterLabel.setText((currentFlashcardIndex + 1) + " / " + currentTopic.getFlashcards().size());
            saveData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void handleShuffleFlashcards() {
        if (currentTopic == null || currentTopic.getFlashcards().isEmpty()) {
            showAlert("Zero Flashcards for this topic","No flashcards to shuffle");
            return;
        }

        Collections.shuffle(currentTopic.getFlashcards());
        currentFlashcardIndex = 0;
        showFlashcards(currentTopic);
        saveData();
    }

    private void saveData() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File("flashcard_data.json"), subjects);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Save Error", "Failed to save data: " + e.getMessage());
        }
    }

    private void loadData() {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File("flashcard_data.json");
        if (file.exists()) {
            try {
                subjects = mapper.readValue(file, new TypeReference<List<Subject>>() {});
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Load Error", "Failed to load data: " + e.getMessage());
                subjects = new ArrayList<>();
            }
        } else {
            subjects = new ArrayList<>();
        }
    }

    @FXML
    private void goToHomePage(ActionEvent event) throws IOException {
        saveData();
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/myapp/fxml/homepage.fxml")
        );
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(
                getClass().getResource("/com/example/myapp/css/homepage.css").toExternalForm()
        );
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Home");
        stage.show();
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

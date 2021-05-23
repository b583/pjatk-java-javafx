package org.openjfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

/**
 * JavaFX App
 */
public class App extends Application {

    // useful links http://tutorials.jenkov.com/javafx/tableview.html

    // TODO research existing tutorials for inspiration
    // TODO spawn dialog with details via context menu
    // TODO something with a selection model?
    // TODO something with a focus model?

    // ostrzezenie bo x osob nie spelnia warunku jak zaznaczysz wszystkich
    // szczepienia, guziki na dole wystaw skierowanie na szczepienie (dostepnosc na podstawie wieku)

    // TODO co bym chciał robić na zajęciach
    // step 0
        // set stage name to 'Vacciner' or anything else you fancy
    // step 1 create a table
        // define simple dto (name, surname, gender, age)
        // create a few instances for rows
        // bind them to the table
        // demonstrate
    // step 2 add a button above the table
        // allow multiselect
        // hint add button and table to a vbox, and then pass vbox to a stack pane)
        // when button is clicked print info about selected rows (tip override toString method and use stream collect collectors joining)
    // step 3 display said info in a modal dialog
        // simple dialog
        // should be modal (out of the box?)
        // ok button to close
        // create a TextArea storing the info so it can be copied and pasted someplace else (AND LOOKS NICE)
    // step 4 make the button disabled when person below 30 is selected
        // for extra points (streams) allow to click the button multiple people are selected and at least one of them is not under 30
        // add a label under the table which will warn that 'with current selection X people will not be skierowane for a vaccination"

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {

        // Step 0
        stage.setTitle("Vacciner");

        // Step 1 create a TableView

        // start with some persons
        var persons = List.of(
          new Person("Name1", "Surname1", Person.Gender.MALE, 19),
          new Person("Name2", "Surname2", Person.Gender.FEMALE, 31),
          new Person("Name3", "Surname3", Person.Gender.MALE, 30),
          new Person("Name4", "Surname4", Person.Gender.FEMALE, 45)
        );

        var table = constructPersonsTable();
        table.getItems().addAll(persons);

        // Step 2 add a button which will print details of selected persons
        var printInfoButton = new Button("Print info");
        printInfoButton.setOnMouseClicked(mouseEvent -> System.out.println(getSelectedPersonsString(table)));
        // allow for multi-selection
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Step 3 display selected people in a modal dialog
        // name is a little bit misleading but return the button, not the dialog
        var showDialogButton = constructInfoDialog(table);

        // Step 4 make the button disabled when person under 30 is selected
        // assume only show dialog button exists
        showDialogButton.setDisable(true); // no selection on start
        table.getSelectionModel().selectedItemProperty().addListener((observableValue, oldPerson, newPerson) -> {
            var isPersonUnder30Selected = table.getSelectionModel().getSelectedItems().stream().anyMatch(person -> person.getAge() < 30);
            showDialogButton.setDisable(isPersonUnder30Selected);
        });

        var scene = new Scene(new StackPane(new VBox(printInfoButton, showDialogButton, table)), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    private TableView<Person> constructPersonsTable() {
        // create TableView instance
        var table = new TableView<Person>();

        // create TableColumns and setCellValueFactory's
        TableColumn<Person, String> nameColumn = constructTableColumn ("Name");
        TableColumn<Person, String> surnameColumn = constructTableColumn ("Surname");
        TableColumn<Person, Person.Gender> genderColumn = constructTableColumn ("Gender");
        TableColumn<Person, Integer> ageColumn = constructTableColumn ("Age");

        // add columns to the table
        table.getColumns().addAll(List.of(nameColumn, surnameColumn, genderColumn, ageColumn));

        return table;
    }

    private <T> TableColumn<Person, T> constructTableColumn(String name) {
        var column = new TableColumn<Person, T>(name);
        column.setCellValueFactory(new PropertyValueFactory<>(name.toLowerCase()));
        return column;
    }

    private String getSelectedPersonsString(TableView<Person> personTableView) {
        var output = new StringBuilder("Selected Persons:");
        output.append(System.lineSeparator());
        for(Person p: personTableView.getSelectionModel().getSelectedItems()) {
            output.append(p);
            output.append(System.lineSeparator());
        }
        return output.toString();
    }

    private Button constructInfoDialog(TableView<Person> personTableView) {
        var infoDialog = new Alert(Alert.AlertType.INFORMATION);
        infoDialog.setTitle("People selected for vaccination");
        infoDialog.setHeaderText("People selected for vaccination");

        // default info dialog is not so nice so create a TextArea
        var infoTextArea = new TextArea();
        infoTextArea.setEditable(false);
        infoTextArea.setWrapText(true);
        infoDialog.getDialogPane().setContent(new VBox(infoTextArea)); // looks nicer if wrapped in a vbox

        // step 3 specific button (in the actual exercise it should be changed)
        var showDialogButton = new Button("Select for vaccination");
        showDialogButton.setOnMouseClicked(mouseEvent -> {
            infoTextArea.setText(getSelectedPersonsString(personTableView));
            infoDialog.show();
        });

        return showDialogButton;
    }

}
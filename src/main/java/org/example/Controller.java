package org.example;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class Controller extends KrPreviewWindowController{
    private File file;
    private boolean isInt = true;
    private boolean isPositive = true;
    private ArrayList<String> sheets = new ArrayList<>();
    private String[] paymentMethodsArray = {"PayPal", "Venmo", "Cash App", "Revolut","Zelle","TransferWise","Other"};
    private ExcelFile excelFile = new ExcelFile();

    @FXML
    private TextField filePath;

    @FXML
    private Button filePathBrowse;

    //clear is path field clear button
    @FXML
    private Button clear;

    //clear2 is write clear button
    @FXML
    private Button clear2;

    //clear3 is delete clear button
    @FXML
    private Button clear3;

    @FXML
    private ChoiceBox<String> sheetChoiceBox;

    @FXML
    private Label sheetLabel;

    @FXML
    private TextField slotsField;

    @FXML
    private Button Write;

    @FXML
    private TextField deleteFromField;

    @FXML
    private TextField deleteToField;

    @FXML
    private Button delete;

    @FXML
    private TextField nameField;

    @FXML
    private ProgressBar pBar;

    @FXML
    private Label writePaymentMethodLabel;

    @FXML
    private RadioButton yesWriteButton;

    @FXML
    private RadioButton noWriteButton;

    @FXML
    private ChoiceBox<String> writeMethodChoiceBox;

    @FXML
    private TextField updateFromField;

    @FXML
    private TextField updateToField;

    @FXML
    private Button update;

    //clear4 is update clear button
    @FXML
    private Button clear4;

    @FXML
    private RadioButton yesUpdateButton;

    @FXML
    private RadioButton noUpdateButton;

    @FXML
    private Label updatePaymentMethodLabel;

    @FXML
    private ChoiceBox<String> updateMethodChoiceBox;

    //adding key-event "Enter" to buttons
    @FXML
    void enterClicked(KeyEvent event) throws Exception {
        if(event.getCode()==KeyCode.ENTER){
            if(event.getSource()==Write){
                writeClicked();
            }
            else if(event.getSource()==delete && delete.isFocused()){
                deleteClicked();
            }
            else if(event.getSource()==update){
                updateClicked();
            }
            else if(event.getSource()==filePathBrowse){
                browseClicked();
            }
            //path field clear button
            else if(event.getSource()==clear){
                clearClicked();
            }
            //write clear button
            else if(event.getSource()==clear2){
                clear2Clicked();
            }
            //delete clear button
            else if(event.getSource()==clear3){
                clear3Clicked();
            }
            //update clear button
            else if(event.getSource()==clear4){
                clear4Clicked();
            }
        }
    }

    @FXML
    void browseClicked() {
        System.out.println("Browse clicked!");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("choose a file");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel Files", "*.xlsx","*.xls");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(null);
        this.file =file;
        if (file!=null) {
            clearItemsInMethodChoiceBox(updateMethodChoiceBox);
            updateMethodChoiceBox.setTooltip(new Tooltip("Select a payment method"));
            addItemsToUpdatePaymentMethodChoiceBox(paymentMethodsArray);
            clearItemsInMethodChoiceBox(writeMethodChoiceBox);
            writeMethodChoiceBox.setTooltip(new Tooltip("Select a payment method"));
            addItemsToWritePaymentMethodChoiceBox(paymentMethodsArray);
            clearItemsInChoiceBox();
            sheetChoiceBox.setVisible(true);
            sheetLabel.setVisible(true);
            sheetChoiceBox.setTooltip(new Tooltip("Select a Sheet"));
            filePath.setText(file.getAbsolutePath());
            this.sheets = excelFile.getSheets(file.getAbsolutePath());
            addItemsToChoiceBox(sheets);
            //testing purpose
            System.out.println(filePath.getText());
        }
    }

    private void addItemsToChoiceBox(ArrayList<String> sheets) {
        for (String sheet : sheets){
            sheetChoiceBox.getItems().add(sheet);
        }
        sheetChoiceBox.setValue(sheets.get(0));
    }

    private void addItemsToWritePaymentMethodChoiceBox(String[] array) {
        for (String method : array){
            writeMethodChoiceBox.getItems().add(method);
        }
        writeMethodChoiceBox.setValue(array[0]);
    }

    private void addItemsToUpdatePaymentMethodChoiceBox(String[] array) {
        for (String method : array){
            updateMethodChoiceBox.getItems().add(method);
        }
        updateMethodChoiceBox.setValue(array[0]);
    }

    private void clearItemsInChoiceBox() {
        if(!sheetChoiceBox.getItems().isEmpty()){
            sheetChoiceBox.getItems().clear();
        }
    }

    private void clearItemsInMethodChoiceBox(ChoiceBox<String> choiceBox) {
        if(!choiceBox.getItems().isEmpty()){
            choiceBox.getItems().clear();
        }
    }

    @FXML
    void clearClicked() {
        clearItemsInChoiceBox();
        clearItemsInMethodChoiceBox(updateMethodChoiceBox);
        clearItemsInMethodChoiceBox(writeMethodChoiceBox);
        filePath.setText("");
        sheetChoiceBox.setVisible(false);
        sheetLabel.setVisible(false);
        //testing purpose
        System.out.println(filePath.getText());
    }

    @FXML
    void clear2Clicked() {
        nameField.setText("");
        slotsField.setText("");
    }

    @FXML
    void clear3Clicked() {
        deleteFromField.setText("");
        deleteToField.setText("");
    }

    @FXML
    void clear4Clicked() {
        updateFromField.setText("");
        updateToField.setText("");
    }

    @FXML
    void updateRadioButtonClicked() {
        if(yesUpdateButton.isSelected() && !yesUpdateButton.isDisable()){
            updatePaymentMethodLabel.setVisible(true);
            updateMethodChoiceBox.setVisible(true);

            updateMethodChoiceBox.setTooltip(new Tooltip("Select a payment method"));
            noUpdateButton.setDisable(false);
            noUpdateButton.setSelected(false);
            yesUpdateButton.setDisable(true);
        }
        if(noUpdateButton.isSelected() && !noUpdateButton.isDisable()){
            updatePaymentMethodLabel.setVisible(false);
            updateMethodChoiceBox.setVisible(false);
            yesUpdateButton.setDisable(false);
            yesUpdateButton.setSelected(false);
            noUpdateButton.setDisable(true);
        }
    }

    @FXML
    void writeRadioButtonClicked() {
        if(yesWriteButton.isSelected() && !yesWriteButton.isDisable()){
            writePaymentMethodLabel.setVisible(true);
            writeMethodChoiceBox.setVisible(true);
            writeMethodChoiceBox.setTooltip(new Tooltip("Select a payment method"));
            noWriteButton.setDisable(false);
            noWriteButton.setSelected(false);
            yesWriteButton.setDisable(true);
        }
        if(noWriteButton.isSelected() && !noWriteButton.isDisable()){
            writePaymentMethodLabel.setVisible(false);
            writeMethodChoiceBox.setVisible(false);
            yesWriteButton.setDisable(false);
            yesWriteButton.setSelected(false);
            noWriteButton.setDisable(true);
        }
    }

    @FXML
    void updateClicked() {
        AlertBox popup = new AlertBox();
        boolean paid = false;
        String paymentMethod = "N/A";
        if(yesUpdateButton.isSelected()&&yesUpdateButton.isDisable()){
            paid = true;
            paymentMethod = updateMethodChoiceBox.getValue();
        }
        //if fields are not empty....
        if(!filePath.getText().isEmpty()&&!updateFromField.getText().isEmpty()&&!updateToField.getText().isEmpty()){
            //if positive and number
            this.isPositive = isPositive(updateFromField);
            this.isInt =  isInt(updateFromField);
            /*
            if update from field is positive and is a number, then we proceed and check if update to field is
            positive and is a number, else we send a message to the user that it is wrong action.
            */
            if(isPositive && isInt){
                this.isPositive = isPositive(updateToField);
                this.isInt =  isInt(updateToField);
                /*
                if delete To field is positive and is a number then we call the delete method in excel,
                else if it is a non integer or if it is a negative integer then we display a message to the user
                stating that it is a illegal entry.
                */
                if(isPositive && isInt){
                    //if the content of delete from is less than or equal to delete to textfield content then perform
                    //deletion
                    if(Integer.parseInt(updateFromField.getText())<=Integer.parseInt(updateToField.getText())){
                        /*
                        do the logic of Delete, make an excel file using the excel delete constructor. and perform the
                        delete action
                        TODO: The Code for update and excel file creation using the update constructor in excel goes here
                        */
                        //display a Alert Confirmation message to the user to confirm their action
                        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to Update " +
                                "the following rows: "+ "Row: "+updateFromField.getText()+" to " +"Row: "+updateToField.getText()+"?");
                        Optional<ButtonType> result = confirmation.showAndWait();
                        //if user confirms they want to proceed their update by clicking ok, then run it
                        if(result.isPresent()&&result.get()==ButtonType.OK){
                            System.out.println("user wants to update and confirmed action by clicking ok.");
                            ExcelFile newExcelFile = new ExcelFile(file,
                                    Integer.valueOf(updateFromField.getText()),Integer.valueOf(updateToField.getText()),
                                    pBar, sheetChoiceBox.getValue(), paymentMethod,paid);
                            newExcelFile.updateRowsInFile();
                        }
                        //if user wish not to proceed their update by clicking cancel, then return
                        else if(result.isPresent()&&result.get()==ButtonType.CANCEL){
                            System.out.println("user does not want to update and clicked cancel.");
                            return;
                        }
                        //if user wish not to proceed their update by clicking close, then return
                        else if(result.isPresent()&&result.get()==ButtonType.CLOSE){
                            System.out.println("user does not want to update and clicked close.");
                            return;
                        }
                    }
                    //update from in this case is greater than update to and that is invalid entry, let the user know...
                    else {
                        System.out.println("'Update From' Can not be greater than 'Update to' Field");
                        popup.setMessage("'Update From' Can not be greater than 'Update to' Field");
                        popup.displayPopup();
                    }
                }
                //else if update to field is not positive or is not a number then...
                else{
                    if(isPositive && !isInt){
                        System.out.println("'Update To' Field can only take numeric entries.");
                        popup.setMessage("'Update To' Field can only take numeric entries.");
                        popup.displayPopup();
                    }
                    if(!isPositive && isInt){
                        System.out.println("'Update To' Field can not be a negative numeric entry or a 0.");
                        popup.setMessage("'Update To' Field can not be a negative numeric entry or a 0.");
                        popup.displayPopup();
                    }
                    if(!isPositive && !isInt){
                        System.out.println("'Update To' Field must be a positive numeric entry.");
                        popup.setMessage("'Update To' Field must be a positive numeric entry.");
                        popup.displayPopup();
                    }
                }
            }
            //else if update from field is not positive or is not a number then...
            else{
                if(isPositive && !isInt){
                    System.out.println("'Update From' Field can only take numeric entries.");
                    popup.setMessage("'Update From' Field can only take numeric entries.");
                    popup.displayPopup();
                }
                if(!isPositive && isInt){
                    System.out.println("'Update From' Field can not be a negative numeric entry or a 0.");
                    popup.setMessage("'Update From' Field can not be a negative numeric entry or a 0.");
                    popup.displayPopup();
                }
                if(!isPositive && !isInt){
                    System.out.println("'Update From' Field must be a positive numeric entry.");
                    popup.setMessage("'Update From' Field must be a positive numeric entry.");
                    popup.displayPopup();
                }
            }
        }
        //else if fields, one or both is/are empty we display empty field, show error
        else {
            if(filePath.getText().isEmpty() && !updateFromField.getText().isEmpty()
                    &&!updateToField.getText().isEmpty()){
                System.out.println("Invalid Path");
                popup.setMessage("Invalid Path");
                popup.displayPopup();
            }
            else if(!filePath.getText().isEmpty() && updateFromField.getText().isEmpty()
                    &&!updateToField.getText().isEmpty()){
                System.out.println("'Update From' field is empty!");
                popup.setMessage("'Update From' field is empty!");
                popup.displayPopup();
            }
            else if(!filePath.getText().isEmpty() && !updateFromField.getText().isEmpty()
                    &&updateToField.getText().isEmpty()){
                System.out.println("'Update To' field is empty!");
                popup.setMessage("'Update To' field is empty!");
                popup.displayPopup();
            }
            else{
                System.out.println("Multiple empty fields");
                popup.setMessage("Multiple empty fields");
                popup.displayPopup();
            }
        }
    }


    @FXML
    void deleteClicked() {
        /*
        if delete is clicked we will have to check and verify that the deletefromfield
        value is less than or equal to deleteToField, and deleteToField is greater
        than delete from value or equal to it. Both need to be positive
        if so then we show a warning message to the user that the action is irreversible
        and action can not be undone. if user accepts then we proceed, else we don't.
        */
        AlertBox popup = new AlertBox();
        //if fields are not empty....
        if(!filePath.getText().isEmpty()&&!deleteFromField.getText().isEmpty()&&!deleteToField.getText().isEmpty()){
            //if positive and number
            this.isPositive = isPositive(deleteFromField);
            this.isInt =  isInt(deleteFromField);
            /*
            if delete from field is positive and is a number, then we proceed and check if delete to field is
            positive and is a number, else we send a message to the user that it is wrong action.
            */
            if(isPositive && isInt){
                this.isPositive = isPositive(deleteToField);
                this.isInt =  isInt(deleteToField);
                /*
                if delete To field is positive and is a number then we call the delete method in excel,
                else if it is a non integer or if it is a negative integer then we display a message to the user
                stating that it is a illegal entry.
                */
                if(isPositive && isInt){
                    //if the content of delete from is less than or equal to delete to textfield content then perform
                    //deletion
                    if(Integer.parseInt(deleteFromField.getText())<=Integer.parseInt(deleteToField.getText())){
                        /*
                        do the logic of Delete, make an excel file using the excel delete constructor. and perform the
                        delete action
                        TODO: The Code for delete and excel file creation using the delete constructor in excel goes here
                        */
                        //display a Alert Confirmation message to the user to confirm their action
                        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,"This action is irreversible. Are you sure you want to delete " +
                                "the following rows: "+ "Row: "+deleteFromField.getText()+" to " +"Row: "+deleteToField.getText()+"?");
                        Optional<ButtonType> result = confirmation.showAndWait();
                        //if user confirms they want to proceed their deletion by clicking ok, then run it
                        if(result.isPresent()&&result.get()==ButtonType.OK){
                            System.out.println("user wants to delete and confirmed action by clicking ok.");
                            ExcelFile newExcelFile = new ExcelFile(file, Integer.valueOf(deleteFromField.getText()),
                                    Integer.valueOf(deleteToField.getText()),pBar, sheetChoiceBox.getValue());
                            newExcelFile.deleteRowsInFile();
                        }
                        //if user wish not to proceed their deletion by clicking cancel, then return
                        else if(result.isPresent()&&result.get()==ButtonType.CANCEL){
                            System.out.println("user does not want to delete and clicked cancel.");
                            return;
                        }
                        //if user wish not to proceed their deletion by clicking close, then return
                        else if(result.isPresent()&&result.get()==ButtonType.CLOSE){
                            System.out.println("user does not want to delete and clicked close.");
                            return;
                        }
                    }
                    //delete from in this case is greater than delete to and that is invalid entry, let the user know...
                    else {
                        System.out.println("'Delete From' Can not be greater than 'Delete to' Field");
                        popup.setMessage("'Delete From' Can not be greater than 'Delete to' Field");
                        popup.displayPopup();
                    }
                }
                //else if delete to field is not positive or is not a number then...
                else{
                    if(isPositive && !isInt){
                        System.out.println("'Delete To' Field can only take numeric entries.");
                        popup.setMessage("'Delete To' Field can only take numeric entries.");
                        popup.displayPopup();
                    }
                    if(!isPositive && isInt){
                        System.out.println("'Delete To' Field can not be a negative numeric entry or a 0.");
                        popup.setMessage("'Delete To' Field can not be a negative numeric entry or a 0.");
                        popup.displayPopup();
                    }
                    if(!isPositive && !isInt){
                        System.out.println("'Delete To' Field must be a positive numeric entry.");
                        popup.setMessage("'Delete To' Field must be a positive numeric entry.");
                        popup.displayPopup();
                    }
                }
            }
            //else if delete from field is not positive or is not a number then...
            else{
                if(isPositive && !isInt){
                    System.out.println("'Delete From' Field can only take numeric entries.");
                    popup.setMessage("'Delete From' Field can only take numeric entries.");
                    popup.displayPopup();
                }
                if(!isPositive && isInt){
                    System.out.println("'Delete From' Field can not be a negative numeric entry or a 0.");
                    popup.setMessage("'Delete From' Field can not be a negative numeric entry or a 0.");
                    popup.displayPopup();
                }
                if(!isPositive && !isInt){
                    System.out.println("'Delete From' Field must be a positive numeric entry.");
                    popup.setMessage("'Delete From' Field must be a positive numeric entry.");
                    popup.displayPopup();
                }
            }
        }
        //else if fields, one or both is/are empty we display empty field, show error
        else {
            if(filePath.getText().isEmpty() && !deleteFromField.getText().isEmpty()
            &&!deleteToField.getText().isEmpty()){
                System.out.println("Invalid Path");
                popup.setMessage("Invalid Path");
                popup.displayPopup();
            }
            else if(!filePath.getText().isEmpty() && deleteFromField.getText().isEmpty()
                    &&!deleteToField.getText().isEmpty()){
                System.out.println("'Delete From' field is empty!");
                popup.setMessage("'Delete From' field is empty!");
                popup.displayPopup();
            }
            else if(!filePath.getText().isEmpty() && !deleteFromField.getText().isEmpty()
                    &&deleteToField.getText().isEmpty()){
                System.out.println("'Delete To' field is empty!");
                popup.setMessage("'Delete To' field is empty!");
                popup.displayPopup();
            }
            else{
                System.out.println("Multiple empty fields");
                popup.setMessage("Multiple empty fields");
                popup.displayPopup();
            }
        }
    }

    @FXML
    void writeClicked() throws Exception {
        this.isInt = isInt(slotsField);
        this.isPositive = isPositive(slotsField);
        boolean paid = false;
        String paymentMethod = "N/A";
        if(yesWriteButton.isSelected()&&yesWriteButton.isDisable()){
            paid = true;
            paymentMethod = writeMethodChoiceBox.getValue();
        }
        if(isPositive && isInt &&!slotsField.getText().isEmpty() && !nameField.getText().isEmpty()){
            //if we have selected a file to write to
            if(!filePath.getText().isEmpty()){
                System.out.println("Writing " + slotsField.getText() + " Slots for: " + nameField.getText());
                ExcelFile newExcelFile = new ExcelFile(file,
                        Integer.valueOf(slotsField.getText()),nameField.getText(),pBar, sheetChoiceBox.getValue(), paid, paymentMethod);
                newExcelFile.writeToSheet();
            }
            //else if we don't have a selected file path
            else{
                System.out.println("Path is invalid!");
                AlertBox popup = new AlertBox("Invalid Path!");
                popup.displayPopup();
            }
        }
        //else if those fields are missing
        else{
            AlertBox popup = new AlertBox();

            if(slotsField.getText().isEmpty() && !nameField.getText().isEmpty()){
                System.out.println("empty slots field");
                popup.setMessage("'Slots' field is empty!");
                popup.displayPopup();
            }
            else if(nameField.getText().isEmpty() && !slotsField.getText().isEmpty()){
                System.out.println("empty name field");
                popup.setMessage("'Name' field is empty!");
                popup.displayPopup();
            }
            else if(!nameField.getText().isEmpty() && !slotsField.getText().isEmpty() && !isInt){
                popup.setMessage("Only numeric entries are allowed in 'Slots' field.");
                popup.displayPopup();
            }
            else if(!nameField.getText().isEmpty() && !slotsField.getText().isEmpty() && isInt && !isPositive){
                System.out.println("negative number or a 0 entered in slots field");
                popup.setMessage("No negative numeric entries, or '0' are allowed in 'Slots' field.");
                popup.displayPopup();
            }
            else{
                System.out.println("multiple empty fields");
                popup.setMessage("Multiple empty fields!");
                popup.displayPopup();
            }

        }
    }

    //Verifies if slots text-field is a number that is larger than 0 and not a 0
    private boolean isPositive(TextField input) {
        try {
            int slots = Integer.parseInt(input.getText());
            System.out.println("slots field contains a numeric value");
            if (slots > 0) {
                return true;
            }
            return false;
        }catch (NumberFormatException e){
            return false;
        }
    }

    //Verifies if slots text-field has numeric value or not
    private boolean isInt(TextField input) {
        try {
            int slots = Integer.parseInt(input.getText());
            System.out.println("slots field contains a numeric value");
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }

    @FXML
    void previewClicked() throws IOException {
        ObservableList<ExcelRow> ol;
        AlertBox alertBox = new AlertBox();
        alertBox.ChangeAlertType();
        if(!filePath.getText().isEmpty()) {
            ExcelFile ef = new ExcelFile();
            ef.initOl(filePath.getText(), sheetChoiceBox.getValue());
            ol = ef.getOl();
            //commented code is to test whether the data from the excel file is being saved to a observable list "ol"
            //or not
            for (ExcelRow er: ol){
                System.out.println(er.getRowNumber()+" "+er.getSlotNumber()+" "
                +er.getName()+" "+er.getPaid()+" "+er.getPaymentMethod());
            }

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("krPreviewWindow.fxml"));
            Parent root = fxmlLoader.load();
            KrPreviewWindowController controller = fxmlLoader.getController();
            controller.init(ol);
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            // this forces the user to close the preview window before getting back to the main
            //program and continuing to use it.
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(sheetChoiceBox.getValue() + " Data in Tableview");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }
        //else if no file is been selected
        else{
            alertBox.setMessage("No selected file to view!");
            alertBox.displayPopup();
        }
    }

    @FXML
    void closeApplication() {
        System.exit(0);
    }
}
package org.example;

import javafx.scene.control.*;

public class AlertBox {
    //creating a public Alert object
    Alert alert = new Alert(Alert.AlertType.ERROR);
    //Constructor#1
    public AlertBox(){

    }
    //Constructor#2
    public AlertBox(String message){
        //setting the title of the Alert
        alert.setTitle("Alert");
        //using the passed message argument to set the body of the alert when displayed
        alert.setContentText(message);

    }
    public void ChangeAlertType()
    {
        this.alert.setAlertType(Alert.AlertType.INFORMATION);
    }
    public void ChangeAlertTypeToConfirmation()
    {
        this.alert.setAlertType(Alert.AlertType.CONFIRMATION);
    }
    //method to display the popup Alert when required
    public void displayPopup(){
        alert.showAndWait();
    }
    //method to set the Alert message
    public void setMessage(String message){
        alert.setContentText(message);
    }
}

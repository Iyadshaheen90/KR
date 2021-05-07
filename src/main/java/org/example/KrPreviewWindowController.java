package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class KrPreviewWindowController {

    private ObservableList<ExcelRow> ol = FXCollections.observableArrayList();

    @FXML
    private TableView<ExcelRow> table;

    @FXML
    private TableColumn<ExcelRow, Integer> colRow;

    @FXML
    private TableColumn<ExcelRow, String> colSlot;

    @FXML
    private TableColumn<ExcelRow, String> colName;

    @FXML
    private TableColumn<ExcelRow, String> colPaid;

    @FXML
    private TableColumn<ExcelRow, String> colPaymentMethod;

    public void init(ObservableList<ExcelRow> ol){
        colRow.setCellValueFactory(new PropertyValueFactory<>("rowNumber"));
        colSlot.setCellValueFactory(new PropertyValueFactory<>("slotNumber"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPaid.setCellValueFactory(new PropertyValueFactory<>("paid"));
        colPaymentMethod.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        table.setItems(ol);
    }
}


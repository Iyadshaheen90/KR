package org.example;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ExcelRow {
    private final int rowNumber;
    private final String slotNumber;
    private final String name;
    private final String paid;
    private final String paymentMethod;

    public ExcelRow(int rowNumber, String name,String slotNumber, String paid, String paymentMethod){
        this.rowNumber = rowNumber;
        this.slotNumber = slotNumber;
        this.name = name;
        this.paid = paid;
        this.paymentMethod = paymentMethod;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public String getSlotNumber() {
        return slotNumber;
    }

    public String getName() {
        return name;
    }

    public String getPaid() {
        return paid;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
}

package com.kalagala.personalschechuler.model;

public enum  AlertType {
    NOTIFICATION(0),
    SILENT(1);
    private AlertType(int id){
        this.id = id;
    }
    private int id;
    public int getId() {
        return id;
    }
    public static AlertType getAlertById(int id){
        for(AlertType alertType: AlertType.values()){
            if (alertType.getId() == id){
                return alertType;
            }
        }
        return null;
    }
}

package com.kalagala.personalschechuler.model;

public enum  AlertType {
    ALARM(0),
    NOTIFICATION(1),
    SILENT(2);
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

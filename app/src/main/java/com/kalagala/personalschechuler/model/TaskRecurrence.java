package com.kalagala.personalschechuler.model;

public enum TaskRecurrence {

    ONCE(0),
    DAILY(1),
    ONLY_ON(2);
    private final int id;
    private TaskRecurrence(int id){
        this.id = id;
    }
    public int getId(){
        return id;
    }
    public static TaskRecurrence getRecuranceById(int id){
        for(TaskRecurrence recurrence: TaskRecurrence.values()){
            if (recurrence.getId() == id){
                return recurrence;
            }
        }
        return null;
    }
}

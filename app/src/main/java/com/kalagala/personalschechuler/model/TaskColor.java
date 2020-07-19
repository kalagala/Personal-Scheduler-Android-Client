package com.kalagala.personalschechuler.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum  TaskColor {
    RED(0),
    YELLOW(1),
    ORANGE(2),
    GREEN(3),
    LIGHT_BLUE(4),
    PURPLE(5),
    PINK(6);

    public int getColorId() {
        return colorId;
    }

    private final int colorId;
    private TaskColor(int id){
        colorId=id;
    }
    private static final List<TaskColor> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static TaskColor randomColor()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
    public static TaskColor getTaskColorWithId(int id){
        for (TaskColor taskColor: TaskColor.values()){
            if (taskColor.getColorId() == id){
                return taskColor;
            }
        }
        return null;
    }
}

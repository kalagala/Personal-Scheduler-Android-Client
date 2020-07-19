package com.kalagala.personalschechuler.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum  TaskColor {
    RED(1),
    YELLOW(2),
    ORANGE(3),
    GREEN(4),
    LIGHT_BLUE(5),
    BLUE(6),
    PURPLE(7),
    PINK(8);

    public final int colorId;
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
}

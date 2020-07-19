package com.kalagala.personalschechuler;

import com.kalagala.personalschechuler.model.TaskColor;

public class Utils {

    public static int getColorResourceForTaskColor(TaskColor color){
        switch (color){
            case RED: return R.color.red;
            case BLUE: return R.color.blue;
            case PINK: return R.color.pink;
            case GREEN: return R.color.green;
            case ORANGE: return R.color.orange;
            case PURPLE: return R.color.purple;
            case YELLOW: return R.color.yellow;
            case LIGHT_BLUE: return R.color.light_blue;

        }
        return 0;
    }
}

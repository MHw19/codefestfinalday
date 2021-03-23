package com.lk.codefist2021.directionsLib;

import com.lk.codefist2021.pojo.Mapdistanceobj;
import com.lk.codefist2021.pojo.Maptimeobj;

/**
 * Created by Vishal on 10/20/2018.
 */

public interface TaskLoadedCallback {
    void onTaskDone(Object... values);
    void onTimeTaskDone(Maptimeobj maptimeobj);
    void onDistanceTaskDone(Mapdistanceobj mapdistanceobj);
}

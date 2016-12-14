package com.michelle.goldwin.tpamobile.object;

import android.database.CursorIndexOutOfBoundsException;

import java.util.Vector;

/**
 * Created by Michelle Neysa on 12/13/2016.
 */

public class CurrentTask {

    String name;
    int cal;
    boolean checked;



    public CurrentTask(){

    }

    public CurrentTask(String name,int cal){

        this.name = name;
        this.cal = cal;
        checked = false;
    }

    public int getCal() {
        return cal;
    }

    public void setCal(int cal) {
        this.cal = cal;
    }

    public boolean isChecked() {
        return checked;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}

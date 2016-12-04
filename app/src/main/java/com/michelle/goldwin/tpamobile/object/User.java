package com.michelle.goldwin.tpamobile.object;

import java.util.Date;

/**
 * Created by Goldwin on 4/12/2016.
 */

public class User {
    public String fullname;
    public Date DOB;
    public Double height;
    public Double weight;
    public Boolean updateStatus;
    public String profileurl;

    public User(){

    }

    public User(String fullname,Date DOB, Double height, Double weight, Boolean updateStatus, String profileurl) {
        this.fullname = fullname;
        this.DOB = DOB;
        this.height = height;
        this.weight = weight;
        this.updateStatus = updateStatus;
        this.profileurl = profileurl;
    }
}

package com.michelle.goldwin.tpamobile.object;

import java.util.Date;

/**
 * Created by Goldwin on 4/12/2016.
 */

public class User {
    public String fullname;
    public String DOB;
    public Double height;
    public Double weight;
    public Boolean updateStatus;
    public String profileurl;

    public User(){

    }

    public User(String fullname,String DOB, Double height, Double weight, Boolean updateStatus, String profileurl) {
        this.fullname = fullname;
        this.DOB = DOB;
        this.height = height;
        this.weight = weight;
        this.updateStatus = updateStatus;
        this.profileurl = profileurl;
    }

}

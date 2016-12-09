package com.michelle.goldwin.tpamobile.global;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Goldwin on 8/12/2016.
 */

public class LoggedUserInformation {
    private static LoggedUserInformation instance;

    private String fullname;
    private String DOB;
    private Double height;
    private Double weight;
    private String profileurl;

    public static LoggedUserInformation getInstance() {
        if(instance == null)
        {
            synchronized (LoggedUserInformation.class)
            {
                //Agar menghindari Partial Initialization Object
                LoggedUserInformation _instance = instance;
                if(_instance == null)
                {
                    synchronized (LoggedUserInformation.class)
                    {
                        _instance = new LoggedUserInformation();
                    }
                }
                instance = _instance;
            }
        }
        return instance;
    }

    public String getFullname() {
        return fullname;
    }
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getProfileurl() {
        return profileurl;
    }

    public void setProfileurl(String profileurl) {
        this.profileurl = profileurl;
    }
}

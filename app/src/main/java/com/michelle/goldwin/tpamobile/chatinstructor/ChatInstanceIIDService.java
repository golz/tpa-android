package com.michelle.goldwin.tpamobile.chatinstructor;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Michelle Neysa on 12/8/2016.
 */

public class ChatInstanceIIDService extends FirebaseInstanceIdService {

    private static final String TAG = "IID Service";

    @Override
    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();


    }
}

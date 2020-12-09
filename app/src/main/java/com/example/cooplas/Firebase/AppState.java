package com.example.cooplas.Firebase;

import com.example.cooplas.Firebase.Models.User;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by phamngocthanh on 7/29/17.
 */

public class AppState {
    public static FirebaseUser currentFireUser;
    public static User currentBpackCustomer;

    public static String email_address;
    public static String first_name;
    public static String last_name;
    public static String photo_url;
}

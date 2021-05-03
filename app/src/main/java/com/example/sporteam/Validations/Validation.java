package com.example.sporteam.Validations;


import com.example.sporteam.Common.Common;
import com.example.sporteam.Model.UserProfile;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Validation {
     private  DatabaseReference table_user_profile;
     private  DatabaseReference table_user;


    public Validation() {
        this.table_user_profile =  FirebaseDatabase.getInstance().getReference().child("UsersProfile");
        this.table_user = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    public boolean isAgeValid(String date){
        boolean isValid = true;
        String pattern = "dd/MM/yyyy";
        String dateNow =new SimpleDateFormat(pattern).format(new Date());
        String[] arrDate = date.split("/");
        String[] arrDateCurrent = dateNow.split("/");

        if(Integer.valueOf(arrDateCurrent[2])-16 >= Integer.valueOf(arrDate[2])){
            isValid=true;
        }else{
            isValid=false;
        }
        return  isValid;
    }

    public boolean isDateFormatValid(String date){
        boolean isValid = true;
        try {
            new SimpleDateFormat("dd/mm/yyyy").parse(date);
            isValid = true;
        } catch (ParseException e) {
            isValid = false;
        }
        return  isValid;
    }

    public boolean isPasswordValidLength(String password){
        boolean isValid;
        if(password.length() > 11){
            isValid =false;
        }else{
          isValid = true;
        }
        return isValid;
    }

    public boolean isDatePast(String date){
        boolean isDatePast= false;
        String pattern = "dd/MM/yyyy";
        String dateNow =new SimpleDateFormat(pattern).format(new Date());
        String[] arrDate= date.split("/");
        String[] arrDateCurrent = dateNow.split("/");

        if (Integer.valueOf(arrDateCurrent[0]) <= Integer.valueOf(arrDate[0])) {
            if (Integer.valueOf(arrDateCurrent[1]) <= Integer.valueOf(arrDate[1])) {
                if (Integer.valueOf(arrDateCurrent[2]) > Integer.valueOf(arrDate[2])) {
                    isDatePast = true;
                }
            } else {
                isDatePast = true;
            }
        } else {
            isDatePast = true;
        }
        return isDatePast;
    }

    public boolean isDateValidForGame(String date){
        boolean isDatePast= false;
        String pattern = "dd/MM/yyyy";
        String dateNow =new SimpleDateFormat(pattern).format(new Date());
        String[] arrDate= date.split("/");
        String[] arrDateCurrent = dateNow.split("/");

            if (Integer.valueOf(arrDateCurrent[1]) <= Integer.valueOf(arrDate[1])) {
                if (Integer.valueOf(arrDateCurrent[2]) > Integer.valueOf(arrDate[2])) {
                    isDatePast = true;
                }
            } else {
                isDatePast = true;
            }

            if(Integer.valueOf(arrDateCurrent[1]) == Integer.valueOf(arrDate[1]) && Integer.valueOf(arrDateCurrent[2]) == Integer.valueOf(arrDate[2])){
                if(Integer.valueOf(arrDateCurrent[0])> Integer.valueOf(arrDate[0])){
                    isDatePast = true;
                }
            }
        return isDatePast;
    }

    public void fillGameHistory(UserProfile userProfile){
        for(int j= 0 ;j<Common.currentUserProfile.getGameHistory().size();j++){
            userProfile.getGameHistory().add(Common.currentUserProfile.getGameHistory().get(j));
        }

    }
    public void fillGamePlanned(UserProfile userProfile){
        for(int i=0; i<Common.currentUserProfile.getGamePlanned().size();i++){
            userProfile.getGamePlanned().add(Common.currentUserProfile.getGamePlanned().get(i));
        }
    }

    public void UpdateFireBaseWithUserDetails(UserProfile userProfile){
        updateRemoveUser();
        table_user_profile.child(Common.currentUser.getId()).setValue(userProfile);
    }

    private void updateRemoveUser() {
        table_user_profile.child(Common.currentUserProfile.getId()).removeValue();
    }
}

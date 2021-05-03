package com.example.sporteam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sporteam.Common.Common;
import com.example.sporteam.Model.UserProfile;
import com.example.sporteam.Validations.Validation;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GameInfoForPlayer extends AppCompatActivity {
private TextView typeText,locationText,dateText,createdByText,joinText,participantsText;
private DatabaseReference table_user_profile;
private Validation validation;
    private Button joinBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info_for_player);

        validation = new Validation();
        table_user_profile = FirebaseDatabase.getInstance().getReference().child("UsersProfile");
        typeText = findViewById(R.id.type);
        locationText = findViewById(R.id.gameLocationInfo);
        dateText = findViewById(R.id.gameDateInfo);
        createdByText = findViewById(R.id.createdByInfo);
        joinText = findViewById(R.id.alreadyJoinedInfo);
        participantsText = findViewById(R.id.participantsInfo);
        joinBtn =  findViewById(R.id.Join);

        updateGameInfo();

        getSupportActionBar().setTitle(getResources().getString(R.string.gameInfo));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.coloPrimary)));


        if(getSupportActionBar() !=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        else
            Toast.makeText(this, getResources().getString(R.string.fail), Toast.LENGTH_SHORT).show();

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.game.getMaxPlayers() > Common.game.getCurrentPlayers()) {
                    boolean isChanged=false;
                    boolean isNotRegistered=true;
                    boolean isAlreadyDeleted = false;
                    for (int i = 0; i < Common.game.getParticipants().size(); i++) {
                        if (Common.game.getParticipants().get(i).getId().equals(Common.currentUserProfile.getId())) {
                                isNotRegistered = false;
                                if(Common.game.getParticipants().get(i).isDeleted()){
                                    isAlreadyDeleted = true;
                                }
                        }
                    }
                    if(isNotRegistered){
                        UserProfile userProfile = new UserProfile(Common.currentUser.getId(),Common.currentUser.getName(),String.valueOf(Common.currentUserProfile.getAge()),0,Common.currentUser.getGender(),Common.currentUser.getPhone(),false,Common.currentUserProfile.getGamesCreated(),Common.currentUserProfile.getGamesPlayed());
                        validation.fillGameHistory(userProfile);
                        validation.fillGamePlanned(userProfile);
                        Common.game.setCurrentPlayers(Common.game.getCurrentPlayers() + 1);
                        Common.game.getParticipants().add(Common.currentUserProfile);
                        DatabaseReference table_games = FirebaseDatabase.getInstance().getReference().child("Games");
                        table_games.child(Common.game.getId()).child(getString(R.string.participants)).setValue(Common.game.getParticipants());
                        table_games.child(Common.game.getId()).child(getString(R.string.currentPlayers)).setValue(Common.game.getCurrentPlayers());
                        userProfile.getGamePlanned().add(Common.game);
                        validation.UpdateFireBaseWithUserDetails(userProfile);
                        isChanged=true;
                        updateGameInfo();
                    }else{
                        if(!isAlreadyDeleted) {
                            Toast.makeText(v.getContext(), getString(R.string.alreadyRegisterGame), Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(v.getContext(), getString(R.string.userDeletedHimSelf), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    if(isChanged){
                        Toast.makeText(v.getContext(), getString(R.string.gameJoinSucceed), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }else{
                    Toast.makeText(v.getContext(), getString(R.string.gameJoinErr), Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,Home.class));
        finish();
    }

    private void updateGameInfo(){

        typeText.setText(getString(R.string.gameType)+Common.game.getSportType());
        locationText.setText(getString(R.string.locationOfGame)+Common.game.getLocationName());
        dateText.setText(getString(R.string.DateOfGamePlanned)+Common.game.getDateOfGame());
        createdByText.setText(getString(R.string.createdBy)+Common.game.getCreatedBy());
        joinText.setText(getString(R.string.howManyParticipantsAlreadyHave)+Common.game.getCurrentPlayers()+"/"+Common.game.getMaxPlayers());

        StringBuilder str = new StringBuilder();
        str.append(getString(R.string.Participants));

        for(int i =0 ;i<Common.game.getParticipants().size();i++){
            if(!Common.game.getParticipants().get(i).isDeleted()) {
                str.append(Common.game.getParticipants().get(i).getName() + "\n");
            }

        }


        participantsText.setText(str);
    }


}
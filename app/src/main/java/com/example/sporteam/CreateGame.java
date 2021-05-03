package com.example.sporteam;


import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sporteam.Common.Common;
import com.example.sporteam.Model.Game;
import com.example.sporteam.Model.UserProfile;
import com.example.sporteam.Validations.Validation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class CreateGame extends AppCompatActivity {

    private Button createBtn;
    private FloatingActionButton addBtn;
    private TextInputEditText textLocation;
    private EditText edtDate;
    private Spinner typeOfGameSpinner;
    private DatePickerDialog picker;
    private String sportType;
    private String dateCreated;
    private DatabaseReference table_user_profile;
    private Validation validation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        getSupportActionBar().setTitle(getResources().getString(R.string.createGame));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.coloPrimary)));

        if(getSupportActionBar() !=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        else
            Toast.makeText(this, getResources().getString(R.string.fail), Toast.LENGTH_SHORT).show();
        table_user_profile = FirebaseDatabase.getInstance().getReference().child("UsersProfile");
        createBtn = findViewById(R.id.create_button);
        addBtn = findViewById(R.id.floating_button_add);
        textLocation= findViewById(R.id.name_of_location);
        edtDate = findViewById(R.id.date_of_game);
        typeOfGameSpinner = findViewById(R.id.spinnerTypeGame);

        validation = new Validation();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_games= database.getReference("Games");
        textLocation.setEnabled(false);
        edtDate.setEnabled(false);


        final List<Game.sportType>  typesOfGame= Arrays.asList(Game.sportType.values());
        ArrayAdapter<Game.sportType> typesOfGameAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,typesOfGame);
        typesOfGameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeOfGameSpinner.setAdapter(typesOfGameAdapter);
        typeOfGameSpinner.setSelection( typesOfGame.indexOf(Game.sportType.Football));
        typeOfGameSpinner.setEnabled(false);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textLocation.setEnabled(true);
                edtDate.setEnabled(true);
                typeOfGameSpinner.setEnabled(true);
                addBtn.setVisibility(View.INVISIBLE);
                createBtn.setVisibility(View.VISIBLE);
            }
        });

        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                int currentYear = year;
                // date picker dialog
                picker = new DatePickerDialog(CreateGame.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                edtDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                closeKeyBoard();

                            }
                        }, year, month, day);
                picker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                picker.show();
            }
        });


        typeOfGameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Object obj = parent.getItemAtPosition(position).toString();
                setSportType(String.valueOf(obj));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


       createBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String location =  textLocation.getText().toString();
                    String date = edtDate.getText().toString();
                    boolean isValid = true;

                    if(location.isEmpty()){
                        textLocation.setError(getString(R.string.locationError));
                        isValid = false;
                    }

                    if(!date.isEmpty()){
                        if(validation.isDateFormatValid(date)) {

                            String pattern = "dd/MM/yyyy";
                            String dateNow =new SimpleDateFormat(pattern).format(new Date());
                            setDateCreated(dateNow);
                            if(validation.isDateValidForGame(date)){
                                edtDate.setError(getString(R.string.datePastError));
                                isValid=false;
                            }
                        }
                    }else{
                        isValid=false;
                        edtDate.setError(getString(R.string.date_error));
                    }

                    if(isValid){
                        UserProfile userProfile = new UserProfile(Common.currentUser.getId(),Common.currentUser.getName(),String.valueOf(Common.currentUserProfile.getAge()),0,Common.currentUser.getGender(),Common.currentUser.getPhone(),false,Common.currentUserProfile.getGamesCreated()+1,Common.currentUserProfile.getGamesPlayed());
                        validation.fillGameHistory(userProfile);
                        validation.fillGamePlanned(userProfile);

                        int maxPlayers = checkGame(getSportType());
                        String id = UUID.randomUUID().toString();
                        Game game = new Game(getDateCreated(),Common.currentUserProfile.getName(),date,location,getSportType(),id,false,false,maxPlayers,1);
                        game.getParticipants().add(Common.currentUserProfile);
                        userProfile.getGamePlanned().add(game);
                        table_games.child(game.getId()).setValue(game);
                        validation.UpdateFireBaseWithUserDetails(userProfile);
                        finish();
                    }
                }
            });

    }


    public String getSportType() {
        return sportType;
    }

    public void setSportType(String sportType) {
        this.sportType = sportType;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,Home.class));
        finish();
    }


    private void closeKeyBoard() {
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int checkGame(String sportType){
        int maxPlayers=0;
        switch (sportType){
            case "Football":
                maxPlayers=22;
                break;
            case "Tennis":
                maxPlayers =4;
                break;
            case "Handball":
                maxPlayers =14;
                break;
            case "Basketball":
                maxPlayers= 10;
                break;
            case "Running":
                //according the corona status
                maxPlayers = 30;
                break;
            case "Volleyball":
                maxPlayers = 12;
                break;
            default:
                break;

        }
        return maxPlayers;
    }
}

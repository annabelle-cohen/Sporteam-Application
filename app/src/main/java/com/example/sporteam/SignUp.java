package com.example.sporteam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.sporteam.Model.Game;
import com.example.sporteam.Model.User;
import com.example.sporteam.Model.UserProfile;
import com.example.sporteam.Style.LoadingDialog;
import com.example.sporteam.Validations.Validation;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Calendar;
import java.util.UUID;


public class SignUp extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private TextInputLayout edtPassword, edtName, edtPhone;
    private EditText edtDate;
    private Button btnSignUp;
    private DatePickerDialog picker;
    private String selectGender;
    private boolean isSignUpValid = true;
    private int currentYear;
    private Validation validation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final Spinner spinner = findViewById(R.id.spinnerGender);
        edtDate = findViewById(R.id.edtDate);
        edtName= findViewById(R.id.edtName);
        edtPassword=  findViewById(R.id.edtPassword2);
        edtPhone= findViewById(R.id.edtPhone);
        btnSignUp = (Button)findViewById(R.id.btnSignUp2);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.gender,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        validation = new Validation();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user= database.getReference("Users");
        final DatabaseReference table_user_profile = database.getReference("UsersProfile");

        //Dialog
        final LoadingDialog loadingDialog = new LoadingDialog(SignUp.this);


        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                currentYear = year;
                // date picker dialog
                picker = new DatePickerDialog(SignUp.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                edtDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                closeKeyBoard();

                            }
                        }, year, month, day);
                picker.show();
            }
        });


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                        //Check if already user phone
                        if(dataSnapshot.child(edtPhone.getEditText().getText().toString()).exists()){
                            if(edtPhone.getEditText().getText().toString().isEmpty()){
                                edtPhone.setError(getString(R.string.error_empty));
                            }else {
                                edtPhone.setError(getString(R.string.phone_exist_in_database));
                                isSignUpValid = false;
                            }
                        }else{
                            boolean isPhoneValid,isPasswordValid,isNameValid,isDateValid;
                            edtPhone.setError(null);
                            String name = edtName.getEditText().getText().toString();
                            String password = edtPassword.getEditText().getText().toString();
                            String date = edtDate.getText().toString();
                            String phone = edtPhone.getEditText().getText().toString();
                            String gender= getSelectGender();


                            isPhoneValid = isPhoneValid(phone);
                            isNameValid = isNameValid(name);
                            isDateValid = checkDate(date);
                            isPasswordValid = isPasswordValid(password);

                            if(isPhoneValid && isNameValid && isDateValid && isPasswordValid){
                                User user = new User(phone,name,password,date,gender);
                                String [] dateOfBirth = user.getDate().split("/");
                                int yearOfBirth= Integer.parseInt(dateOfBirth[2]);
                                int age = Calendar.getInstance().get(Calendar.YEAR)-yearOfBirth;
                                String test = getString(R.string.test);
                                UserProfile userProfile= new UserProfile(user.getId(),user.getName(),String.valueOf(age),0,user.getGender(),user.getPhone(),false,0,0);
                                //Only For initialize for database the arrays fields.
                                Game testForUser = new Game(test,test,test,test,test,UUID.randomUUID().toString(),false,true,0,0);
                                userProfile.getGamePlanned().add(testForUser);
                                userProfile.getGameHistory().add(testForUser);

                                table_user.child(edtPhone.getEditText().getText().toString()).setValue(user);
                                table_user_profile.child(user.getId()).setValue(userProfile);
                                LoadingMessageDialog(loadingDialog);
                                Intent succeed = new Intent(SignUp.this,Succeed_Activity.class);
                                startActivity(succeed);

                            }

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });




    }


    private void closeKeyBoard() {
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    private boolean isPhoneValid(String phone){
        boolean isValid=true;

        if(phone.length() != 10){
            edtPhone.setError(getString(R.string.phone_must_be_10digits));
            isValid = false;
        }else{
            isValid = isPhoneNumberValid(phone);
        }
        return isValid;
    }

    private boolean isPasswordValid(String password){
        boolean isValid =true;

        if(!password.isEmpty()){

            if(validation.isPasswordValidLength(password)){
                edtPassword.setError(null);
            }else {
                edtPassword.setError(getString(R.string.maxPassError));
                return isValid =false;
            }

        }else{
            edtPassword.setError(getString(R.string.error_empty));
            isValid=false;
        }
        return isValid;
    }

    private boolean checkDate(String date){
        boolean isValid=true;
        if(date.isEmpty()){
            edtDate.setError(getString(R.string.date_error));
            isValid=false;
        }else{
            edtDate.setError(null);
            isValid= isDateValid(date);
        }

        return isValid;
    }

    private boolean isNameValid(String name){

        boolean isValid=true;

       if(name.isEmpty()){
            edtName.setError(getString(R.string.error_empty));
            isValid = false;
        }else {
            edtName.setError(null);
        }

        return isValid;
    }

    private boolean isPhoneNumberValid(String phone){
        boolean isValid = true;
        char [] digits = phone.toCharArray();
        if(digits[0] != '0'){
            edtPhone.setError(getString(R.string.phone_number_format));
            isValid=false;
        }else if(digits[1] != '5'){
            edtPhone.setError(getString(R.string.phone_number_format));
            isValid=false;
        }else{
            for(int i = 2;i<digits.length;i++ ){
                if(digits[1] == ' '){
                    edtPhone.setError(getString(R.string.phone_must_include_only_digits));
                    isValid=false;
                }
            }
        }
        return isValid;
    }



    private boolean isDateValid(String date){
        boolean isValid;

        if(validation.isDateFormatValid(date)){
            edtDate.setError(null);
            isValid = true;
        }else{
            isValid = false;
            edtDate.setError(getString(R.string.date_format_error));
        }

        if(isValid) {
            if (!validation.isAgeValid(date)) {
                edtDate.setError(getString(R.string.illegalAge));
                isValid = false;
            }
        }
        return isValid;
    }


    private void LoadingMessageDialog(final LoadingDialog loadingDialog){
        loadingDialog.startLoadingDialog();
        Handler handler= new Handler();
        //If the user press back then the loading message dialog disappear
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               // loadingDialog.dismissDialog();
            }
        },3000);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.setSelectGender( parent.getItemAtPosition(position).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public String getSelectGender() {
        return selectGender;
    }

    public void setSelectGender(String selectGender) {
        this.selectGender = selectGender;
    }
}

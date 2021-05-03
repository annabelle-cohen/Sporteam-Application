package com.example.sporteam.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.sporteam.Common.Common;
import com.example.sporteam.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EditProfileFragment extends Fragment {

    public interface EditProfileListener{
        void updateDataBaseWithGender();
        void updateDataBaseWithAge();
        void notifyHeader();
        void updateName(String name);
        void  updatePhone(String phone);
        void updateDateAccordingAge();
    }
    private TextInputEditText edtName,edtPhone;
    private TextView gameCreated,gamePlayed;
    private Spinner spinnerAge,spinnerGender;
    private ImageView profileImage;
    private FloatingActionButton fButton;
    private Button updateBtn;
    private EditProfileListener mListener;
    private String age;
    private String gender;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        LayoutInflater lf = getActivity().getLayoutInflater();
        View view =inflater.inflate(R.layout.fragment_editprofile,container,false);

        edtName = view.findViewById(R.id.name_profile_editor);
        edtPhone = view.findViewById(R.id.phone_profile_editor);
        fButton=view.findViewById(R.id.floating_button);
        spinnerAge = view.findViewById(R.id.spinnerAges);
        spinnerGender =view.findViewById(R.id.spinnerGender);
        profileImage = view.findViewById(R.id.profileImgEdit);
        gameCreated =view.findViewById(R.id.game_created);
        gamePlayed = view.findViewById(R.id.game_played);
        updateBtn=view.findViewById(R.id.update_user_profile);

        final List age = new ArrayList<Integer>();
        for (int i = 16; i <= 100; i++) {
            age.add(Integer.toString(i));
        }
        ArrayAdapter<Integer> adapterAges= new ArrayAdapter<>(this.getActivity(),android.R.layout.simple_spinner_item,age);
        adapterAges.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAge.setAdapter(adapterAges);
        spinnerAge.setSelection(age.indexOf(Common.currentUserProfile.getAge()));
        spinnerAge.setEnabled(false);


        ArrayAdapter<CharSequence> adapterGender = ArrayAdapter.createFromResource(this.getActivity(),R.array.gender,android.R.layout.simple_spinner_item);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapterGender);
        if(Common.currentUserProfile.getGender().equals(getString(R.string.female))){
            spinnerGender.setSelection(0);
        }else{
            spinnerGender.setSelection(1);
        }
        spinnerGender.setEnabled(false);
      profileImage.setImageResource(Common.currentUserProfile.getUrl());

      edtName.setText(Common.currentUserProfile.getName());
      edtName.setEnabled(false);

      edtPhone.setText(Common.currentUserProfile.getPhone());
      edtPhone.setEnabled(false);

      gameCreated.setText(getString(R.string.gameCreated)+Common.currentUserProfile.getGamesCreated());
      gamePlayed.setText(getString(R.string.gamePlayed)+(Common.currentUserProfile.getGameHistory().size()-1));

        spinnerAge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object obj = parent.getItemAtPosition(position).toString();
                 setAge(String.valueOf(obj));

                if(!Common.currentUserProfile.getAge().equals(getAge())){
                    if(yearHasChanged(Integer.valueOf(getAge()))){
                        EditProfileFragment.this.mListener.updateDateAccordingAge();
                    }
                    updateAge(getAge());
                    EditProfileFragment.this.mListener.updateDataBaseWithAge();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Object obj = parent.getItemAtPosition(position).toString();
                setGender(String.valueOf(obj));


                if(!Common.currentUserProfile.getGender().equals(getGender())){
                     updateGender(getGender());
                    EditProfileFragment.this.mListener.updateDataBaseWithGender();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
      fButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
                spinnerAge.setEnabled(true);
                spinnerGender.setEnabled(true);
                edtName.setEnabled(true);
                edtPhone.setEnabled(true);
                fButton.setVisibility(View.INVISIBLE);
                updateBtn.setVisibility(View.VISIBLE);


          }
      });

      updateBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              String name = edtName.getText().toString();
              String phone = edtPhone.getText().toString();
              boolean valid = true;

              if(name.isEmpty()){
                  edtName.setError(getString(R.string.error_empty));
                  valid = false;
              }else{
                  if(!Common.currentUserProfile.getName().equals(name)){
                      EditProfileFragment.this.mListener.updateName(name);
                  }
              }

              if(phone.isEmpty() || phone.length() != 10){
                  edtPhone.setError(getString(R.string.phoneInvalid));
                  valid=false;
              }else{
                  if(validPhone(phone,edtPhone)){
                      if(!Common.currentUserProfile.getPhone().equals(phone)) {
                          EditProfileFragment.this.mListener.updatePhone(phone);
                      }
                  }else{
                      valid=false;
                  }
              }

              if(valid) {
                  edtName.setEnabled(false);
                  edtPhone.setEnabled(false);
                  spinnerAge.setEnabled(false);
                  spinnerGender.setEnabled(false);
                  updateBtn.setVisibility(View.INVISIBLE);
                  fButton.setVisibility(View.VISIBLE);
                  EditProfileFragment.this.mListener.notifyHeader();
              }
          }
      });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
             EditProfileListener listener = (EditProfileListener)context;
          if(listener != null){
              registerListener(listener);
          }
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString() + getResources().getString(R.string.mustImplementEditProfile));
        }
    }
    public void registerListener(EditProfileFragment.EditProfileListener listener) {
        this.mListener = listener;
    }

    private void updateAge(String age){
        Common.currentUserProfile.setAge(age);
    }

    private void updateGender(String gender){
        Common.currentUserProfile.setGender(gender);
        changePicAccordingGender(gender);
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    private void changePicAccordingGender(String gender){
        if(gender.equals(getString(R.string.female))){
            Common.currentUserProfile.setUrl(R.drawable.woman_profile_coronatime);
        }else{
            Common.currentUserProfile.setUrl(R.drawable.man_profile_coronatime);

        }

        profileImage.setImageResource(Common.currentUserProfile.getUrl());
    }

    private boolean validPhone(String phone,TextInputEditText edtPhone){

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

    private boolean yearHasChanged(int age){
        String [] dateOfBirth = Common.currentUser.getDate().split("/");
        int yearOfBirth= Integer.parseInt(dateOfBirth[2]);
        int oldAge = Calendar.getInstance().get(Calendar.YEAR)-yearOfBirth;

        if(oldAge != age){
            int current = Calendar.getInstance().get(Calendar.YEAR)-age;
            int day = Integer.valueOf(dateOfBirth[0]);
            int month =Integer.valueOf(dateOfBirth[1]);
            String newDate= day+"/"+month+"/"+current;
            Common.currentUser.setDate(newDate);

            return true;
        }else{
            return false;
        }
    }
}

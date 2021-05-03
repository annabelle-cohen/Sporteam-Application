package com.example.sporteam.Fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.sporteam.Common.Common;
import com.example.sporteam.R;
import com.example.sporteam.Validations.Validation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Calendar;

public class DefinitionsFragment extends Fragment {
    public interface  UpdateUserListener{
        void updatePassword();
        void updateDateOfBirth();
        void UpdateAgeAccordingDate();
    }
private Button updateBtn;
private FloatingActionButton editBtn;
private TextInputEditText edtPhone,edtPass;
private EditText edtDate;
private DatePickerDialog picker;
private UpdateUserListener mListener;
private Validation validation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_definitations,container,false);
        validation = new Validation();

        updateBtn = view.findViewById(R.id.update_user_definition);
        editBtn = view.findViewById(R.id.floating_button_edit_def);
        edtPhone = view.findViewById(R.id.userPhone);
        edtPass = view.findViewById(R.id.userPassword);
        edtDate = view.findViewById(R.id.birthDateOfUser);

        edtPhone.setText(Common.currentUser.getPhone());
        edtPass.setText(Common.currentUser.getPassword());
        edtDate.setText(Common.currentUser.getDate());

        edtPhone.setEnabled(false);
        edtPass.setEnabled(false);
        edtDate.setEnabled(false);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //User can not change his phone number that he registered with because it is the key in the user table.
                edtPass.setEnabled(true);
                edtDate.setEnabled(true);
                editBtn.setVisibility(View.INVISIBLE);
                updateBtn.setVisibility(View.VISIBLE);
            }
        });

        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                // date picker dialog
                picker = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                edtDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = edtPass.getText().toString();
                String date = edtDate.getText().toString();
                boolean isValid = true;

                if(!password.isEmpty()){
                    if(validation.isPasswordValidLength(password)) {
                        if (!Common.currentUser.getPassword().equals(password)) {
                            Common.currentUser.setPassword(password);
                            DefinitionsFragment.this.mListener.updatePassword();
                        }
                    }else{
                        isValid=false;
                        edtPass.setError(getString(R.string.maxPassError));
                    }
                }else{
                    isValid=false;
                    edtPass.setError(getString(R.string.error_empty));
                }

                if(!date.isEmpty()){
                    if(validation.isDateFormatValid(date)){
                        edtDate.setError(null);
                        if(!Common.currentUser.getDate().equals(date)) {
                            if (validation.isAgeValid(date)) {
                                Common.currentUser.setDate(date);
                                String[] dateOfBirth = Common.currentUser.getDate().split("/");
                                int yearOfBirth = Integer.parseInt(dateOfBirth[2]);
                                int age = Calendar.getInstance().get(Calendar.YEAR) - yearOfBirth;
                                if (checkIfNeedChangeAge(age)) {
                                    DefinitionsFragment.this.mListener.UpdateAgeAccordingDate();
                                }
                                DefinitionsFragment.this.mListener.updateDateOfBirth();
                            } else {
                                isValid = false;
                                edtDate.setError(getString(R.string.illegalAge));
                            }
                        }
                    }else{
                        edtDate.setError(getString(R.string.date_format_error));
                        isValid=false;
                    }
                }else{
                    isValid=false;
                    edtDate.setError(getString(R.string.date_error));
                }

                if(isValid){
                    edtPass.setEnabled(false);
                    edtDate.setEnabled(false);
                    updateBtn.setVisibility(View.INVISIBLE);
                    editBtn.setVisibility(View.VISIBLE);
                }

            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            UpdateUserListener listener = (UpdateUserListener)context;
            if(listener != null){
                registerListener(listener);
            }
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + getResources().getString(R.string.mustImplementEditProfile));
        }
    }

    public void registerListener(UpdateUserListener listener) {
        this.mListener = listener;
    }


    private boolean checkIfNeedChangeAge(int age){
        if(Integer.valueOf(Common.currentUserProfile.getAge()) == age){
            return false;
        }else{
            Common.currentUserProfile.setAge(String.valueOf(age));
            return true;
        }
    }
}

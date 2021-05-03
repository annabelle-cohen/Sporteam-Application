package com.example.sporteam.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.sporteam.R;


public class Successfully_Registered extends Fragment {

    public interface FragmentSucceedListener{
        void moveToLogin();
        void moveToMain();
    }

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Button moveToLogin;
    private  Button moveToMain;
    private  FragmentSucceedListener mListener;

    private String mParam1;
    private String mParam2;

    public Successfully_Registered() {
    }


    public static Successfully_Registered newInstance(String param1, String param2) {
        Successfully_Registered fragment = new Successfully_Registered();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view =  lf.inflate(R.layout.fragment_successfully__registered, container, false);
        moveToLogin = (Button)view.findViewById(R.id.start_now);
        moveToMain =  (Button)view.findViewById(R.id.start_later);

        moveToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Successfully_Registered.this.mListener.moveToLogin();
            }
        });

        moveToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Successfully_Registered.this.mListener.moveToMain();
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            FragmentSucceedListener listener = (FragmentSucceedListener) context;

            if (listener != null)
                this.registerListener(listener);
        } catch (ClassCastException e) {

            throw new ClassCastException(context.toString() + getResources().getString(R.string.mustImplementSuccess));

        }
    }

    public void registerListener(FragmentSucceedListener listener) {
        this.mListener = listener;
    }


}

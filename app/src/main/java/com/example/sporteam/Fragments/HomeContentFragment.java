package com.example.sporteam.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.sporteam.R;

public class HomeContentFragment extends Fragment {

    public interface FragmentHomeContentListener{
        void moveToCreateNewGame();
        void moveToShowExistGame();
        void moveToSearchForField();
    }

    private CardView joinExistGame;
    private CardView createNewGame;
    private CardView searchForFields;
    private FragmentHomeContentListener mListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_homecontent,container,false);

        joinExistGame = (CardView)view.findViewById(R.id.join_exist_game);
        createNewGame= (CardView)view.findViewById(R.id.create_new_game);
        searchForFields=(CardView)view.findViewById(R.id.search_for_field);

        joinExistGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              HomeContentFragment.this.mListener.moveToShowExistGame();
            }
        });

        createNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeContentFragment.this.mListener.moveToCreateNewGame();
            }
        });

        searchForFields.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeContentFragment.this.mListener.moveToSearchForField();

            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            FragmentHomeContentListener listener = (FragmentHomeContentListener)context;

            if(listener != null){
                this.registerListener(listener);
            }
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString() + getResources().getString(R.string.mustImplement));
        }
    }

    public void registerListener(FragmentHomeContentListener listener) {
        this.mListener = listener;
    }
}

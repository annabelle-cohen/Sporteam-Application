package com.example.sporteam.Fragments;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sporteam.Common.Common;
import com.example.sporteam.Model.Game;
import com.example.sporteam.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class FutureGamesFragment extends Fragment {
    private RecyclerView recyclerView;
    private FutureAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private FutureAdapter.FutureViewClickListener listener;
    private Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_futuregames, container, false);
        mContext = getContext();
        recyclerView = v.findViewById(R.id.futureRecycler);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        getDataFromUserProfile();
        return v;
    }

    private void getDataFromUserProfile() {
        ArrayList<Game> relevantGames = new ArrayList<>();
        for(int i =0 ;i<Common.currentUserProfile.getGamePlanned().size();i++){
            if(!Common.currentUserProfile.getGamePlanned().get(i).isDeleted()){
                relevantGames.add(Common.currentUserProfile.getGamePlanned().get(i));
            }
        }
        adapter = new FutureAdapter(this.getActivity(), relevantGames, listener);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}

    class FutureAdapter extends RecyclerView.Adapter<FutureAdapter.ViewHolder> {

        public interface FutureViewClickListener{
            void onClick(int position);
        }

        private static final String Tag = "RecyclerView";
        private Context nContext;
        private ArrayList<Game> gamesList;
        private FutureViewClickListener listener;
        private Game game;
        private boolean isNeedUpdate;


        public FutureAdapter(Context nContext, ArrayList<Game> gamesList,FutureViewClickListener listener) {
            this.nContext = nContext;
            this.gamesList = gamesList;
            this.listener = listener;
            this.isNeedUpdate = false;

        }

        @NonNull
        @Override
        public FutureAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.futurecard, parent, false);
            return new FutureAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            holder.textCategoryF.setText(gamesList.get(position).getSportType());
            holder.textLocationF.setText(gamesList.get(position).getLocationName());
            holder.textDateGameF.setText(nContext.getResources().getString(R.string.gamePlanned) + gamesList.get(position).getDateOfGame());
            holder.textCurrentPlayersF.setText(nContext.getResources().getString(R.string.playerfromMaxPlayers) + gamesList.get(position).getCurrentPlayers() + "/" +
                    gamesList.get(position).getMaxPlayers());

            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    game = gamesList.get(holder.getAdapterPosition());
                    gamesList.get(holder.getAdapterPosition()).setDeleted(true);
                    Common.currentUserProfile.getGamePlanned().get(Common.currentUserProfile.getGamePlanned().indexOf(gamesList.get(holder.getAdapterPosition()))).setDeleted(true);

                    gamesList.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                    notifyDataSetChanged();
                    updateDataBase(game);
                }

            });
        }

        private void updateDataBase(final Game game) {
           setNeedUpdate(true);
            if(isNeedUpdate()){
                DatabaseReference table_user_profile = FirebaseDatabase.getInstance().getReference().child("UsersProfile");
                table_user_profile.child(Common.currentUserProfile.getId()).child(nContext.getString(R.string.gamePlaned)).setValue(Common.currentUserProfile.getGamePlanned());
                final DatabaseReference games_Table = FirebaseDatabase.getInstance().getReference("Games");
                games_Table.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       Game game1 = dataSnapshot.child(game.getId()).getValue(Game.class);
                        for (int i = 0; i < game1.getParticipants().size(); i++) {
                            if (game1.getParticipants().get(i).getId().equals(Common.currentUserProfile.getId())) {
                                game1.getParticipants().get(i).setDeleted(true);
                                game1.setCurrentPlayers(game.getCurrentPlayers()-1);

                            }
                        }
                        games_Table.child(game.getId()).child(nContext.getString(R.string.participants)).setValue(game1.getParticipants());
                        games_Table.child(game.getId()).child(nContext.getString(R.string.currentPlayers)).setValue(game1.getCurrentPlayers());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
               setNeedUpdate(false);
            }
        }

        public boolean isNeedUpdate() {
            return isNeedUpdate;
        }

        public void setNeedUpdate(boolean needUpdate) {
            isNeedUpdate = needUpdate;
        }

        @Override
        public int getItemCount() {
            return gamesList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView textCategoryF;
            private TextView textLocationF;
            private TextView textDateGameF;
            private TextView textCurrentPlayersF;
            private FloatingActionButton deleteBtn;
            private boolean isClicked = false;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                            if(!isClicked) {
                                deleteBtn.setVisibility(View.VISIBLE);
                                isClicked=true;

                            }else{
                                deleteBtn.setVisibility(View.GONE);
                                isClicked = false;
                            }


                    }
                });


                textCategoryF = itemView.findViewById(R.id.categoryOfSportFragF);
                textLocationF = itemView.findViewById(R.id.locationGameFragF);
                textDateGameF = itemView.findViewById(R.id.dateGameFragF);
                textCurrentPlayersF = itemView.findViewById(R.id.playersFragF);
                 deleteBtn= itemView.findViewById(R.id.delete_btn);
            }


        }

    }
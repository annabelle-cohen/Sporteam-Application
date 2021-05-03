package com.example.sporteam.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sporteam.Common.Common;
import com.example.sporteam.Model.Game;
import com.example.sporteam.R;

import java.util.ArrayList;

public class GameHistoryFragment extends Fragment {
    private RecyclerView recyclerView;
    private HistoryAdapter  adapter;
    private RecyclerView.LayoutManager layoutManager;
    private HistoryAdapter.HistoryViewClickListener listener;
    private ArrayList<Game> relevantGames = new ArrayList<>();
    private Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game_history2,container,false);
        mContext = getContext();
        recyclerView = v.findViewById(R.id.futureRecycler);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        getDataFromUserProfile();
        return v;
    }
    private void getDataFromUserProfile() {
        for(int i = 0 ;i< Common.currentUserProfile.getGameHistory().size();i++){
            if(!Common.currentUserProfile.getGameHistory().get(i).getDateCreated().equals(getString(R.string.test))){
                relevantGames.add(Common.currentUserProfile.getGameHistory().get(i));
            }
        }
        adapter = new HistoryAdapter(this.getActivity(), relevantGames, listener);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}

class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter .ViewHolder> {

    public interface HistoryViewClickListener{
        void onClick(int position);
    }

    private static final String Tag = "RecyclerView";
    private Context nContext;
    private ArrayList<Game> gamesList;
    private HistoryViewClickListener listener;


    public HistoryAdapter(Context nContext, ArrayList<Game> gamesList,HistoryViewClickListener listener) {
        this.nContext = nContext;
        this.gamesList = gamesList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.historycard, parent, false);
        return new HistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textCategoryU.setText(gamesList.get(position).getSportType());
        holder.textLocationU.setText(gamesList.get(position).getLocationName());
        holder.textDateGameU.setText(nContext.getResources().getString(R.string.gamePlanned) + gamesList.get(position).getDateOfGame());
        holder.textCurrentPlayersU.setText(nContext.getResources().getString(R.string.playerfromMaxPlayers) + gamesList.get(position).getCurrentPlayers() + "/" +
                gamesList.get(position).getMaxPlayers());
    }

    @Override
    public int getItemCount() {
        return gamesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textCategoryU;
        private TextView textLocationU;
        private TextView textDateGameU;
        private TextView textCurrentPlayersU;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (gamesList.get(getAdapterPosition()).getMaxPlayers() != gamesList.get(getAdapterPosition()).getCurrentPlayers()) {

                    } else {
                        Toast.makeText(nContext, nContext.getResources().getString(R.string.GameIsFullErr), Toast.LENGTH_SHORT).show();
                    }
                }
            });


            textCategoryU = itemView.findViewById(R.id.categoryOfSportFragU);
            textLocationU = itemView.findViewById(R.id.locationGameFragU);
            textDateGameU = itemView.findViewById(R.id.dateGameFragU);
            textCurrentPlayersU = itemView.findViewById(R.id.playersFragU);
        }
    }

}
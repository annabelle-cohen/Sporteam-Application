package com.example.sporteam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sporteam.Common.Common;
import com.example.sporteam.Model.Game;
import com.example.sporteam.Validations.Validation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;


public class ExistingGames extends AppCompatActivity implements RecyclerAdapter.RecyclerViewClickListener {
private RecyclerView recyclerView;
private DatabaseReference databaseReference;
private RecyclerAdapter recyclerAdapter;
private RecyclerAdapter.RecyclerViewClickListener listener;
private ArrayList<Game> relevantGames = new ArrayList<>();
public static final String ACTIVITY_RESULT_KEY_SECOND="Game";
private Validation validation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_games);
        validation = new Validation();
        recyclerView= findViewById(R.id.gamesRecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        databaseReference = FirebaseDatabase.getInstance().getReference();
       ClearAll();
       getDataFromFirebase();

        getSupportActionBar().setTitle(getResources().getString(R.string.joinExistGameForBar));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.coloPrimary)));

        if(getSupportActionBar() !=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        else
            Toast.makeText(this, getResources().getString(R.string.fail), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ClearAll();
        getDataFromFirebase();
    }


    private void getDataFromFirebase() {

        Query query = databaseReference.child("Games");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren() ) {
                    Game game = snapshot.getValue(Game.class);


                    if(!isDateNotPast(game.getDateOfGame(),snapshot,game)){
                        relevantGames.add(game);

                    }

                }
                    recyclerAdapter = new RecyclerAdapter(getApplicationContext(),relevantGames,listener);
                    recyclerView.setAdapter(recyclerAdapter);
                    recyclerAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean isDateNotPast(String date,DataSnapshot snapshot,Game game) {

        boolean isDatePast = false;

        if(validation.isDateValidForGame(date)){
            isDatePast=true;
            game.setDatePast(true);
            DatabaseReference games_table = databaseReference.child("Games");
            games_table.child(game.getId()).child(getString(R.string.datePast)).setValue(game.isDatePast());
        }

        return isDatePast;
    }

    private void ClearAll() {
        if(relevantGames != null){
            relevantGames.clear();

            if(recyclerAdapter != null){
                recyclerAdapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,Home.class));
        finish();
    }

    @Override
    public void onClick(int position) {
            Toast.makeText(this, (CharSequence) relevantGames.get(position),Toast.LENGTH_SHORT).show();
            recyclerAdapter.notifyDataSetChanged();

    }
}

class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    public interface RecyclerViewClickListener{
        void onClick(int position);
    }

    private static final String Tag = "RecyclerView";
    private Context nContext;
    private ArrayList<Game> gamesList;
    private RecyclerViewClickListener listener;



    public RecyclerAdapter(Context nContext, ArrayList<Game> gamesList,RecyclerViewClickListener listener) {
        this.nContext = nContext;
        this.gamesList = gamesList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        holder.textCategory.setText(gamesList.get(position).getSportType());
        holder.textLocation.setText(gamesList.get(position).getLocationName());
        holder.textDateGame.setText(nContext.getResources().getString(R.string.gamePlanned)+gamesList.get(position).getDateOfGame());
        holder.textCurrentPlayers.setText(nContext.getResources().getString(R.string.playerfromMaxPlayers)+gamesList.get(position).getCurrentPlayers()+"/"+
                gamesList.get(position).getMaxPlayers());

    }

    @Override
    public int getItemCount() {
        return gamesList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textCategory;
        private TextView textLocation;
        private TextView textDateGame;
        private TextView textCurrentPlayers;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(gamesList.get(getAdapterPosition()).getMaxPlayers()!=gamesList.get(getAdapterPosition()).getCurrentPlayers()) {
                        Common.game = gamesList.get(getAdapterPosition());
                        Intent intent = new Intent(v.getContext(), GameInfoForPlayer.class);
                        v.getContext().startActivity(intent);

                    }else{
                        Toast.makeText(nContext,nContext.getResources().getString(R.string.GameIsFullErr),Toast.LENGTH_SHORT).show();
                    }
                }
            });


            textCategory = itemView.findViewById(R.id.categoryOfSport);
            textLocation = itemView.findViewById(R.id.locationGame);
            textDateGame = itemView.findViewById(R.id.dateGame);
            textCurrentPlayers = itemView.findViewById(R.id.players);
        }
    }
}


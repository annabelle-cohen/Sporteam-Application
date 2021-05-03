package com.example.sporteam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.sporteam.Common.Common;
import com.example.sporteam.Fragments.DefinitionsFragment;
import com.example.sporteam.Fragments.EditProfileFragment;
import com.example.sporteam.Fragments.FutureGamesFragment;
import com.example.sporteam.Fragments.GameHistoryFragment;
import com.example.sporteam.Fragments.HomeContentFragment;
import com.example.sporteam.Model.UserProfile;
import com.example.sporteam.Validations.Validation;
import com.google.android.material.internal.NavigationMenuPresenter;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;


public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,HomeContentFragment.FragmentHomeContentListener,EditProfileFragment.EditProfileListener,DefinitionsFragment.UpdateUserListener{
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView mImageView;
    private TextView mTextView_Name;
    private TextView mTextView_Phone;
    private UserProfile userProfile;
    private FirebaseDatabase database;
    private DatabaseReference table_user;
    private DatabaseReference table_user_profile;
    private Validation validation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        database = FirebaseDatabase.getInstance();
        table_user_profile = database.getReference("UsersProfile");
        table_user = database.getReference("Users");
        validation = new Validation();
        table_user_profile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //get specific user profile from fire base
              userProfile = dataSnapshot.child(Common.currentUser.getId()).getValue(UserProfile.class);
              Common.currentUserProfile=userProfile;
                if(Common.currentUserProfile != null) {
                    updateHeader(Common.currentUserProfile);
                    updateGamesListsAccordingDate();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        //initialize for menu
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //default fragment as we begin is the home contentp
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeContentFragment()).commit();
            navigationView.setCheckedItem(R.id.Home);
        }

    }

    private void updateGamesListsAccordingDate() {

        for(int i =0;i<Common.currentUserProfile.getGamePlanned().size();i++) {
            if (!Common.currentUserProfile.getGamePlanned().get(i).getDateOfGame().equals(getString(R.string.test))) {

                if (validation.isDateValidForGame(Common.currentUserProfile.getGamePlanned().get(i).getDateOfGame())) {
                    Common.currentUserProfile.getGameHistory().add(Common.currentUserProfile.getGamePlanned().get(i));
                    Common.currentUserProfile.getGamePlanned().remove(Common.currentUserProfile.getGamePlanned().get(i));

                }

            }
        }

    }


    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.Home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeContentFragment()).commit();
                break;
            case R.id.EditProfile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new EditProfileFragment()).commit();
                break;
            case R.id.Definitions:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new DefinitionsFragment()).commit();
                break;
            case R.id.GameHistory:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new GameHistoryFragment()).commit();
                break;
            case R.id.FutureGames:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FutureGamesFragment()).commit();
                break;
            case R.id.nav_company_name:
                Toast.makeText(this,getResources().getString(R.string.app_name),Toast.LENGTH_SHORT).show();
                break;
            case R.id.LogOut:
                startActivity(new Intent(Home.this,MainActivity.class));
                finish();
                break;
            case R.id.contact:
                Toast.makeText(this,getResources().getString(R.string.contactUs),Toast.LENGTH_SHORT).show();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void updateHeader(UserProfile currentUserProfile) {

        View header = navigationView.getHeaderView(0);
        mTextView_Name = header.findViewById(R.id.showUserName);
        mTextView_Phone= header.findViewById(R.id.showUserPhone);
        mImageView = header.findViewById(R.id.profileImg);
            if(userProfile.getName() != null) {
                mTextView_Name.setText(currentUserProfile.getName());
            }
            if(userProfile.getPhone() != null) {
                mTextView_Phone.setText(Common.currentUser.getPhone());
            }

                if (userProfile.getGender().equals(getString(R.string.female))) {
                    mImageView.setImageResource(R.drawable.woman_profile_coronatime);
                    Common.currentUserProfile.setUrl(R.drawable.woman_profile_coronatime);
                    updateUrl(Common.currentUserProfile.getUrl());

                } else {
                    mImageView.setImageResource(R.drawable.man_profile_coronatime);
                    Common.currentUserProfile.setUrl(R.drawable.man_profile_coronatime);

            }

    }


    @Override
    public void moveToCreateNewGame() {
        startActivity(new Intent(Home.this,CreateGame.class));

    }

    @Override
    public void moveToShowExistGame() {
        startActivity(new Intent(Home.this,ExistingGames.class));

    }

    @Override
    public void moveToSearchForField() {
        startActivity(new Intent(Home.this,Permissions.class));

    }

    @Override
    public void notifyHeader() {
        updateHeader(Common.currentUserProfile);
        updateNavigationView();
    }

    @Override
    public void updateDataBaseWithGender() {
        table_user_profile.child(Common.currentUserProfile.getId()).child(getString(R.string.gender)).setValue(Common.currentUserProfile.getGender());
        table_user_profile.child(Common.currentUserProfile.getId()).child(getString(R.string.url)).setValue(Common.currentUserProfile.getUrl());
    }

    @Override
    public void updateDataBaseWithAge() {
        table_user_profile.child(Common.currentUserProfile.getId()).child(getString(R.string.age)).setValue(Common.currentUserProfile.getAge());
    }

    @Override
    public void updateName(String name){
        Common.currentUserProfile.setName(name);
        table_user_profile.child(Common.currentUserProfile.getId()).child(getString(R.string.name)).setValue(Common.currentUserProfile.getName());

    }


    @Override
    public void  updatePhone(String phone){
        Common.currentUserProfile.setPhone(phone);
        table_user_profile.child(Common.currentUserProfile.getId()).child(getString(R.string.phoneForUpdate)).setValue(Common.currentUserProfile.getPhone());

    }

    @Override
    public void updateDateAccordingAge() {
        updateDateOfBirth();
    }

    private void updateUrl(int url){
        table_user_profile.child(Common.currentUserProfile.getId()).child(getString(R.string.url)).setValue(url);
    }

    private void updateNavigationView() {
        try {
            Field presenterField = NavigationView.class.getDeclaredField("mPresenter");
            presenterField.setAccessible(true);
            ((NavigationMenuPresenter) presenterField.get(navigationView)).updateMenuView(true);
        } catch (NoSuchFieldException e) {
           // e.printStackTrace();
        } catch (IllegalAccessException e) {
           // e.printStackTrace();
        }
    }

    @Override
    public void updatePassword() {
        table_user.child(Common.currentUser.getPhone()).child(getString(R.string.password)).setValue(Common.currentUser.getPassword());
    }

    @Override
    public void updateDateOfBirth() {
        table_user.child(Common.currentUser.getPhone()).child(getString(R.string.date)).setValue(Common.currentUser.getDate());
    }

    @Override
    public void UpdateAgeAccordingDate() {
        this.updateDataBaseWithAge();
    }

}

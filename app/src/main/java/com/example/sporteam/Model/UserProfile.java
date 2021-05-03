package com.example.sporteam.Model;

import java.util.ArrayList;

public class UserProfile {

    private String name;
    private String age;
    private String id;
    private int url,gamesCreated,gamesPlayed;
    private String gender;
    private String phone;
    private boolean isDeleted;
    private ArrayList<Game> gamePlanned = new ArrayList<>();
    private ArrayList<Game> gameHistory = new ArrayList<>();

    public UserProfile(){

    }

    public UserProfile(String id,String name, String age, int url, String gender, String phone,boolean isDeleted,int gamesCreated,int gamesPlayed) {
        this.id=id;
        this.name = name;
        this.age = age;
        this.url = url;
        this.gender = gender;
        this.phone = phone;
        this.gamesCreated = gamesCreated;
        this.gamesPlayed = gamesPlayed;
        this.isDeleted=isDeleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public int getUrl() {
        return url;
    }

    public void setUrl(int url) {
        this.url = url;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ArrayList<Game> getGamePlanned() {
        return gamePlanned;
    }

    public void setGamePlanned(ArrayList<Game> gamePlanned) {
        this.gamePlanned = gamePlanned;
    }

    public ArrayList<Game> getGameHistory() {
        return gameHistory;
    }

    public void setGameHistory(ArrayList<Game> gameHistory) {
        this.gameHistory = gameHistory;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getGamesCreated() {
        return gamesCreated;
    }

    public void setGamesCreated(int gamesCreated) {
        this.gamesCreated = gamesCreated;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}

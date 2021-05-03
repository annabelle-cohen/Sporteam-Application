package com.example.sporteam.Model;

import java.util.ArrayList;

public class Game{

    private String id;
    private String dateCreated;
    private String createdBy;
    private String dateOfGame;
    private String locationName;
    private String sportType;
    private int currentPlayers;
    private int maxPlayers = 0;
    private ArrayList<UserProfile> participants = new ArrayList<>();
    private boolean datePast;
    private boolean isDeleted;
    public static enum sportType{
        Football,Tennis,Handball,Basketball,Running,Volleyball
    };

    public Game(){

    }

    public Game(String dateCreated, String createdBy, String dateOfGame,String locationName,String gameType,String id,boolean datePast,boolean isDeleted,int maxPlayers,int currentPlayers) {
        this.id = id;
        this.dateCreated = dateCreated;
        this.createdBy = createdBy;
        this.dateOfGame = dateOfGame;
        this.locationName = locationName;
        this.datePast=datePast;
        this.isDeleted = isDeleted;
        this.sportType = gameType;
        this.maxPlayers=maxPlayers;
        this.currentPlayers=currentPlayers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getDateOfGame() {
        return dateOfGame;
    }

    public void setDateOfGame(String dateOfGame) {
        this.dateOfGame = dateOfGame;
    }

    public ArrayList<UserProfile> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<UserProfile> participants) {
        this.participants = participants;
    }

    public boolean isDatePast() {
        return datePast;
    }

    public void setDatePast(boolean datePast) {
        this.datePast = datePast;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getSportType() {
        return sportType;
    }

    public void setSportType(String sportType) {
        this.sportType = sportType;
    }

    public int getCurrentPlayers() {
        return currentPlayers;
    }

    public void setCurrentPlayers(int currentPlayers) {
        this.currentPlayers = currentPlayers;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}

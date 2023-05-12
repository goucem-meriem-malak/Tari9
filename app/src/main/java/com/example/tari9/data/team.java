package com.example.tari9.data;

import java.util.ArrayList;

public class team {
    private ArrayList<String> team_services;

    public team() {
    }

    public team(ArrayList<String> team_services) {
        this.team_services = team_services;
    }

    public ArrayList<String> getTeam_services() {
        return team_services;
    }

    public void setTeam_services(ArrayList<String> team_services) {
        this.team_services = team_services;
    }
}
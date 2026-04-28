package com.campusflow;

import com.campusflow.config.Config;

public class Main {

    public static void main(String[] args) {
        Config config = Config.loadDefault();

        System.out.println("CampusFlow Week 14 starter");
        System.out.println("Environment: " + config.env());
        System.out.println("Port: " + config.port());
        System.out.println("Database: " + config.databasePath());
        System.out.println("TODO: connect this skeleton to your real CampusFlow app before release.");
    }
}

package com.campusflow;

import com.campusflow.config.Config;

public class Main {

    public static void main(String[] args) {
        Config config = Config.loadDefault();

        System.out.println("CampusFlow Week 14 starter");
        System.out.println("Environment: " + config.env());
        System.out.println("Port: " + config.port());
        System.out.println("Database: " + config.databasePath());
        System.out.println("待办：发布前把这个 skeleton 接入你真实的 CampusFlow app。");
    }
}

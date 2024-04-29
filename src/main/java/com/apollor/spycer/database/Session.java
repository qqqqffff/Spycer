package com.apollor.spycer.database;

public class Session {


    private Session(){

    }
    public Session(
            String sessionId,
            String userId,
            double locationLat,
            double locationLong,
            String sessionStart,
            String sessionEnd
    ){

        this.sessionId = sessionId;
        this.userId = userId;
        this.locationLat = locationLat;
        this.locationLong = locationLong;
        this.sessionStart = sessionStart;
        this.sessionEnd = sessionEnd;
    }

    private String sessionId;
    private String userId;
    private double locationLat;
    private double locationLong;
    private String sessionStart;
    private String sessionEnd;

}

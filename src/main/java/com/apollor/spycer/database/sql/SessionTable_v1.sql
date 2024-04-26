CREATE TABLE Session (
    session_id varchar(36) NOT NULL,
    userid varchar(36) NOT NULL,
    location_lat float,
    location_long float,
    session_start timestamp,
    session_end timestamp,
    PRIMARY KEY (session_id),
    FOREIGN KEY (userid) REFERENCES Users(userid)
);
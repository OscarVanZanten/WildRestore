CREATE TABLE chunks (
    id INTEGER PRIMARY KEY ,
    x INTEGER not null DEFAULT 0,
    z INTEGER not null DEFAULT 0
);

CREATE TABLE blocks (
    id INTEGER PRIMARY KEY ,
    chunkid INTEGER not null DEFAULT 0,
    x TINYINT not null DEFAULT 0,
    y TINYINT not null DEFAULT 0, 
    z INTEGER not null DEFAULT 0, 
    material INTEGER not null DEFAULT 0,
    subtype TINYINT not null DEFAULT 0
);
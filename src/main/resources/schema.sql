create table GENRE
(
    ID   INTEGER auto_increment
        primary key,
    NAME CHARACTER VARYING not null
);

create table MPA_RATE
(
    ID   INTEGER auto_increment
        primary key,
    NAME CHARACTER VARYING not null
);

create table FILMS
(
    ID          INTEGER auto_increment
        primary key,
    NAME        CHARACTER VARYING not null,
    DESCRIPTION CHARACTER VARYING(200),
    RELEASEDATE DATE              not null,
    DURATION    INTEGER           not null,
    GENRE       CHARACTER VARYING not null,
    MPA_ID      INTEGER
        references MPA_RATE,
    constraint DURATION_POSITIVE
        check ("DURATION" > 0)
);

create table USERS
(
    ID       INTEGER auto_increment
        primary key,
    EMAIL    CHARACTER VARYING not null
        constraint UQ_EMAIL
            unique,
    LOGIN    CHARACTER VARYING not null
        constraint UQ_LOGIN
            unique,
    NAME     CHARACTER VARYING,
    BIRTHDAY DATE              not null
);

create table FILMS_LIKE
(
    FILM_ID INTEGER
        references FILMS,
    USER_ID INTEGER
        references USERS
);

create table FRIENDS_LIST
(
    USER_ID      INTEGER,
    FRIEND_ID    INTEGER,
    CONFIRMATION BOOLEAN,
    foreign key (USER_ID) references USERS,
    foreign key (FRIEND_ID) references USERS
);


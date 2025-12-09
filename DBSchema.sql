create table Products
(
    id          INTEGER not null
        constraint Products_pk
            primary key autoincrement,
    name        varchar not null,
    description varchar,
    price       float,
    nrItems     INTEGER not null,
    nrSold      integer not null,
    slug        varchar,
    brand       varchar,
    currency    varchar,
    category    varchar,
    constraint check_nrItems
        check ("Products".nrItems >= 0),
    constraint check_price
        check ("Products".price > 0),
    constraint nrSold
        check ("Products".nrSold >= 0)
);

create table Sellers
(
    id          integer not null
        constraint Sellers_pk
            primary key autoincrement,
    name        varchar not null,
    description varchar
);

create table Users
(
    email       varchar not null
        constraint Users_pk
            unique,
    password    varchar not null,
    isAdmin     boolean,
    authToken   integer not null,
    lastLogin   datetime,
    address     varchar not null,
    dateCreated date    not null,
    firstName   varchar,
    lastName    varchar
);

create table "Order"
(
    idUser         integer not null
        constraint Order_pk
            primary key
        constraint Order_Users_email_fk
            references Users (email),
    date           date    not null,
    deliveryStatus varchar not null,
    totalProducts  float,
    totalShipping  float,
    totalPrice     float,
    paymentMethod  varchar,
    paymentStatus  boolean,
    address        varchar
);

create table CartItems
(
    idOrder   varchar not null
        constraint CartItems_Order_idUser_fk
            references "Order",
    idProduct integer not null
        constraint CartItems_Products_id_fk
            references Products,
    nrOrdered integer not null,
    constraint CartItems_pk
        primary key (idOrder, idProduct),
    constraint check_nrOrdered
        check (nrOrdered > 0)
);


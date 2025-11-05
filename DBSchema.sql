create table Products
(
    id          integer not null
        constraint Products_pk
            primary key autoincrement,
    name        varchar not null,
    description varchar,
    price       float,
    nrItems     integer not null,
    nrSold      varchar not null,
    constraint check_nrItems
        check (Products.nrItems >= 0),
    constraint check_nrSold
        check (Products.nrSold >= 0),
    constraint check_price
        check (Products.price > 0)
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
    dateCreated date    not null
);

create table CartItems
(
    idUser    varchar not null
        constraint CartItems_Users_email_fk
            references Users (email),
    idProduct integer not null
        constraint CartItems_Products_id_fk
            references Products,
    nrOrdered integer not null,
    constraint CartItems_pk
        primary key (idUser, idProduct),
    constraint check_nrOrdered
        check (CartItems.nrOrdered > 0)
);

create table "Order"
(
    idProduct      varchar not null
        constraint Order_Products_id_fk
            references Products,
    idUser         integer not null
        constraint Order_Users_email_fk
            references Users (email),
    date           date    not null,
    deliveryStatus varchar not null,
    constraint Order_pk
        primary key (idProduct, idUser)
);



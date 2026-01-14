DROP TABLE IF EXISTS Reviews;
DROP TABLE IF EXISTS CartItems;
DROP TABLE IF EXISTS Orders;
DROP TABLE IF EXISTS Addresses;
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Sellers;
DROP TABLE IF EXISTS Products;


CREATE TABLE Products
(
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    name        VARCHAR NOT NULL,
    description VARCHAR,
    price       FLOAT CHECK (price > 0),
    nrItems     INTEGER NOT NULL CHECK (nrItems >= 0),
    nrSold      INTEGER NOT NULL CHECK (nrSold >= 0),
    slug        VARCHAR,
    brand       VARCHAR,
    currency    VARCHAR,
    category    VARCHAR
);

CREATE TABLE Sellers
(
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    name        VARCHAR NOT NULL,
    description VARCHAR
);

CREATE TABLE Users
(
    email       VARCHAR PRIMARY KEY,
    password    VARCHAR NOT NULL,
    isAdmin     BOOLEAN,
    authToken   INTEGER,
    lastLogin   DATETIME,
    dateCreated DATE NOT NULL,
    firstName   VARCHAR,
    lastName    VARCHAR,
    phoneNumber VARCHAR
);

CREATE TABLE Addresses
(
    id          VARCHAR PRIMARY KEY, -- UUID generated in Java
    userEmail   VARCHAR NOT NULL,
    type        VARCHAR CHECK (type IN ('billing', 'shipping')),
    firstName   VARCHAR,
    lastName    VARCHAR,
    street      VARCHAR,
    city        VARCHAR,
    county      VARCHAR,
    postalCode  VARCHAR,
    country     VARCHAR,
    phoneNumber VARCHAR,
    isPrimary   BOOLEAN DEFAULT 0,
    CONSTRAINT Addresses_Users_fk FOREIGN KEY (userEmail) REFERENCES Users (email) ON DELETE CASCADE
);

CREATE TABLE Orders
(
    idOrder         INTEGER PRIMARY KEY,
    idUser          VARCHAR NOT NULL,
    orderDate       DATE,
    deliveryStatus  VARCHAR,
    totalProducts   FLOAT,
    totalShipping   FLOAT,
    totalPrice      FLOAT,
    currency        VARCHAR,
    paymentMethod   VARCHAR,
    paymentStatus   BOOLEAN,
    billingAddress  VARCHAR,
    shippingAddress VARCHAR,
    CONSTRAINT Orders_Users_email_fk
        FOREIGN KEY (idUser) REFERENCES Users (email)
);

CREATE TABLE CartItems
(
    idOrder   INTEGER NOT NULL,
    idProduct INTEGER NOT NULL,
    nrOrdered INTEGER NOT NULL CHECK (nrOrdered > 0),
    PRIMARY KEY (idOrder, idProduct),
    FOREIGN KEY (idOrder) REFERENCES Orders (idOrder),
    FOREIGN KEY (idProduct) REFERENCES Products (id)
);

CREATE TABLE Reviews
(
    idProduct        INTEGER NOT NULL,
    idUser           VARCHAR NOT NULL,
    rating           INTEGER NOT NULL,
    title            VARCHAR NOT NULL,
    description      VARCHAR,
    createdAt        DATE NOT NULL,
    verifiedPurchase BOOLEAN NOT NULL,
    PRIMARY KEY (idProduct, idUser),
    FOREIGN KEY (idProduct) REFERENCES Products (id),
    FOREIGN KEY (idUser) REFERENCES Users (email)
);

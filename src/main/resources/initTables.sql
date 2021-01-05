CREATE SCHEMA TEST_LIBRARY DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE TEST_LIBRARY;

CREATE TABLE IF NOT EXISTS GENRE (
    Id INT UNSIGNED AUTO_INCREMENT,
    Genre VARCHAR(50) NOT NULL,
    CONSTRAINT UC_Genre UNIQUE (Genre),
    CONSTRAINT PK_Genre_Id PRIMARY KEY (Id)
);

CREATE TABLE IF NOT EXISTS BOOK (
    Id INT UNSIGNED AUTO_INCREMENT,
    ISBN VARCHAR(17) NOT NULL,
    Title VARCHAR(50) NOT NULL,
    Author VARCHAR(50) NOT NULL,
    Price DOUBLE UNSIGNED NOT NULL,
    Publisher VARCHAR(50) NOT NULL,
    Genre_Id INT UNSIGNED NOT NULL,
    Preview MEDIUMTEXT NOT NULL,
    CONSTRAINT PK_Book_Id PRIMARY KEY (Id),
    CONSTRAINT UC_ISBN UNIQUE (ISBN),
    CONSTRAINT FK_Genre_Id_BOOK FOREIGN KEY (Genre_Id)
        REFERENCES GENRE (Id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS ORDER_STATUS (
    Id INT UNSIGNED AUTO_INCREMENT,
    Status VARCHAR(20) NOT NULL,
    CONSTRAINT UC_Status UNIQUE (Status),
    CONSTRAINT PK_Status_Id PRIMARY KEY (Id)
);

CREATE TABLE IF NOT EXISTS `ORDER` (
    Id INT UNSIGNED AUTO_INCREMENT,
    Library_User_Id INT UNSIGNED NOT NULL,
    Order_Time TIMESTAMP NOT NULL,
    Status_Id INT UNSIGNED NOT NULL,
    CONSTRAINT PK_Order_Id PRIMARY KEY (Id),
    CONSTRAINT FK_Library_User_Id_ORDER FOREIGN KEY (Library_User_Id)
        REFERENCES LIBRARY_USER (Id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT FK_Status_Id_ORDER FOREIGN KEY (Status_Id)
        REFERENCES ORDER_STATUS (Id)
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS ORDER_BOOK (
    Order_Id INT UNSIGNED NOT NULL,
    Book_Id INT UNSIGNED NOT NULL,
    CONSTRAINT CompKey_Book_Id_Order_Id PRIMARY KEY (Order_Id, Book_Id),
    CONSTRAINT FK_Order_Id_BOOK_ORDER FOREIGN KEY (Order_Id)
        REFERENCES `ORDER` (Id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT FK_Book_Id_BOOK_ORDER FOREIGN KEY (Book_Id)
        REFERENCES BOOK (Id)
        ON UPDATE CASCADE
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS USER_ROLE (
    Id INT UNSIGNED AUTO_INCREMENT,
    Role VARCHAR(20) NOT NULL,
    CONSTRAINT UC_Role UNIQUE (Role),
    CONSTRAINT PK_User_Role_Id PRIMARY KEY (Id)
);

CREATE TABLE IF NOT EXISTS LIBRARY_USER (
    Id INT UNSIGNED AUTO_INCREMENT,
    Name VARCHAR(20) NOT NULL,
    Login VARCHAR(100) NOT NULL,
    Password VARCHAR(255) NOT NULL,
    Email VARCHAR(50) NOT NULL,
    Role_Id INT UNSIGNED NOT NULL,
    CONSTRAINT UC_Login UNIQUE (Login),
    CONSTRAINT UC_Email UNIQUE (Email),
    CONSTRAINT PK_Library_User_Id PRIMARY KEY (Id),
    CONSTRAINT FK_Role_Id_LIBRARY_USER FOREIGN KEY (Role_Id)
        REFERENCES USER_ROLE (Id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS USER_BANK_ACCOUNT (
    Library_User_Id INT UNSIGNED NOT NULL,
    IBAN VARCHAR(34) NOT NULL,
    CONSTRAINT UC_IBAN UNIQUE (IBAN),
    CONSTRAINT CompKey_Library_User_Id_IBAN PRIMARY KEY (Library_User_Id, IBAN),
    CONSTRAINT FK_Library_User_Id_BANK_ACCOUNT FOREIGN KEY (Library_User_Id)
        REFERENCES LIBRARY_USER (Id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);
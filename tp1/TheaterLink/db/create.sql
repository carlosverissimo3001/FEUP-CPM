CREATE TYPE voucher_type AS ENUM ('FIVE_PERCENT', 'FREE_COFFEE', 'FREE_POPCORN');
CREATE TYPE transaction_type AS ENUM ('TICKET_PURCHASE', 'CAFETERIA_ORDER');

-- Users Table
CREATE TABLE Users (
    UserID UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    Name VARCHAR(255) NOT NULL,
    NIF VARCHAR(9) NOT NULL,
    CreditCardType VARCHAR(50) NOT NULL,
    CreditCardNumber VARCHAR(16) NOT NULL,
    CreditCardValidity VARCHAR(5) NOT NULL, -- MM/YY
    PublicKey VARCHAR(255) NOT NULL
);

-- Shows Table
CREATE TABLE Shows (
    ShowID SERIAL PRIMARY KEY,
    Name VARCHAR(255) NOT NULL,
    Description TEXT,
    Picture VARCHAR(255),
    Price INT NOT NULL,
    Duration INT NOT NULL, -- Duration in minutes
    ReleaseDate DATE NOT NULL
);

-- ShowsDates Table
CREATE TABLE ShowDates (
    ShowDateID SERIAL PRIMARY KEY,       -- Unique identifier for the show date
    ShowID INT REFERENCES Shows(ShowID),
    Date DATE NOT NULL,
    AvailableSeats INT NOT NULL
);

-- Tickets Table
CREATE TABLE Tickets (
    TicketID UUID PRIMARY KEY DEFAULT gen_random_uuid(), -- Unique identifier for the ticket
    UserID UUID REFERENCES Users(UserID),                -- User that bought the ticket
    ShowDateID INT REFERENCES ShowDates(ShowDateID),     -- Reference to the show date
    Seat VARCHAR(3) NOT NULL,                            -- Seat
    IsUsed BOOLEAN NOT NULL DEFAULT FALSE
);

-- Transactions Table
CREATE TABLE Transactions (
    TransactionID UUID PRIMARY KEY DEFAULT gen_random_uuid(),  -- Unique identifier for the transaction
    TransactionType transaction_type NOT NULL,                 -- Type of transaction
    UserID UUID REFERENCES Users(UserID),                      -- User that made the transaction
    Total INT NOT NULL                              -- Total amount of the transaction
);

-- Ticket Transaction Table
CREATE TABLE TicketTransactions (
    TransactionID UUID REFERENCES Transactions(TransactionID),  -- Transaction that generated the ticket
    TicketID UUID REFERENCES Tickets(TicketID),
    NumberOfTickets INT NOT NULL
);

-- Cafeteria Order Transaction Table
CREATE TABLE CafeteriaTransactions (
    RedundantOrderID UUID PRIMARY KEY DEFAULT gen_random_uuid(), -- Unique identifier for the cafeteria order (only needed bc cafeteriaorderitem needs to reference this a primary key)
    TransactionID UUID REFERENCES Transactions(TransactionID),   -- Transaction that generated the cafeteria order
    OrderNumber INT NOT NULL, -- To be used by the user and the cafeteria to identify the order in a screen
    NumberOfItems INT NOT NULL -- Number of items in the order
);

-- Cafeteria Order Items Table
CREATE TABLE CafeteriaOrderItem (
    CafeteriaTransactionID UUID REFERENCES CafeteriaTransactions(RedundantOrderID), -- Cafeteria order that generated the item
    ItemName VARCHAR(255) NOT NULL,                            -- Name of the item
    Price INT NOT NULL,
    Duration INT NOT NULL, -- Duration in minutes
    ReleaseDate DATE NOT NULL
);


-- Vouchers Table
CREATE TABLE Vouchers (
    VoucherID UUID PRIMARY KEY DEFAULT gen_random_uuid(),               -- Unique identifier for the voucher
    UserID UUID REFERENCES Users(UserID),                               -- User that owns the voucher
    TransactionIDGenerated UUID REFERENCES Transactions(TransactionID),  -- Transaction that generated the voucher
    TransactionIDUsed UUID REFERENCES Transactions(TransactionID),       -- Transaction that used the voucher
    VoucherType voucher_type NOT NULL,                                  -- Type of voucher
    IsUsed BOOLEAN DEFAULT FALSE
);


-- INSERTING DATA
-- Insert shows into Shows table
INSERT INTO Shows (name, description, picture, price, duration, releasedate)
VALUES
    ('Hamilton', 'Award-winning Broadway musical about the life of Alexander Hamilton', 'hamilton.jpg', 25, 165, '2015-02-17'),
    ('The Lion King', 'Musical based on the Disney animated film', 'lion_king.jpg', 28, 88, '1997-08-07'),
    ('Wicked', 'Musical retelling of the Wizard of Oz story from the perspective of the Wicked Witch of the West', 'wicked.jpg', 17, 165, '2003-05-28'),
    ('Les Misérables', 'Musical based on the novel by Victor Hugo', 'les_miserables.jpg', 19, 170, '1980-09-24'),
    ('Phantom of the Opera', 'Classic musical by Andrew Lloyd Webber', 'phantom_of_the_opera.jpg', 23, 143, '2004-12-10'),

    ('The Book of Mormon', 'Musical comedy about Mormon missionaries', 'book_of_mormon.jpg', 27, 150, '2011-03-24'),
    ('Chicago', 'Musical set in the Roaring Twenties', 'chicago.jpg', 19,  120, '1975-06-03'),
    ('Mamma Mia!', 'Musical featuring the music of ABBA', 'mamma_mia.jpg', 40, 100, '1999-04-06'),
    ('Cats', 'Musical composed by Andrew Lloyd Webber', 'cats.jpg', 16, 90, '1981-05-11');

-- Insert random dates for showid 1 (Hamilton)
INSERT INTO ShowDates (showid, date, availableseats)
VALUES
    (1, '2024-03-15', 500),
    (1, '2024-03-20', 500),
    (1, '2024-03-25', 500);

-- Insert random dates for showid 2 (The Lion King)
INSERT INTO ShowDates (showid, date, availableseats)
VALUES
    (2, '2024-03-20', 250),
    (2, '2024-04-05', 250),
    (2, '2024-04-15', 250);

-- Insert random dates for showid 3 (Wicked)
INSERT INTO ShowDates (showid, date, availableseats)
VALUES
    (3, '2024-03-25', 1000),
    (3, '2024-04-20', 1000),
    (3, '2024-05-10', 1000);

-- Insert random dates for showid 4 (Les Misérables)
INSERT INTO ShowDates (showid, date, availableseats)
VALUES
    (4, '2024-04-01', 750),
    (4, '2024-04-25', 750),
    (4, '2024-05-10', 750);

-- Insert random dates for showid 5 (Phantom of the Opera)
INSERT INTO ShowDates (showid, date, availableseats)
VALUES
    (5, '2024-04-10', 250),
    (5, '2024-04-25', 250),
    (5, '2024-05-01', 250);

-- Insert random dates for showid 6 (The Book of Mormon)
INSERT INTO ShowDates (showid, date, availableseats)
VALUES
    (6, '2024-04-15', 500),
    (6, '2024-04-20', 500),
    (6, '2024-05-10', 500);

-- Insert random dates for showid 7 (Chicago)
INSERT INTO ShowDates (showid, date, availableseats)
VALUES
    (7, '2024-04-20', 750),
    (7, '2024-04-25', 750),
    (7, '2024-05-01', 750);

-- Insert random dates for showid 8 (Mamma Mia!)
INSERT INTO ShowDates (showid, date, availableseats)
VALUES
    (8, '2024-04-25', 1000),
    (8, '2024-05-01', 1000),
    (8, '2024-05-10', 1000);

-- Insert random dates for showid 9 (Cats)
INSERT INTO ShowDates (showid, date, availableseats)
VALUES
    (9, '2024-05-01', 750),
    (9, '2024-05-10', 750),
    (9, '2024-05-20', 750);


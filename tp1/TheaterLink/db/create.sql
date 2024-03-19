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
    Price INT NOT NULL
);

-- ShowsDates Table
CREATE TABLE ShowDates (
    ShowDateID SERIAL PRIMARY KEY,
    ShowID INT REFERENCES Shows(ShowID),
    Date DATE NOT NULL,
    AvailableSeats INT NOT NULL
);

-- Tickets Table
CREATE TABLE Tickets (
    TicketID SERIAL PRIMARY KEY,
    UserID UUID REFERENCES Users(UserID),
    ShowID INT REFERENCES Shows(ShowID),
    SeatNumber INT,
    IsUsed BOOLEAN DEFAULT FALSE
);

-- Vouchers Table
CREATE TABLE Vouchers (
    VoucherID SERIAL PRIMARY KEY,
    UserID UUID REFERENCES Users(UserID),
    VoucherType VARCHAR(50) NOT NULL,
    IsUsed BOOLEAN DEFAULT FALSE
);

-- Transactions Table
CREATE TABLE Transactions (
    TransactionID SERIAL PRIMARY KEY,
    UserID UUID REFERENCES Users(UserID),
    TransactionType VARCHAR(50) NOT NULL,
    Date DATE NOT NULL,
    TotalAmount DECIMAL(10, 2) NOT NULL
);


-- INSERTING DATA
-- Insert shows into Shows table
INSERT INTO Shows (name, description, picture, price)
VALUES
    ('Hamilton', 'Award-winning Broadway musical about the life of Alexander Hamilton', 'hamilton.jpg', 25),
    ('The Lion King', 'Musical based on the Disney animated film', 'lion_king.jpg', 28),
    ('Wicked', 'Musical retelling of the Wizard of Oz story from the perspective of the Wicked Witch of the West', 'wicked.jpg', 17),
    ('Les Misérables', 'Musical based on the novel by Victor Hugo', 'les_miserables.jpg', 19),
    ('Phantom of the Opera', 'Classic musical by Andrew Lloyd Webber', 'phantom_of_the_opera.jpg', 23),
    ('The Book of Mormon', 'Musical comedy about Mormon missionaries', 'book_of_mormon.jpg', 27),
    ('Chicago', 'Musical set in the Roaring Twenties', 'chicago.jpg', 19),
    ('Mamma Mia!', 'Musical featuring the music of ABBA', 'mamma_mia.jpg', 40),
    ('Cats', 'Musical composed by Andrew Lloyd Webber', 'cats.jpg', 16),
    ('The Phantom of the Opera', 'Classic musical about a mysterious masked man', 'phantom_of_the_opera_2.jpg', 18);

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

-- Insert random dates for showid 10 (The Phantom of the Opera)
INSERT INTO ShowDates (showid, date, availableseats)
VALUES
    (10, '2024-05-10', 500),
    (10, '2024-05-20', 500),
    (10, '2024-06-01', 500);

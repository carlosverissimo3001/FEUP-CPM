import psycopg2
import random, string

## GETTERS, no filters

def get_users(conn: psycopg2.extensions.connection):
    """
    Get all users from the database

    :param psycopg2.extensions.connection conn: connection to the database

    :return: list of tuples
    """
    cur = conn.cursor()

    cur.execute('''
        SELECT json_build_object(
            'userid', userid,
            'name', name,
            'nif', nif,
            'creditcardtype', creditcardtype,
            'creditcardnumber', creditcardnumber,
            'creditcardvalidity', creditcardvalidity,
            'publickey', publickey
        ) FROM users
    ''')

    rows = cur.fetchall()
    data = []

    for row in rows:
        data.append(row[0])

    return data

def get_shows(conn: psycopg2.extensions.connection):
    """
    Get all shows from the database

    :param psycopg2.extensions.connection conn: connection to the database

    :return: list of tuples
    """
    cur = conn.cursor()

    cur.execute('''
        SELECT json_build_object(
            'showid', showid,
            'name', name,
            'description', description,
            'picture', picture,
            'price', price
        ) FROM shows
    ''')

    rows = cur.fetchall()

    data = []

    for row in rows:
        data.append(row[0])

    return data

## CHECK IF USER EXISTS
def check_user_exists(conn: psycopg2.extensions.connection, nif: str):
    """
    Check if a user exists in the database

    :param psycopg2.extensions.connection conn: connection to the database
    :param str nif: user's nif

    :return: bool
    """
    cur = conn.cursor()

    cur.execute('''
        SELECT json_build_object(
            'userid', userid,
            'name', name,
            'nif', nif,
            'creditcardtype', creditcardtype,
            'creditcardnumber', creditcardnumber,
            'creditcardvalidity', creditcardvalidity,
            'publickey', publickey
        ) FROM users WHERE nif = %s
    ''', (nif,))

    return cur.fetchone()

## ADD USER
def add_user(conn: psycopg2.extensions.connection, user_id, name, nif, card, public_key):
    """
    Add a user to the database

    :param psycopg2.extensions.connection conn: connection to the database
    :param user.User user: user to be added to the database
    """
    print(f"user_id: {user_id}")
    card_type = card.get('type')
    card_number = card.get('number')
    card_expiration_date = card.get('expiration_date')

    user_id_str = str(user_id)

    cur = conn.cursor()

    try:
        cur.execute('''
            INSERT INTO users (userid, name, nif, creditcardtype, creditcardnumber, creditcardvalidity, publickey)
            VALUES (%s, %s, %s, %s, %s, %s, %s)
        ''', (user_id_str, name, nif, card_type, card_number, card_expiration_date, public_key))
        conn.commit()
        return True

    except psycopg2.Error as e:
        print("Error inserting user: ", e)
        conn.rollback() # Rollback the transaction
        return False

## GET SHOW DETAILS BY ID
def get_show(conn: psycopg2.extensions.connection, show_id: str):
    """
    Get the details of a show given its id

    :param psycopg2.extensions.connection conn: connection to the database
    :param str show_id: show's id

    :return: tuple
    """
    cur = conn.cursor()

    cur.execute('''
        SELECT json_build_object(
            'showid', showid,
            'name', name,
            'description', description,
            'picture', picture,
            'price', price
        ) FROM shows WHERE showid = %s
    ''', (show_id,))

    row = cur.fetchone()

    if row is None:
        return None

    return row

def get_show_dates(conn, show_id: str):
    """
    Get the dates available for a show given its id

    :param psycopg2.extensions.connection conn: connection to the database
    :param str show_id: show's id

    :return: list of tuples
    """
    cur = conn.cursor()

    cur.execute('''
        SELECT json_build_object(
            'date', date,
            'availableseats', availableseats
        ) FROM showdates WHERE showid = %s
    ''', (show_id,))

    rows = cur.fetchall()

    data = []
    for row in rows:
        data.append(row[0])

    return data

def get_user_by_user_id(conn: psycopg2.extensions.connection, user_id: str):
    """
    Get a user by its user_id

    :param psycopg2.extensions.connection conn: connection to the database
    :param str user_id: user's id

    :return: tuple
    """
    cur = conn.cursor()

    cur.execute('''
        SELECT json_build_object(
            'userid', userid,
            'name', name,
            'nif', nif,
            'creditcardtype', creditcardtype,
            'creditcardnumber', creditcardnumber,
            'creditcardvalidity', creditcardvalidity,
            'publickey', publickey
        ) FROM users WHERE userid = %s
    ''', (user_id,))

    row = cur.fetchone()

    if row is None:
        return None

    return row

def create_ticket(conn: psycopg2.extensions.connection, user_id: str, show_date_id: int):
    """
    Create a ticket for a user

    :param psycopg2.extensions.connection conn: connection to the database
    :param str user_id: user's id
    :param str show_date_id: show date's id
    """
    cur = conn.cursor()

    # 30 seats per row
    seat_number = random.randint(1, 30)

    # 15 rows
    seat_row = random.choice(string.ascii_uppercase[:15])

    seat = f"{seat_number}{seat_row}"

    try:
        cur.execute('''
            INSERT INTO tickets (userid, showdateid, seat)
            VALUES (%s, %s, %s)
            RETURNING *
        ''', (user_id, show_date_id, seat))
        conn.commit()

        return cur.fetchone()

    except psycopg2.Error as e:
        print("Error inserting ticket: ", e)
        conn.rollback() # Rollback the transaction
        return None

def get_user_tickets(conn: psycopg2.extensions.connection, user_id: str):
    """
    Get all tickets for a user

    :param psycopg2.extensions.connection conn: connection to the database
    :param str user_id: user's id

    :return: list of tuples
    """
    cur = conn.cursor()

    # select the tickets for the user
    cur.execute('''
        SELECT json_build_object(
            'ticketid', tickets.ticketid,
            'userid', tickets.userid,
            'showName', shows.name,
            'seat', tickets.seat,
            'date', showdates.date
        ) FROM tickets join showdates on tickets.showdateid = showdates.showdateid join shows on showdates.showid = shows.showid WHERE userid = %s
    ''', (user_id,))

    rows = cur.fetchall()

    data = []
    for row in rows:
        data.append(row[0])

    return data

def get_ticket_by_ticket_id(conn: psycopg2.extensions.connection, ticket_id: int):
    """
    Get all tickets for a user

    :param psycopg2.extensions.connection conn: connection to the database
    :param str user_id: user's id

    :return: list of tuples
    """
    cur = conn.cursor()

    # select the tickets for the user
    cur.execute('''
        SELECT json_build_object(
            'ticketid', tickets.ticketid,
            'userid', tickets.userid,
            'showName', shows.name,
            'seat', tickets.seat,
            'date', showdates.date
        ) FROM tickets join showdates on tickets.showdateid = showdates.showdateid join shows on showdates.showid = shows.showid WHERE ticketid = %s
    ''', (ticket_id,))

    row = cur.fetchone()

    if row is None:
        return None

    return row

def create_voucher(conn: psycopg2.extensions.connection, user_id: str, voucher_type: str):
    """
    Create a voucher for a ticket

    :param psycopg2.extensions.connection conn: connection to the database
    :param str user_id: user's id
    :param str voucher_type: voucher's type (FIVE_PERCENT, FREE_COFFEE, FREE_POPCORN)
    """
    cur = conn.cursor()

    try:
        cur.execute('''
            INSERT INTO vouchers (userid, vouchertype)
            VALUES (%s, %s)
            RETURNING json_build_object(
                'voucherid', voucherid,
                'userid', userid,
                'vouchertype', vouchertype
            )
        ''', (user_id, voucher_type))
        conn.commit()

        return cur.fetchone()

    except psycopg2.Error as e:
        print("Error inserting voucher: ", e)
        conn.rollback() # Rollback the transaction
        return None
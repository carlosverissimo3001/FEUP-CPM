import psycopg2
import sys

## GETTERS, no filters

def get_users(conn: psycopg2.extensions.connection):
    """
    Get all users from the database

    :param psycopg2.extensions.connection conn: connection to the database

    :return: list of tuples
    """
    cur = conn.cursor()

    cur.execute('''
        SELECT * FROM users
    ''')

    return cur.fetchall()

def get_shows(conn: psycopg2.extensions.connection):
    """
    Get all shows from the database

    :param psycopg2.extensions.connection conn: connection to the database

    :return: list of tuples
    """
    cur = conn.cursor()

    cur.execute('''
        SELECT * FROM shows
    ''')

    return cur.fetchall()

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
        SELECT * FROM users WHERE nif = %s
    ''', (nif,))

    return cur.fetchone() is not None

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



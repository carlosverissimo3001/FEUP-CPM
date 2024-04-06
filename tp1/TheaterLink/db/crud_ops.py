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
            'price', price,
            'duration', duration,
            'releasedate', releasedate
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
def add_user(conn: psycopg2.extensions.connection, name, nif, card, public_key):
    """
    Add a user to the database

    :param psycopg2.extensions.connection conn: connection to the database
    :param user.User user: user to be added to the database
    """
    card_type = card.get('type')
    card_number = card.get('number')
    card_expiration_date = card.get('expiration_date')


    cur = conn.cursor()

    try:
        cur.execute('''
            INSERT INTO users (name, nif, creditcardtype, creditcardnumber, creditcardvalidity, publickey)
            VALUES (%s, %s, %s, %s, %s, %s)
            RETURNING *
        ''', (name, nif, card_type, card_number, card_expiration_date, public_key))
        conn.commit()

        return cur.fetchone()

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
            'price', price,
            'duration', duration
            'releasedate', releasedate
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
            'showdateid', showdateid
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
            'imagePath', shows.picture,
            'seat', tickets.seat,
            'date', showdates.date,
            'isUsed', tickets.isUsed
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

def create_voucher(conn: psycopg2.extensions.connection, user_id: str, voucher_type: str, gen_trans_id: str):
    """
    Create a voucher for a ticket

    :param psycopg2.extensions.connection conn: connection to the database
    :param str user_id: user's id
    :param str voucher_type: voucher's type (FIVE_PERCENT, FREE_COFFEE, FREE_POPCORN)
    :param gen_trans_id: transaction id that generated the voucher
    """
    cur = conn.cursor()

    try:
        cur.execute('''
            INSERT INTO vouchers (userid, vouchertype, TransactionIDGenerated)
            VALUES (%s, %s, %s)
            RETURNING json_build_object(
                'voucherid', voucherid,
                'userid', userid,
                'vouchertype', vouchertype

            )
        ''', (user_id, voucher_type, gen_trans_id))
        conn.commit()

        return cur.fetchone()

    except psycopg2.Error as e:
        print("Error inserting voucher: ", e)
        conn.rollback() # Rollback the transaction
        return None

def mark_voucher_as_used(conn: psycopg2.extensions.connection, voucher_id: int):
    """
    Mark a voucher as used

    :param psycopg2.extensions.connection conn: connection to the database
    :param int voucher_id: voucher's id
    """
    cur = conn.cursor()

    try:
        cur.execute('''
            UPDATE vouchers
            SET isUsed = TRUE
            WHERE voucherid = %s
            RETURNING *
        ''', (voucher_id,))
        conn.commit()

        return cur.fetchone()

    except psycopg2.Error as e:
        print("Error marking voucher as used: ", e)
        conn.rollback() # Rollback the transaction
        return None

def mark_ticket_as_used(conn: psycopg2.extensions.connection, ticket_id: int):
    """
    Mark a ticket as used

    :param psycopg2.extensions.connection conn: connection to the database
    :param int ticket_id: ticket's id
    """
    cur = conn.cursor()

    try:
        cur.execute('''
            UPDATE tickets SET isUsed = TRUE WHERE ticketid = %s
        ''', (ticket_id,))
        conn.commit()

        return True

    except psycopg2.Error as e:
        print("Error marking ticket as used: ", e)
        conn.rollback() # Rollback the transaction
        return False

def get_user_vouchers(conn: psycopg2.extensions.connection, user_id: str):
    """
    Get all vouchers for a user

    :param psycopg2.extensions.connection conn: connection to the database
    :param str user_id: user's id

    :return: list of tuples
    """
    cur = conn.cursor()

    # select the vouchers for the user
    cur.execute('''
        SELECT json_build_object(
            'voucherid', voucherid,
            'userid', userid,
            'vouchertype', vouchertype,
            'isUsed', isUsed
        ) FROM vouchers WHERE userid = %s
    ''', (user_id,))

    rows = cur.fetchall()

    data = []
    for row in rows:
        data.append(row[0])

    return data

def create_transaction(conn: psycopg2.extensions.connection, user_id: str, transaction_type: str, total_cost):
    """
    Create a transaction

    :param psycopg2.extensions.connection conn: connection to the database
    :param str type: transaction's type
    :param str user_id: user's id
    :param total_cost: total cost of the transaction

    :return: tuple
    """
    cur = conn.cursor()

    try:
        cur.execute('''
            INSERT INTO transactions (TransactionType, Total, UserID)
            VALUES (%s, %s, %s)
            RETURNING *
        ''', (transaction_type, total_cost, user_id))
        conn.commit()

        return cur.fetchone()

    except psycopg2.Error as e:
        print("Error inserting transaction: ", e)
        conn.rollback() # Rollback the transaction
        return None

def create_ticket_transaction(conn: psycopg2.extensions.connection, transaction_id: str, ticket_id: str, number_of_tickets: int):
    """
    Create a transaction for ticket purchases

    :param psycopg2.extensions.connection conn: connection to the database
    :param str transaction_id: transaction's id
    :param str ticket_id: ticket's id (if multiple tickets, the first ticket's id)
    :param int number_of_tickets: number of tickets purchased

    :return: tuple
    """
    cur = conn.cursor()

    try:
        cur.execute('''
            INSERT INTO tickettransactions (TransactionID, TicketID, NumberOfTickets)
            VALUES (%s, %s, %s)
            RETURNING *
        ''', (transaction_id, ticket_id, number_of_tickets))
        conn.commit()

        return cur.fetchone()

    except psycopg2.Error as e:
        print("Error inserting ticket transaction: ", e)
        conn.rollback()
        return None

def create_cafeteria_transaction(conn: psycopg2.extensions.connection, transaction_id, num_items):
    """
    Create a transaction for cafeteria purchases

    :param psycopg2.extensions.connection conn: connection to the database
    :param str user_id: user's id

    :return: tuple
    """
    cur = conn.cursor()

    orderNumber = random.randint(1, 1000)

    try:
        cur.execute('''
            INSERT INTO cafeteriatransactions (TransactionID, OrderNumber, NumberOfItems)
            VALUES (%s, %s, %s)
            RETURNING *
        ''', (transaction_id, orderNumber, num_items))
        conn.commit()

        return cur.fetchone()

    except psycopg2.Error as e:
        print("Error inserting cafeteria transaction: ", e)
        conn.rollback()
        return None

def add_cafeteria_order_item(conn: psycopg2.extensions.connection, transaction_id: str, item: str, quantity: int):
    """
    Add an item to a cafeteria order

    :param psycopg2.extensions.connection conn: connection to the database
    :param str transaction_id: transaction's id
    :param str item: item's name
    :param int quantity: quantity of the item

    :return: tuple
    """
    cur = conn.cursor()

    try:
        cur.execute('''
            INSERT INTO cafeteriaorderitem (CafeteriaTransactionID, Price, ItemName, Quantity)
            VALUES (%s, %s, %s, %s)
            RETURNING *
        ''', (transaction_id, 3, item, quantity))
        conn.commit()

        return cur.fetchone()

    except psycopg2.Error as e:
        print("Error inserting cafeteria order item: ", e)
        conn.rollback()
        return None

def set_voucher_used_transaction(conn: psycopg2.extensions.connection, voucher_id: str, transaction_id: str):
    """
    Set a voucher as used in a transaction

    :param psycopg2.extensions.connection conn: connection to the database
    :param str voucher_id: voucher's id
    :param str transaction_id: transaction's id

    :return: tuple
    """
    cur = conn.cursor()

    try:
        cur.execute('''
            UPDATE vouchers SET TransactionIDUsed = %s WHERE voucherid = %s
            RETURNING *
        ''', (transaction_id, voucher_id))
        conn.commit()

        return cur.fetchone()

    except psycopg2.Error as e:
        print("Error setting voucher as used: ", e)
        conn.rollback()
        return None

def get_cafeteria_transactions(conn: psycopg2.extensions.connection, user_id: str):
    """
    Get all cafeteria transactions for a user

    :param psycopg2.extensions.connection conn: connection to the database
    :param str user_id: user's id

    :return: list of tuples
    """

    cur = conn.cursor()

    try:
        cur.execute('''

            SELECT json_build_object(
                'transaction_id', transactions.transactionid,
                'total', total,
                'order_id', cafeteriatransactions.redundantorderid,
                'order_number', cafeteriatransactions.ordernumber
            )
            from transactions join cafeteriatransactions on transactions.transactionid = cafeteriatransactions.transactionid
            WHERE userid = %s AND transactiontype = 'CAFETERIA_ORDER'
        ''', (user_id,)
        )
        conn.commit()

        rows = cur.fetchall()

    except psycopg2.Error as e:
        print("Error getting user orders: ", e)
        conn.rollback()
        return None

    data = []
    for row in rows:
        data.append(row[0])

    return data

def get_cafeteria_order_items(conn: psycopg2.extensions.connection, order_id: str):
    """
    Get all items for a cafeteria order

    :param psycopg2.extensions.connection conn: connection to the database
    :param str order_id: order's id

    :return: list of tuples
    """

    cur = conn.cursor()

    try:
        cur.execute('''

            SELECT json_build_object(
                'item_name', itemname,
                'price', price,
                'quantity', quantity
            )
            from cafeteriaorderitem
            WHERE cafeteriatransactionid = %s
        ''', (order_id,)
        )
        conn.commit()

        rows = cur.fetchall()

    except psycopg2.Error as e:
        print("Error getting user orders: ", e)
        conn.rollback()
        return None

    data = []
    for row in rows:
        data.append(row[0])

    return data

def get_vouchers_used(conn: psycopg2.extensions.connection, transaction_id: str):
    """
    Get all vouchers used in a transaction

    :param psycopg2.extensions.connection conn: connection to the database
    :param str voucher_id: voucher's id

    :return: tuple
    """
    cur = conn.cursor()

    try:
        cur.execute('''
            SELECT json_build_object(
                'voucherid', voucherid,
                'vouchertype', vouchertype
            ) FROM vouchers WHERE transactionidused = %s
        ''', (transaction_id,))

        rows = cur.fetchall()

    except psycopg2.Error as e:
        print("Error getting voucher used transaction: ", e)
        conn.rollback()
        return None

    data = []
    for row in rows:
        data.append(row[0])

    return data


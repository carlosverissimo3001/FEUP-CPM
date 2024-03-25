import psycopg2

from flask import Flask, Blueprint, request, jsonify
from db import crud_ops

## ROUTE DEFINITIONS ##
def construct_blueprint(dbConn: psycopg2.extensions.connection):
    global DB_CONN
    DB_CONN = dbConn

    transaction_page = Blueprint('transaction_page', __name__)

    return transaction_page


## HANDLER FUNCTIONS ##
def put_transaction(transaction_type: str, user_id, total_cost: float):
    '''
    Top-level handler for transactions

    Parameters:
        type (str): type of transaction
        user_id (str): user's id
        total_cost (float): total cost of the transaction
    '''
    trans_id = None

    ## ADD TO TRANSACTIONS TABLE ###
    transaction_row = crud_ops.create_transaction(DB_CONN, user_id, transaction_type, total_cost)
    if transaction_row is None:
        return jsonify({'message': 'Error creating transaction!'})
    else:
        trans_id = transaction_row[0] # transaction_id

    return trans_id


def handle_ticket_purchase(trans_id: str, tickets: list):
    '''
    Handler for ticket purchases

    Parameters:
        trans_id (str): transaction id
        tickets (list): list of tickets purchased
    '''

    # if more than 1 ticket was purchased, the id of the first ticket is used
    ticket_id = tickets[0].get('ticketid')
    num_tickets = len(tickets)

    # add to ticket_transactions table
    ticket_trans_row = crud_ops.create_ticket_transaction(DB_CONN, trans_id, ticket_id, num_tickets)
    if ticket_trans_row is None:
        return jsonify({'message': 'Error creating ticket transaction!'})
    else:
        return ticket_trans_row[0] # transaction_id, same as trans_id in this case

def handle_cafeteria_order(data, vouchers, total_cost: float):
    '''
    Handler for cafeteria orders

    Parameters:
        data (dict): data for the transaction (cafeteria items)
        vouchers (list): list of vouchers generated during the transaction
        total_cost (float): total cost of the transaction
    '''
    print('Handling cafeteria orders...')

    return None
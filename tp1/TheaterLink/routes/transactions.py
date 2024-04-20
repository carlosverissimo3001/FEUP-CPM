import psycopg2

from flask import Flask, Blueprint, request, jsonify
from db import crud_ops

## ROUTE DEFINITIONS ##
def construct_blueprint(dbConn: psycopg2.extensions.connection):
    global DB_CONN
    DB_CONN = dbConn

    transaction_page = Blueprint('transaction_page', __name__)

    @transaction_page.route('/transactions', methods=['GET'])
    def get_user_transactions():
        user_id = request.args.get('user_id')

        transactions = crud_ops.get_user_transactions(DB_CONN, user_id)

        tickets = crud_ops.get_user_tickets(DB_CONN, user_id)
        vouchers = crud_ops.get_user_vouchers(DB_CONN, user_id)

        tickets = [ticket for ticket in tickets if ticket["isUsed"] == False]
        vouchers = [voucher for voucher in vouchers if voucher["isUsed"] == False]

        for transaction in transactions:
            transaction["vouchers_used"] = crud_ops.get_vouchers_used(DB_CONN, transaction["transaction_id"])
            transaction["vouchers_generated"] = crud_ops.get_vouchers_generated(DB_CONN, transaction["transaction_id"])

            ## TICKET PURCHASE ##
            if transaction["transaction_type"] == "TICKET_PURCHASE":
                ticket_transaction = crud_ops.get_ticket_transaction(DB_CONN, transaction["transaction_id"])[0]

                ticket_id = ticket_transaction.get("ticketid")
                num_tickets = ticket_transaction.get("numberoftickets")

                ticket = crud_ops.get_ticket_by_ticket_id(DB_CONN, ticket_id)[0]
                del ticket["ticketid"]
                del ticket["userid"]
                del ticket["seat"]
                ticket["num_tickets"] = num_tickets

                transaction["items"] = [ticket]

            ## CAFETERIA ORDER ##
            else:
                cafeteria_transaction = crud_ops.get_cafeteria_transaction(DB_CONN, transaction["transaction_id"])[0]
                orderid = cafeteria_transaction.get("orderid")

                order_items = crud_ops.get_cafeteria_order_items(DB_CONN, orderid)
                items = []

                for voucher_used in transaction["vouchers_used"]:
                    if voucher_used["vouchertype"] == "FREE_POPCORN":
                        items.append({"itemname": "(Free) Popcorn", "quantity": 1})
                    elif voucher_used["vouchertype"] == "FREE_COFFEE":
                        items.append({"itemname": "(Free) Coffee", "quantity": 1})

                for item in order_items:
                    items.append(item)

                transaction["items"] = items

            """ transactions["nif"] = crud_ops.get_user_nif(DB_CONN, user_id) """

        return jsonify({'message': 'Transactions retrieved successfully!', 'transactions':  transactions, 'tickets': tickets, 'vouchers': vouchers}), 200

    return transaction_page


## HANDLER FUNCTIONS ##
def put_transaction(transaction_type: str, user_id, total_cost):
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

def handle_cafeteria_order(trans_id, order):
    '''
    Handler for cafeteria orders

    Parameters:
        trans_id (str): transaction id
        order (dict): order details
    '''

    # add to the cafeteria_orders table
    items = order["items"]
    num_items = sum([item["quantity"] for item in items])


    ## add to the cafeteria transactions table
    order_row = crud_ops.create_cafeteria_transaction(DB_CONN, trans_id, num_items)
    if order_row is None:
        return jsonify({'message': 'Error creating cafeteria transaction!'})

    redundant_id = order_row[0] # used for FK constraint below
    order_no = order_row[2] # order_no

    for item in items:
        qnt = item["quantity"]
        price = item["price"]
        itemname = item["itemname"]

        # add to the cafeteria_orders table
        order_row = crud_ops.add_cafeteria_order_item(DB_CONN, redundant_id, itemname, qnt, price)
        if order_row is None:
            return jsonify({'message': 'Error creating cafeteria order item!'})

    return order_no
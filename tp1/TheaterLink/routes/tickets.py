from flask import Flask, render_template, request, redirect, url_for, jsonify, Blueprint
from db import crud_ops
from utils import utils
from routes import transactions

from cryptography.hazmat.backends import default_backend
from cryptography.hazmat.primitives import serialization
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.hazmat.primitives import hashes
import base64, psycopg2, random

VOUCHER_TYPE = ["FIVE_PERCENT", "FREE_COFFEE", "FREE_POPCORN"]


def construct_blueprint(dbConn: psycopg2.extensions.connection):
    ticket_page = Blueprint('ticket_page', __name__)


    @ticket_page.route('/purchase_tickets', methods=['POST'])
    def purchase_tickets():
        data = request.json

        # extract parameters from the data
        show_date_id = data.get('show_date_id')
        num_tickets = data.get('num_tickets')
        total_cost = data.get('total_cost')
        user_id = data.get('user_id')
        signature = data.get('signature')

        # Get the public key of the user
        public_key = utils.get_public_key(dbConn, user_id)

        # TODO: VERIFY SIGNATURE

        # need to add a row to the tickets table for each ticket
        # for each ticket purchased, a voucher is also created
        ticket_data = []
        voucher_data = []

        # Add a row to the transactions table
        # Don't distinguish between different types of transactions for now
        trans_id = transactions.put_transaction('TICKET_PURCHASE', user_id, total_cost)

        for _ in range(num_tickets):
            ## TICKET LOGIC ##
            ticket_row = crud_ops.create_ticket(dbConn, user_id, show_date_id)
            if ticket_row is None:
                return jsonify({'message': 'Error purchasing tickets!'})

            ticket = utils.get_full_ticket(dbConn, ticket_row[0])
            ticket_data.append(ticket)

            ## VOUCHER LOGIC ##
            vc_type = random.choice(VOUCHER_TYPE[1:])
            voucher_row = crud_ops.create_voucher(dbConn, user_id, vc_type, trans_id)

            if voucher_row is None:
                return jsonify({'message': 'Error purchasing tickets! Error creating voucher!'})

            voucher_data.append(voucher_row[0])

        # if the total cost is greater than 200, give a 5% discount
        if total_cost >= 200:
            vc_type = VOUCHER_TYPE[0]
            voucher_row = crud_ops.create_voucher(dbConn, user_id, vc_type, trans_id)

            if voucher_row is None:
                return jsonify({'message': 'Error purchasing tickets! Error creating voucher!'})

            voucher_data.append(voucher_row[0])

        ## HANDLE TICKET TRANSACTION ##
        if transactions.handle_ticket_purchase(trans_id, ticket_data) is None:
            return jsonify({'message': 'Error purchasing tickets! Error creating ticket transaction!'})

        return jsonify({'message': 'Tickets purchased successfully!', 'tickets': ticket_data, 'vouchers': voucher_data})


    @ticket_page.route('/get_tickets', methods=['GET'])
    def get_user_tickets():
        user_id = request.args.get('user_id')

        tickets = crud_ops.get_user_tickets(dbConn, user_id)
        active = request.args.get('active') == 'true'

        if active:
            tickets = [t for t in tickets if not t['isUsed']]

        return jsonify(tickets)

    # Will be called by the validation terminal app after the ticket is scanned
    @ticket_page.route('/validate_ticket', methods=['POST'])
    def validate_ticket():
        return NotImplementedError

    # Will be called by the validation terminal app after the ticket is validated
    # Might not have to be an actual endpoint, could be handled by the validate_ticket endpoint
    # TODO: Decide on the implementation
    @ticket_page.route('/set_ticket_as_used', methods=['POST'])
    def mark_ticket_as_used():
        data = request.json

        ticket_id = data.get('ticket_id')

        crud_ops.mark_ticket_as_used(dbConn, ticket_id)

        return jsonify({'message': 'Ticket marked as used!'})



    return ticket_page

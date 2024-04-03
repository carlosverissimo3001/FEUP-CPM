import psycopg2
from routes import transactions

from db import crud_ops
from flask import request, jsonify, Blueprint

VOUCHER_TYPE = ["FIVE_PERCENT", "FREE_COFFEE", "FREE_POPCORN"]

def construct_blueprint(dbConn: psycopg2.extensions.connection):
    cafeteria_page = Blueprint('cafeteria_page', __name__)

    @cafeteria_page.route('/submit_order', methods=['POST'])
    def submit_order():
        data = request.get_json()

        vouchers_used = data['vouchers_used']
        user_id = data['user_id']
        order = data['order']
        total = order['total']

        # create a transaction
        transaction_id = transactions.put_transaction('CAFETERIA_ORDER', user_id, total)

        for i in range(len(vouchers_used)):
            # mark vouchers as used
            mark_voucher_as_used(vouchers_used[i])
            # set in which transaction the vouchers were used
            crud_ops.set_voucher_used_transaction(dbConn, vouchers_used[i], transaction_id)

        # if the total cost is greater than 200, give a 5% discount
        if total >= 200:
            vc_type = VOUCHER_TYPE[0]
            voucher_row = crud_ops.create_voucher(dbConn, user_id, vc_type, transaction_id)

            if voucher_row is None:
                return jsonify({'message': 'Error purchasing tickets! Error creating voucher!'})

            """ voucher_data.append(voucher_row[0]) """

        ## HANDLE CAFETERIA ORDER ##
        if transactions.handle_cafeteria_order(transaction_id, order) is None:
            return jsonify({'message': 'Error submitting order!'})

        return jsonify({'message': 'Order submitted successfully!'}), 201


    def mark_voucher_as_used(voucher_id):
        crud_ops.mark_voucher_as_used(dbConn, voucher_id)

    return cafeteria_page
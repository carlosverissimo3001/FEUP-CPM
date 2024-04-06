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
            crud_ops.mark_voucher_as_used(dbConn, vouchers_used[i])

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
        order_no =  transactions.handle_cafeteria_order(transaction_id, order)
        if order_no is None:
            return jsonify({'message': 'Error submitting order!'})

        return jsonify({'message': 'Order submitted successfully!', 'order_no': order_no}), 200


    @cafeteria_page.route('/orders', methods=['GET'])
    def get_user_orders():
        user_id = request.args.get('user_id')

        orders = build_user_orders(user_id)

        return jsonify({'message': 'Orders retrieved successfully!', 'orders': orders }), 200


    def build_user_orders(user_id):
        ## GET CAFETERIA TRANSACTIONS ##
        caft_transactions = crud_ops.get_cafeteria_transactions(dbConn, user_id)

        for transaction in caft_transactions:
            transaction["items"] = crud_ops.get_cafeteria_order_items(dbConn, transaction["order_id"])
            transaction["vouchers"] = crud_ops.get_vouchers_used(dbConn, transaction["transaction_id"])

            for voucher in transaction["vouchers"]:
                ## free coffee
                if voucher["vouchertype"] == VOUCHER_TYPE[1]:
                    transaction["items"].append({"item_name": "Free Coffee", "price": 0, "quantity": 1})
                elif voucher["vouchertype"] == VOUCHER_TYPE[2]:
                    transaction["items"].append({"item_name": "Free Popcorn", "price": 0, "quantity": 1})

            transaction["vouchers"] = len(transaction["vouchers"])

        return caft_transactions

    return cafeteria_page
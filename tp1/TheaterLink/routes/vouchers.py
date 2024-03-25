import psycopg2

from db import crud_ops
from flask import request, jsonify, Blueprint

def construct_blueprint(dbConn: psycopg2.extensions.connection):
    voucher_page = Blueprint('voucher_page', __name__)

    @voucher_page.route('/get_user_vouchers', methods=['GET'])
    def get_user_vouchers():
        user_id = request.args.get('user_id')

        vouchers = crud_ops.get_user_vouchers(dbConn, user_id)

        return jsonify(vouchers)

    return voucher_page
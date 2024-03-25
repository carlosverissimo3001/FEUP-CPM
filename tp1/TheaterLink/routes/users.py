from flask import Flask, render_template, request, redirect, url_for, jsonify, Blueprint
from db import crud_ops
import uuid

def construct_blueprint(dbConn):
    user_page = Blueprint('user_page', __name__)

    @user_page.route('/users')
    def users():
        users = crud_ops.get_users(dbConn)

        return jsonify(users)

    @user_page.route('/register', methods=['POST'])
    def register_user():
        # Get data from the request
        data = request.json

        # extract parameters from the data
        # NIF is used for unique identification
        nif = data.get('nif')

        name = data.get('name')
        public_key = data.get('public_key')     #
        card = data.get('card')                 # assum card is a dictionary with the following keys: number, expiration_date, cvv

        # TODO: Decide if validation is going to be done by the server or in the client

        # Generate a unique user_id
        user_id = uuid.uuid4()

        # Add the user to the database
        sucess = crud_ops.add_user(dbConn, user_id, name, nif, card, public_key)

        if not sucess:
            return jsonify({'error': 'Error adding user to the database'}), 500
        else:
            print('User added successfully')


        # Return the user_id with success message
        return jsonify({'user_id': user_id, 'message': 'User added successfully'}), 201

    # given a user id, return the user details
    @user_page.route('/get_user', methods=['POST'])
    def get_name():
        data = request.json

        user_id = data.get('user_id')

        user = crud_ops.get_user_by_user_id(dbConn, user_id)

        if user is None:
            return jsonify({'error': 'User not found'}), 404
        else:
            user = user[0]

        return jsonify({'name': user["name"]}), 200

    return user_page
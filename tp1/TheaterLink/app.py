from flask import Flask, render_template, request, redirect, url_for, jsonify
import db.init_db as init_db
import db.crud_ops as crud_ops
import sys
import uuid

app = Flask(__name__)
dbConn = init_db.connect_to_db()

@app.route('/users')
def users():
    users = crud_ops.get_users(dbConn)

    return "Welcome " + str(users)

@app.route('/shows')
def shows():
    shows = crud_ops.get_shows(dbConn)

    return shows

@app.route('/')
def index():
    return 'Welcome to the Ticketing Server!'


@app.route('/register', methods=['POST'])
def register_user():
    # Get data from the request
    data = request.json()

    # extract parameters from the data
    # NIF is used for unique identification
    nif = data.get('nif')

    if crud_ops.check_user_exists(dbConn, nif):
        return jsonify({'error': 'User already exists'}), 400

    name = data.get('name')
    public_key = data.get('public_key')     #
    card = data.get('card')                 # assum card is a dictionary with the following keys: number, expiration_date, cvv

    # TODO: Decide if validation is going to be done by the server or in the client

    # Add the user to the database
    crud_ops.add_user(dbConn, name, nif, card, public_key)

    # Generate unique UUID for the user (16bytes)
    user_id = uuid.uuid4().bytes[:16]

    # Return the user_id with success message
    return jsonify({'user_id': user_id.hex(), 'message': 'User added successfully'}), 201

if __name__ == '__main__':
    app.run(debug=True)

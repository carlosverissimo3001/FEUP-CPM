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

    return {'users' : users}

@app.route('/shows')
def shows():
    shows = crud_ops.get_shows(dbConn)

    return {'shows' : shows}

# given a show_id, return the show details and dates available
@app.route('/shows/<show_id>')
def show_details(show_id):
    show = crud_ops.get_show(dbConn, show_id)[0]

    dates = crud_ops.get_show_dates(dbConn, show_id)

    show["dates"] = dates

    return {'show' : show }


@app.route('/')
def index():
    return 'Welcome to the Ticketing Server!'

@app.route('/pre-register', methods=['POST'])
def pre_register_user():
    # Get data from the request
    data = request.json
    nif = data.get('nif')

    row = crud_ops.check_user_exists(dbConn, nif)

    if row is not None:
        user = row[0]
        user_id = user["userid"]
        return jsonify({'user_id': user_id, 'message': 'User already exists'}), 200

    else:
         return jsonify({'message': 'New user'}), 204

@app.route('/register', methods=['POST'])
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


if __name__ == '__main__':
    app.run(debug=True)

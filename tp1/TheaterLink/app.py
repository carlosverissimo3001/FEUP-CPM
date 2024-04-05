from flask import Flask, render_template, request, redirect, url_for, jsonify
from routes import shows, users, tickets, vouchers, transactions, cafeteria

import db.init_db as init_db


app = Flask(__name__)

# Connect to the database
dbConn = init_db.connect_to_db()

# Register the many blueprints
app.register_blueprint(shows.construct_blueprint(dbConn))
app.register_blueprint(users.construct_blueprint(dbConn))
app.register_blueprint(tickets.construct_blueprint(dbConn))
app.register_blueprint(vouchers.construct_blueprint(dbConn))
app.register_blueprint(transactions.construct_blueprint(dbConn))
app.register_blueprint(cafeteria.construct_blueprint(dbConn))

@app.route('/')
def index():
    return 'Welcome to the Ticketing Server!'


if __name__ == '__main__':
    app.run(debug=True)

import os
import psycopg2
import sys
from dotenv import load_dotenv

load_dotenv()

def connect_to_db():
    """
    Connect to the postgres database

    :return psycopg2.extensions.connection: connection to the database
    """
    conn = psycopg2.connect(
        host=os.getenv('DB_HOST'),
        database=os.getenv('DB_NAME'),
        user=os.getenv('DB_USER'),
        password=os.getenv('DB_PASSWORD'),
        port=5432
    )

    # Return the connection
    return conn




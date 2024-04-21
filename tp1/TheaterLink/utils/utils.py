import psycopg2, base64, rsa
from db import crud_ops
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.hazmat.primitives import hashes
from cryptography.hazmat.primitives.serialization import load_der_public_key
from cryptography.hazmat.primitives.asymmetric import rsa

def get_public_key(dbConn: psycopg2.extensions.connection, user_id: str, ) -> str:
    '''
    Get the public key of a user

        Parameters:
            dbConn (psycopg2.extensions.connection): connection to the database
            user_id (str): user's id

        Returns:
            str: user's public key
    '''
    user = crud_ops.get_user_by_user_id(dbConn, user_id)

    if user is None:
        return None
    else:
        user = user[0]

    return user.get('publickey')

def get_full_ticket(dbConn: psycopg2.extensions.connection, ticket_id: str) -> dict:
    '''
    Get the full ticket information

        Parameters:
            dbConn (psycopg2.extensions.connection): connection to the database
            ticket_id (str): ticket's id

        Returns:
            dict: ticket's information
    '''
    ticket = crud_ops.get_ticket_by_ticket_id(dbConn, ticket_id)

    if ticket is None:
        return None

    return ticket[0]


def decode_public_key(base64_public_key: str) -> rsa.RSAPublicKey:
    """
    Decodes a Base64-encoded public key and returns an RSAPublicKey object.

    Args:
        base64_public_key (str): The Base64-encoded public key.

    Returns:
        rsa.RSAPublicKey: The decoded public key.
    """
    # Decode the Base64 string into bytes
    public_key_bytes = base64.b64decode(base64_public_key)

    # Load the public key from the DER-encoded bytes
    public_key = load_der_public_key(public_key_bytes)

    return public_key


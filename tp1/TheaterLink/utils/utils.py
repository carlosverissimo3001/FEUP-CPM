import psycopg2
import rsa
from db import crud_ops
from cryptography.hazmat.backends import default_backend
from cryptography.hazmat.primitives import serialization
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.hazmat.primitives import hashes
import base64

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

def decrypt_body(signature_str: str, public_key_str: str, message: str) -> bool :
    '''
    Decrypt the body of a message

        Parameters:
            cyphertext (str): cyphertext to decrypt
            public_key_str (str): public key to use

        Returns:
            bool: True if the signature is valid, False otherwise
    '''
    # Decode the Base64 string into a byte array
    public_key_bytes = base64.b64decode(public_key_str)

    # Construct the PEM-formatted public key string
    pem_public_key_str = "-----BEGIN PUBLIC KEY-----\n" + \
                            public_key_bytes + \
                            "\n-----END PUBLIC KEY-----"


    # Load the public key
    public_key = serialization.load_pem_public_key(
        public_key_bytes,
        backend=default_backend()
    )

    # Verify the signature
    signature = base64.b64decode(signature_str)

    try:
        public_key.verify(
            signature,
            message,
            padding.PKCS1v15(),
            hashes.SHA256()
        )
        print('Signature verified')
        return True
    except:
        print('Signature not verified')
        return False
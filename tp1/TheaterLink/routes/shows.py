import base64, os

from flask import Flask, render_template, request, redirect, url_for, jsonify, Blueprint, send_file
from db import crud_ops

IMAGES_PATH = os.path.join(os.path.dirname(os.path.abspath(__file__)), '../images/')


def construct_blueprint(dbConn):
    show_page = Blueprint('show_page', __name__)

    @show_page.route('/shows')
    def shows():
        shows = crud_ops.get_shows(dbConn)

        # Does the client needs the base64 encoded image? Or is the image path enough, i.e, client has cached the image
        needs_image = request.args.get('images')

        shows = crud_ops.get_shows(dbConn)

        for show in shows:
            show["dates"] = crud_ops.get_show_dates(dbConn, show['showid'])

            if needs_image == 'true':
                img_path = IMAGES_PATH + show['picture']

                with open(img_path, 'rb') as f:
                    img_data = f.read()

                # encode the image data to base64
                image_base64 = base64.b64encode(img_data).decode('utf-8')

                show['picture'] = image_base64

        return {"shows": shows}

    # given a show_id, return the show details and dates available
    @show_page.route('/shows/<show_id>')
    def show_details(show_id):
        show = crud_ops.get_show(dbConn, show_id)[0]

        dates = crud_ops.get_show_dates(dbConn, show_id)

        show["dates"] = dates

        return jsonify(show)

    return show_page

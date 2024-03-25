from flask import Flask, render_template, request, redirect, url_for, jsonify, Blueprint
from db import crud_ops


def construct_blueprint(dbConn):
    show_page = Blueprint('show_page', __name__)

    @show_page.route('/shows')
    def shows():
        shows = crud_ops.get_shows(dbConn)

        return {'shows' : shows}

    # given a show_id, return the show details and dates available
    @show_page.route('/shows/<show_id>')
    def show_details(show_id):
        show = crud_ops.get_show(dbConn, show_id)[0]

        dates = crud_ops.get_show_dates(dbConn, show_id)

        show["dates"] = dates

        return {'show' : show }

    return show_page

# TheaterConnect

This project was completed for the academic course of [Mobile Computing](https://sigarra.up.pt/feup/en/ucurr_geral.ficha_uc_view?pv_ocorrencia_id=518814), a first year course of the Master's in Computing and Informatics Engineering @FEUP.

Consult the project specifications [here](docs/specifications.pdf) 

## Group Composition

| Name | Email | GitHub |
| --- | --- | --- |
| Carlos Veríssimo | up201907716@up.pt | [carlosverissimo3001](https://github.com/carlosverissimo3001)
| Nuno Jesus | up201905477@up.pt | [Nuno-Jesus](https://github.com/Nuno-Jesus)

## Scenario

A music and event theater wants to provide to its customers an integrated system for easy acquisition and validation of tickets and cafeteria vouchers to be used on premises.

## Folder Structure

- [docs](./docs/): Documentation
- [TheaterLink](./TheaterLink/)
  - Backend for the application
  - Handles the database and the API
  - Written in Python using Flask
- [TheaterPal](./TheaterPal/)
  - Frontend for the application
  - Android app in Kotlin
- [TheaterValid8](./TheaterValid8/)
  - App to be run on the ticket validation terminal
  - Android app in Kotlin
- [TheaterBite](./TheaterBite/)
  - App to be run on the cafeteria ordering terminal
  - Android app in Kotlin

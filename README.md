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

## Features / How To Use

### Register

The first time a customer uses the system, they must register. This is done by providing their name, NIF, and credit card information. The credit card information is used to make the payments for the tickets and cafeteria orders.

The image below shows the registration screen:

<p align="center">
  <img src="/images/TheaterPal/register_activity.png" alt="Register Activity" width="300">
</p>

After the user registers, an RSA key pair is generated. The public key is sent to the server and stored in the database. The private key is stored in the user's device.

This step is only done once, and the user can then use the system without having to register again.

### Consult Shows

The customer, after registering, is presented with a list of shows that are available for purchase. The shows are retrieved from the server and displayed in the app.

The image below shows the list of shows:

<p align="center">
  <img src="/images/TheaterPaL/shows_fragment.png" alt="Shows Fragment" width="300">
</p>


### Purchase Tickets

By clicking on a show card, the user is presented with more information about the show, such as the description, price, and duration:

<p align="center">
  <img src="/images/TheaterPal/show_details_activity.png" alt="Show Details Activity" width="300">
</p>

From here, the user can select from a list of available dates and purchase tickets for the show (maximum of 4 tickets per purchase).

### Biometric Authentication

Before submitting the order, the user must authenticate using biometrics. This is done to ensure that the user is the one making the purchase.

> Note: As some phones don't provide either a fingerprint sensor or facial recognition, we also provide the option to authenticate using a PIN or pattern.

The authentication screen is shown below:

<p align="center">
  <img src="/images/TheaterPal/biometric_auth.jpg" alt="Biometric Authentication" width="300">
</p>

After the user authenticates, the order is submitted to the server, and the tickets are generated.

### Consult Tickets

The user can consult all the tickets they have purchased. This is done by clicking on the "Wallet" icon in the bottom navigation bar.

The image below shows the wallet screen:

<p align="center">
  <img src="/images/TheaterPal/wallet_fragment.png" alt="Wallet Fragment" width="300">
</p>

The user can also filter the tickets to show only the ones that are still valid.

The wallet screen has two tabs: one for the tickets and other for orders made in the cafeteria.

### Check items in the Cafeteria

The user can also check the items available in the cafeteria. This is done by clicking on the "Cafeteria" icon in the bottom navigation bar.

The image below shows the cafeteria screen:

<p align="center">
  <img src="/images/TheaterPal/cafeteria_fragment_products.png" alt="Cafeteria Fragment" width="300">
</p>


### Consult Available Vouchers

By swiping to the "Vouchers" tab in the cafeteria screen, the user can consult all the vouchers they have available:

<p align="center">
  <img src="/images/TheaterPal/cafeteria_fragment_vouchers.png" alt="Cafeteria Fragment" width="300">
</p>

The user can also filter the vouchers to show only the ones that are still valid.

### Order Food

To make an order in the cafeteria, the user first selects from the list of products:

#### Choosing Products

<p align="center">
  <img src="/images/TheaterPal/cafeteria_fragment_choosing_product.png" alt="Cafeteria Fragment" width="300">
</p>

Note that the button to submit the order is disabled until the user selects at least one item.
At any moment, the user can see the total price of the order.

#### Select Vouchers

After clicking on the "Next Step" button, the user is presented with a screen to select the vouchers they want to use:

<p align="center">
  <img src="/images/TheaterPal/cafeteria_fragment_choosing_vouchers.png" alt="Cafeteria Fragment" width="300">
</p>

There are some restriction in place:

- The user can only select up to 2 vouchers.
- If available, the user can select only one 5% discount voucher.

#### Validate Order

After selecting the vouchers, the user can click on the "Submit Order" button to generate the order and, go to the cafeteria validation screen.

<p align="center">
  <img src="/images/TheaterPal/cafeteria_NFC_validating_order.png" alt="Cafeteria Fragment" width="300">
</p>

At this point, NFC should be enabled. If it isn't, the user is prompted to enable it before proceeding.

After the validation is done, the user will see in the cafeteria terminal a screen that shows the result of the validation. It will show the order number that the customer should look for in the cafeteria.

<p align="center">
  <img src="/images/TheaterBite/nfc_result.png" alt="Cafeteria Fragment" width="300">
</p>

The terminal will show the items in the order and the total price, as well as the vouchers that were used.

### Consult Orders

After the order is submitted, the user can consult all the orders they have made in the cafeteria. This is done by clicking on the "Wallet" icon in the bottom navigation bar and swiping to the "Orders" tab.

<p align="center">
  <img src="/images/TheaterPal/wallet_fragment_orders.png" alt="Order Consult" width="300">
</p>

### Validate Tickets

#### Prepare Terminal (Server / Terminal)

In order to validate tickets, the terminal app should be set to accept tickets for a given show at a given date. The following screen is the first thing that the validator is presented with when opening the `TheaterValid8` app.

<p align="center">
  <img src="/images/TheaterValid8/1_main_activity.png" alt="Main Activity" width="300">
</p>

After selecting the show and date, the validator confirms the selection:

<p align="center">
  <img src="/images/TheaterValid8/2_main_activity_after_selection.png" alt="Main Activity" width="300">
</p>

And then the following screen is presented in the terminal, as has some information about the show.

<p align="center">
  <img src="/images/TheaterValid8/3_validator_activity.png" alt="Validator Activity" width="300">
</p>

To validate tickets, just click on the "Scan" button and the terminal is ready to receive the tickets.

<p align="center">
  <img src="/images/TheaterValid8/4_scanning_activity.png" alt="Scanning Activity" width="300">
</p>

#### Present Tickets (Client / Customer)

The user can also validate the tickets they have purchased. In the "Wallet" screen, the user can swipe to the "Tickets" tab and, by selecting a ticket, they can validate it.

<p align="center">
  <img src="/images/TheaterPal/wallet_fragment_choose_tickets.png" alt="Ticket Validation" width="300">
</p>

When the user selects a ticket, a button to validate it appears. At this stage, the user can chosoe up to 4 tickets to validate, at each time.

After clicking on the "Validate" button, the user is presented with a screen to tap their phone on the NFC terminal.

<p align="center">
  <img src="/images/TheaterPal/wallet_fragment_NFC_validating_tickets.png" alt="Ticket Validation" width="300">
</p>

#### Validation Result

After receving a tap from the customer, the terminal will show the result of the validation.

<p align="center">
  <img src="/images/TheaterValid8/5_validation_success.png" alt="Validation Success" width="300">

Keep in mind that the user can send up to 4 tickets at the same time, including different shows and dates.

The validation terminal is only set to accept tickets for a certain show and date. If the ticket is not valid, the user is presented with an error message.

<p align="center">
  <img src="/images/TheaterValid8/6_validation_failure.jpg" alt="Ticket Validation" width="300">
</p>

### Consult Transactions

At any given moment, the user can consult all the transactions they have made. To do this, go to the "Wallet" screen and click on the `Click here to see consult transactions` text.

<p align="center">
  <img src="/images/TheaterPal/wallet_fragment.png" alt="Transaction Consult" width="300">
</p>

Per the specifications, consulting the transctions will fetch the vouchers and tickets that are still not used by the customer. In this way used tickets and vouchers are deleted from the customer app, allowing the customer to recover some voucher transmitted by mistake in a previous order, or not yet transmitted, and get rid of used ones, if they are still there.

As this is a potentially heavy operation, the user is prompted to confirm if they want to fetch the transactions.

<p align="center">
  <img src="/images/TheaterPal/wallet_fragment_consult_transactions.png" alt="Transaction Consult" width="300">
</p>

If the user chooses to proceed, the transactions are fetched and displayed in a list:

<p align="center">
  <img src="/images/TheaterPal/wallet_fragment_transactions.png" alt="Transaction Consult" width="300">
</p>

Clicking on a transaction will show a receipt-like screen with all the details of the transaction:

<p align="center">
  <img src="/images/TheaterPal/wallet_fragment_receipt.png" alt="Transaction Details" width="300">
</p>

## Scenario Tests

An internet connection is only needed for three main operations:

1. Registering for the first time
2. Purchasing tickets
3. Consult all transactions

There are some operations that can be done offline, and only require an NFC equipped phone:

### Offline Ticket Validation

One of the main features of the system is the ability to validate tickets using NFC, even if the phone has no internet connection.

The scenario test is as follows:

1. The user buys tickets for a show. (With internet connection)
2. The server response contains the tickets and vouchers. The app caches the tickets and vouchers.
3. The user loses internet connection but still has the tickets and vouchers cached.
4. The user goes to the theater and tries to validate the tickets using NFC.
5. The validation is successful, and the user is allowed to enter the theater, even without internet connection

> the validation terminal needs internet connection to verify the tickets with the server.

### Offline Cafeteria Ordering

Another main feature of the system is the ability to order food in the cafeteria using NFC, even if the phone has no internet connection.

The scenario test is as follows:

1. The user chooses the items they want to order in the cafeteria.
2. They can add up to 2 vouchers to the order, even without internet connection, as they were cached when the user purhcased the tickets.
3. The user submits the order using NFC.
4. The order is successfully submitted, and the user is given an order number to look for in the cafeteria.

> The cafeteria terminal needs internet connection to submit the order to the server.

### User Authentication

Another scenario test is the user authentication. The user needs to authenticate using biometrics before purchasing tickets, as this is the only way to ensure that the user is the one making the purchase.

> To make a cafeteria order, this is not needed as the user needs to go up to the terminal to submit the order.

In order to support as many devices as possible, we also provide the option to authenticate using a PIN or pattern.

Thus, the authentication types are a Class 2 biometric `BIOMETRIC_WEAK`, that allows for both facial recognition and fingerprint, and a `DEVICE_CREDENTIAL` type that allows for PIN, pattern or password.

> If the user has not defined an authentication method, the app will prompt the user to do so and won't allow the user to proceed without it.

The authentication screen is shown below:

<p align="center">
  <img src="/images/TheaterPal/biometric_auth.jpg" alt="Biometric Authentication" width="300">
</p>

More info about biomtric authentication can be found [here](https://developer.android.com/training/sign-in/biometric-auth)

## Demo

> To be added soon
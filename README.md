# Mobile Clone API

This project is a clone of the [**mobile.de**](https://www.mobile.de/) backend, built using **Spring Boot**. The API simulates typical operations for managing vehicle listings such as creating, updating, deleting, and searching for cars, as well as operation for auctions and for bidding on them.

## Table of Contents
1. [Features](#features)
2. [Prerequisites](#prerequisites)
3. [API Endpoints](#api-endpoints)
4. [Technologies Used](#technologies-used)
5. [Contributing](#contributing)

## Features
- **CRUD operations** for all entities (User, Listing, Auction, Bid)
- **Search Functionality**: Search for vehicles based on different criteria (make, price range, model, etc.).
- **Automatic** winner establishment for auctions
- **Pagination** for all endpoints that return a list of entities
- **API Key Authentication** for all endpoints 

## Prerequisites
- **Java 17** or later
- **Maven** 3.6+
- A **PostgreSQL** database (or another relational DB supported by Spring JPA)

## API Endpoints
- **User**
    - `GET /users`: Get all users
    - `GET /users/{id}`: Get a user by ID
    - `POST /users`: Create a new user
    - `PUT /users/{id}`: Update a user
    - `DELETE /users/{id}`: Delete a user

- **Listing**
    - `GET /listings`: Get all listings
    - `GET /listings/{id}`: Get a listing by ID
    - `POST /listings`: Create a new listing
    - `PUT /listings/{id}`: Update a listing
    - `DELETE /listings/{id}`: Delete a listing
    - `GET /listings/search`: Search for listings based on different criteria

- **Auction**
    - `GET /auctions`: Get all auctions
    - `GET /auctions/{id}`: Get an auction by ID
    - `POST /auctions`: Create a new auction
    - `DELETE /auctions/{id}`: Delete an auction

- **Bid**
    - `GET /bids`: Get all bids belonging to a specific auction
    - `POST /bids`: Place a new bid
    - `DELETE /bids/{id}`: Delete a bid

## Technologies Used
- **Spring Boot**
- **Hibernate & Spring Data JPA** for data access
- **Lombok** for boilerplate code reduction
- **MapStruct** for mapping entities to DTOs
- **Jakarta Bean Validation** for input validation
- **JUnit 5 & Mockito** for unit testing
- **SpringBootTest & H2 Database** for integration testing
- **PostgreSQL** for the production database
- **Spring Security** for API key authentication

## Contributing
Pull requests are welcome.\
For major changes, please open an issue first to discuss what you would like to change.

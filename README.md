# Fulfillment Service

The Fulfillment Service is responsible for processing orders and updating the delivery status. It is called by the Order Service using REST.

## Overview

The Fulfillment Service is a critical component of the order management system. It receives orders from the Order Service, processes them, and manages the delivery status. This service ensures that orders are handled efficiently and delivered to customers on time.

## Features

- **Order Processing:** Accepts orders from the Order Service via REST endpoints and processes them according to business logic.
- **Delivery Status Management:** Tracks the delivery status of orders and updates it as necessary.
- **Integration with Order Service:** Communicates with the Order Service via REST to retrieve order details and update delivery status.

## Technologies Used

- **Java:** The service is implemented using the Java programming language.
- **Spring Boot:** Utilizes the Spring Boot framework for building and running the service.
- **RESTful API:** Exposes RESTful endpoints for communication with the Order Service.
- **PostgreSQL:** Stores data related to orders and delivery status in a PostgreSQL database.



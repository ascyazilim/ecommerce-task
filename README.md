# E-Commerce Task

This is an e-commerce backend project developed with Spring Boot, showcasing a simple e-commerce system that includes functionalities such as managing products, customers, carts, and orders.

## Technologies

- Spring Boot
- Spring Data JPA
- H2 Database
- Lombok

## Setup and Installation

### Prerequisites

- JDK 11 or later
- Maven 3.2+

### Running the Application Locally

1. Clone the repository to your local machine:
git clone https://github.com/ascyazilim/ecommerce-task.git
2. Navigate to the project directory:
mvn spring-boot:run
The application will start and be accessible at `http://localhost:8080`.

## API Endpoints Overview

The application exposes several RESTful endpoints:

### Customer Management

- **Add Customer**: `POST /api/customers`
- **Get All Customers**: `GET /api/customers`

### Product Management

- **Create Product**: `POST /api/products`
- **Update Product**: `PUT /api/products/{id}`
- **Delete Product**: `DELETE /api/products/{id}`
- **Get Product by ID**: `GET /api/products/{id}`
- **Get All Products**: `GET /api/products`

### Cart Operations

- **Get Cart for Customer**: `GET /api/carts/{customerId}`
- **Add Product to Cart**: `POST /api/carts/add`
- **Remove Product from Cart**: `POST /api/carts/remove`
- **Empty Cart**: `POST /api/carts/{cartId}/empty`

### Order Processing

- **Place an Order**: `POST /api/orders`
- **Get Order by ID**: `GET /api/orders/{orderId}`
- **List All Orders for a Customer**: `GET /api/orders/customer/{customerId}`

## Features

- **Product Management**: Create, update, and delete products.
- **Customer Management**: Add customers and retrieve their information.
- **Cart Management**: Add and remove products from the cart, update cart items, and empty the cart.
- **Order Processing**: Place orders and view order details.
- **Stock Management**: Track product stock levels.
- **Price Tracking**: Maintain historical pricing information for ordered products.

## Contributing

If you'd like to contribute to this project, please feel free to fork the repository and submit a pull request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

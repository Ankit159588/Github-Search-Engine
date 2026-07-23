# Detailed Project Documentation

This document provides a comprehensive overview of the `Learn-Spring-Boot` application structure, architecture, and configuration requirements.

## 1. Project Architecture

The application is designed using a modular approach, separating business logic into distinct domains:

- **OrderModule**: Handles order creation, item management, and status tracking.
- **PaymentModule**: Manages payment processing and verification through Razorpay.
- **ProductModule**: Manages product inventory and categories.
- **SecurityModule**: Implements JWT authentication, role management, and security configurations.
- **UserModule**: Defines the user entity and roles for authentication.

## 2. Configuration & Environment Variables

The project utilizes `dotenv-java` to manage environment-specific configurations. Ensure a `.env` file is present in the project root with the following keys (or set them as environment variables):

```env
# Database Configuration
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/your_db_name
SPRING_DATASOURCE_USERNAME=your_username
SPRING_DATASOURCE_PASSWORD=your_password

# Razorpay Configuration
RAZORPAY_KEY_ID=your_razorpay_key_id
RAZORPAY_KEY_SECRET=your_razorpay_key_secret

# Security/JWT Configuration
JWT_SECRET=your_jwt_signing_key
```

## 3. API Documentation

The project includes built-in OpenAPI/Swagger documentation. Once the application is running, you can access the interactive API explorer at:

`http://localhost:8080/swagger-ui.html` (or the configured context path)

## 4. Running the Application

### Prerequisites
- JDK 23
- Maven
- PostgreSQL running locally or in Docker

### Build and Run
1. Clone the repository.
2. Ensure the `.env` file is correctly configured.
3. Build the project:
   ```bash
   ./mvnw clean install
   ```
4. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

## 5. Security Details

- **JWT Authentication**: Secured endpoints require a valid Bearer token.
- **Filtering**: `JwtAuthenticationFilter` intercepts requests to validate the token before allowing access to protected resources.
- **Admin Initialization**: `AdminInitializer` ensures an initial administrator account is available.

# 👤 VStore - User Service

This is the **User Service** for the VStore microservices-based e-commerce platform. It handles all user-centric operations, including user registration, authentication (JWT), profile management, and role-based authorization.

This service registers itself with the **Eureka Server** and is accessible via the **API Gateway**. It is a core part of the [VStore project](https://github.com/your-username/VStore).

---

## ⚙️ Core Technologies

* **Backend**: Java 17, Spring Boot
* **Database**: MySQL
* **Security**: Spring Security, JWT
* **Service Discovery**: Spring Cloud Eureka Client

---

## 🔑 Environment Variables

* **`PORT`**: `8081`
* **`EUREKA_SERVER`**: URL of the Eureka registry.
* **`DB_URL`**: MySQL connection URL.
* **`DB_USERNAME`**: Database username.
* **`DB_PASSWORD`**: Database password.
* **`JWT_SECRET`**: Secret key for signing JWT tokens.
* **`JWT_EXPIRATION`**: Token expiration time in minutes.

---

## 🚀 API Endpoints

* **POST `/api/users/signup`**: Registers a new user. (Access: Public)
* **POST `/api/users/login`**: Authenticates a user and returns a JWT. (Access: Public)
* **GET `/api/users/me`**: Fetches the current user's profile. (Access: Authenticated)
* **PATCH `/api/users/update`**: Updates the current user's profile. (Access: Authenticated)
* **POST `/api/users/update/role`**: Updates the role of a specified user. (Access: ADMIN)
* **DELETE `/api/users/delete`**: Deletes a user account. (Access: ADMIN)

---

## ▶️ How to Run

1.  Ensure **MySQL** and the **VStore-EurekaServer** are running.
2.  Navigate to the `VStore-UserService/` directory.
3.  Run the service using Maven:
    ```bash
    mvn spring-boot:run
    ```
4.  The service will register with Eureka and be available on port `8081`.

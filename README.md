# Employee Management System (EMS) API

This repository contains a RESTful API for an Employee Management System (EMS) developed using Spring Boot. The API allows you to manage employee records, including creating, updating, deleting, and assigning supervisors.

## Technologies Used
- **Java**: 21
- **Spring Boot**: 3.4
- **PostgreSQL**: Database
- **Maven**: Build tool

## API Endpoints

### Employee Management

#### 1. Create Employee
- **Endpoint**: `POST /api/employees`
- **Request Body**:
    ```json
    {
      "firstName": "John",
      "lastName": "Doe",
      "email": "john.doe@example.com",
      "position": "Software Engineer"
    }
    ```
- **Validation Rules**:
    - `firstName` and `lastName`: Must not be null or empty.
    - `email`: Must be unique and in a valid email format.
    - `position`: Must not be null or empty.

#### 2. Update Employee
- **Endpoint**: `PUT /api/employees/{employeeId}`
- **Request Body**:
    ```json
    {
      "firstName": "John",
      "lastName": "Doe",
      "email": "john.doe@example.com",
      "position": "Senior Software Engineer"
    }
    ```
- **Validation Rules**:
    - Same as Create Employee.
    - `employeeId` must exist in the database.

#### 3. Get Employee by ID
- **Endpoint**: `GET /api/employees/{employeeId}`
- **Response**:
    ```json
    {
      "employeeId": 1,
      "firstName": "John",
      "lastName": "Doe",
      "email": "john.doe@example.com",
      "position": "Software Engineer",
      "createdAt": "2024-10-14T10:00:00"
    }
    ```

#### 4. Delete Employee
- **Endpoint**: `DELETE /api/employees/{employeeId}`
- **Validation Rules**:
    - `employeeId` must exist.
    - Employee must not have subordinates.

#### 5. Assign Supervisor
- **Endpoint**: `POST /api/employees/{employeeId}/assign-supervisor/{supervisorId}`
- **Validation Rules**:
    - Both `employeeId` and `supervisorId` must exist.
    - Cannot assign an employee as a supervisor of themselves.
    - **Prevent Circular Assignment**: An employee cannot supervise another employee who is their own supervisor, directly or indirectly.

#### 6. Get All Employees with Pagination
- **Endpoint**: `GET /api/employees?page=0&size=10`
- **Response**:
    ```json
    {
      "content": [
        {
          "employeeId": 1,
          "firstName": "John",
          "lastName": "Doe",
          "email": "john.doe@example.com",
          "position": "Software Engineer"
        }
      ],
      "totalElements": 100,
      "totalPages": 10,
      "size": 10,
      "number": 0
    }
    ```

#### 7. Get Employee Details (Supervisor and Subordinates)
- **Endpoint**: `GET /api/employees/{employeeId}/details`
- **Response**:
    ```json
    {
      "employee": {
        "employeeId": 1,
        "firstName": "John",
        "lastName": "Doe",
        "email": "john.doe@example.com",
        "position": "Software Engineer"
      },
      "supervisor": {
        "employeeId": 2,
        "firstName": "Jane",
        "lastName": "Smith",
        "email": "jane.smith@example.com",
        "position": "Manager"
      },
      "subordinates": [
        {
          "employeeId": 3,
          "firstName": "Alice",
          "lastName": "Johnson",
          "email": "alice.johnson@example.com",
          "position": "Intern"
        }
      ]
    }
    ```

## Running the Application

To run the application, ensure you have Docker installed and follow these steps:

1. **Clone the Repository**:
    ```bash
    git clone https://github.com/ramimohsen/Employee-Service.git
    cd <repository-directory>
    ```

2. **Start postgres container service from docker compose file **:

3. **Run maven install command to build the app image**
    - Run the following command:
    ```bash
       cd <repository-directory>
       mvn install  
    ```

4. **Start the Application**:
    - Run the following command to start the services:
    ```bash
    docker-compose up --build
    ```

5. **Access the Application**:
    - Once the application is up and running, you can access it at `http://localhost:8080/api/employees`.

## Swagger Documentation

After running the application, you can access the Swagger UI at:
- **URL**: `http://localhost:8080/swagger-ui/index.html`

## Validation Rules Summary
- **Required Fields**: All employee fields except for optional ones like `position` must be provided.
- **Email Validation**: Must be unique and follow standard email format.
- **Supervisor Assignment**: An employee cannot supervise themselves.
- **Prevent Circular Assignment**: An employee cannot be assigned as a supervisor of another employee who is already under their supervision, directly or indirectly.


## Database Management

This service uses **Liquibase** for database schema generation and data seeding. It automatically handles the creation of necessary tables and inserts initial data during application startup.


## License

This project is licensed under the MIT License.

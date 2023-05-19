# Login Server

This is a login server written in Java using the Spring framework. It provides APIs to create, update, delete, and retrieve user credentials from a H2 database. The server uses a JWT token for security and includes APIs to check if credentials are correct for a given user.

The server is built following the Ports and Adapters architecture, allowing it to work independently of the client application. In this example, it is used to connect a React Native app located in a separate repository.

## Installation

- Clone the repository to your local machine.
- Ensure you have Java 8 or later installed.
- Ensure you have a compatible version of the Spring framework installed.
- Install any additional dependencies required by your IDE or build tool.

## Usage

- Start the server by running the main method in the Application class.
- Connect to the server using the APIs provided by sending HTTP requests to the specified endpoint.
- Use the provided APIs to create, update, delete, and retrieve user credentials from the H2 database.
- Secure your requests by including the JWT token in the authorization header.

## APIs Endpoints

### User Endpoint
The /user endpoint provides APIs for creating, updating, and deleting users. Here are the available endpoints:

- POST /user: Creates a user with the provided user data. The request body must contain a UserData object. If the user is created successfully, the API returns a 200 OK status code and a message "User created successfully". If the email is already in use, the API returns a 401 Unauthorized status code and a message "Email already in use".

- DELETE /user/delete: Deletes the user with the provided user data. The request body must contain a UserData object. If the user is deleted successfully, the API returns a 200 OK status code and a message "User deleted successfully". If the user is not found, the API returns a 404 Not Found status code and a message with the exception message. If the credentials are invalid, the API returns a 401 Unauthorized status code and a message with the exception message.

- PATCH /user/update: Updates the user with the provided user data. The request body must contain a UserData object. If the user is updated successfully, the API returns a 200 OK status code and a message "User updated successfully". If the user is not found, the API returns a 404 Not Found status code and a message with the exception message. 
If the credentials are invalid, the API returns a 401 Unauthorized status code and a message with the exception message. If there is an unexpected error, the API returns a 500 Internal Server Error status code and a message "Error updating user".

### Credentials Endpoint
The /credentials endpoint provides APIs for resetting password and authenticating users. Here are the available endpoints:

- POST /credentials/forgot/{email}: Sends a reset email to the user with the provided email. If the email is valid, the API returns a 200 OK status code and a message "Reset email sent".
If the email is invalid, the API returns a 401 Unauthorized status code and a message "Invalid email".

- POST /credentials/login: Authenticates the user with the provided email and password. The request body must contain a UserLoginRequestDto object with the email and password. If the authentication is successful, the API returns a 200 OK status code, a JWT-token in the Authorization header, and a UserData object with the user's data. If the email or password is invalid, the API returns a 401 Unauthorized status code and a message "Invalid email or password".

## License

This project is licensed under the MIT License - see the LICENSE file for details.

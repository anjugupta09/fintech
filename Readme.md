# Fintech Application

1. I have created three submodules in single repo which will run as microservices all on different ports.
   There is a shared submodule, it contains the common functionality needed in these microservices.
2. JWT authentication logic in present in shared submodule. There are two API calls "New user sign up" and "login" which will not require JWT authentication.
   On calling the login API, access token will be generated and will populate in the API response.

3. Account service has API calls for user-registration and sign in functionality.
4. Transaction service has API calls for fund transfer and to get account balance.

5. According to microservice standard, account-service and transaction service should have separate database and transaction management should have been handled using SAGA Orchestration/Coordinator 
   which would have made this whole exercise pretty complicated. For simplicity, I decided to used single database for account as well as transaction service.

6. On successful user registration and fund transfer operations, a message will be published to Kafka topic, which will be consumed by the notification-service. Notification-service will send the notification
   via email/sms to the user. I have added the email functionality, it can be extended to many other channels like sms, social media as per requirement.

# Technologies used
Java, Spring boot, MySql, Kafka

# Steps to run the application
1. Goto shared folder in terminal and run the command "docker-compose up" without the quotes.
2. Now to run  account-service, go to account-service folder and run the command "gradle clean bootRun" without the quotes.
3. And similarly run the other two microservices transaction-service and notification-service

# Api End Points Details
1. User Registration- POST:/auth/signup
   This will register the user and user account in DB.

2. User login- POST:/auth/login
   After user registration, user can sign in using the credentials email and password. Access token will be generated on successful authentication.

3. Get Account Details- GET:/account/{accountNumber}
   Account details can be fetched with respect to account number provided as path param. If valid accessToken is not passed in the request, 404 error code will be thrown.

4. Check Balance- GET:/account/balance/{accountNumber}
   To check balance for the provided account number.

5. Fund Transfer- POST:/transaction/fundTransfer
   To transfer the funds between two accounts.

# Application Flow:
1. Please do the registration using the API POST:/auth/signup
2. After you are successfully registered, you will get the email notification.
3. Now you can login using the API POST:/auth/login which will return the access token in response along with user details.
4. Access token will be required for further API calls otherwise you will get the un authorized error.
5. Using the valid accessToken, you can do fund transfer (POST:/transaction/fundTransfer), 
    check balance (GET:/account/balance/{accountNumber}) or get account details (GET:/account/{accountNumber})

# Important things to take care
We can keep the secret key and passwords in the kubernetes secrets. I have kept these in properties files for simplicity.

# Junit test cases
I have added some Junit test cases. Wanted to add more but as it would have taken more time, So I added it for 2-3 classes only.

# Postman Collection
I have added the Postman Collection in this project for reference.

 



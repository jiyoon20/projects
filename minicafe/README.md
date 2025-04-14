# Minicafe- Cafe Order Management System

**Version 1.0**  
A simple Spring Boot application for order management, including customer information management, order tracking, and menu management.

## Features

### Admin
- Admin Sign-up and login
- Admin dashboard
- Menu creation and modification
- Customer management(Activate / Deactivate customers)
- Order managerment (View order details and update status)
	
### User
- User Sign-up and login
- Menu list and menu details
- Place Order for menu items

## Built with
* Java21
* Spring Boot
* Gradle
* H2 Database (local dev)
* AWS S3 (Image uploads)

## Getting Started
### 1. Clone the repository
``` bash
git clone http://github.com/jiyoon20/project/minicafe.git
cd minicafe
```

### 2. Build the project
``` bash
./gradlew build
```

### 3. Run locally
```
bash
java -jar build/libs/minicafe.jar
```
(Default port : http://localhost:8080)

## API Documentation
minicafe-api.md

## Deployment Plan
- version 1.0 : Initial deployment to AWS Elastic Benastalk
- version 1.5 : Add enhancements: 
	* OAuth login integration
	* Customer rewards system
	* Rating and reviews

## License
This project is licensed under the MIT License.

## Author
Jooie Yoon (https://github.com/jiyoon20)
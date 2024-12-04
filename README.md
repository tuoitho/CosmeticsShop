# Cosmetics Shop 💄

Cosmetics Shop is a modern e-commerce platform built with cutting-edge technologies like Java 21, Spring Boot 3, and Thymeleaf for Java template engine. It leverages JPA for ORM, employs Spring Security 6.1 for robust authentication and authorization, integrates Google login for user convenience, and utilizes reCAPTCHA for enhanced security. The platform relies on MySQL for reliable data storage and Redis for efficient OTP management. Payment options include PayPal and VNPay, ensuring a smooth and flexible shopping experience.

## Purpose 🎯

The **Cosmetics Shop** aims to provide an online platform where users can browse, review, and purchase a wide range of cosmetic products with ease. The platform prioritizes security and user experience, offering a secure environment for transactions, flexible payment options, and intuitive browsing features. Whether you are looking for skincare, makeup, or beauty accessories, this platform ensures a seamless shopping experience, backed by powerful technology.

## Technologies Used 🛠️

- **Backend**: Java 21, Spring Boot 3, Spring Security 6.1
- **Frontend**: Thymeleaf, HTML, CSS, JavaScript
- **Database**: MySQL 🗄️
- **ORM**: JPA (Java Persistence API) 🔄
- **Version Control**: Git, GitHub 🌐

## Installation and Setup 🚀

To set up the **Cosmetics Shop** locally, follow these steps:

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/cosmetics-shop.git

2. Navigate into the project directory:
   ```bash
   cd cosmetics-shop

3. Set up the database:
Ensure you have MySQL installed and running.
Create a new database (e.g., cosmetics_shop)
    ```bash
    CREATE DATABASE cosmetics_shop;

Update the application.properties file with your MySQL database credentials

    spring.datasource.url=jdbc:mysql://localhost:3306/cosmetics_shop
    spring.datasource.username=your-username
    spring.datasource.password=your-password
    spring.jpa.hibernate.ddl-auto=update
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver


4. Build and run the application:
 If you're using Maven
    ```bash
    mvn clean install
    mvn spring-boot:run


 If you're using Gradle
 
    ./gradlew build
    ./gradlew bootRun


5. The application should be accessible at http://localhost:8080.

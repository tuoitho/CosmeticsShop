# Cosmetics Shop 💄

Cosmetics Shop is a modern e-commerce platform built with cutting-edge technologies like Java 21, Spring Boot 3, and Thymeleaf for Java template engine. It leverages JPA for ORM, employs Spring Security 6.1 for robust authentication and authorization, integrates Google login for user convenience, and utilizes reCAPTCHA for enhanced security. The platform relies on MySQL for reliable data storage and Redis for efficient OTP management.Supporting flexible payments via PayPal and VNPay, the platform also utilizes AJAX for a smooth user experience with real-time interactions, and Cloudinary for fast, scalable cloud-based image storage and optimization.


## Installation and Setup 🚀

To set up the **Cosmetics Shop** locally, follow these steps:


### 🏆 Running from JAR File (GitHub Release)

#### 1️⃣ Download the Latest JAR File
Download the JAR file from GitHub Releases:
👉 [Download JAR](https://github.com/PhatBee/Cosmetics_Shop/releases/download/2024/CosmeticSellingWebsite-0.0.1-SNAPSHOT.jar)

---

#### 2️⃣ Prepare the Database

##### 🔹 **Create Database**
Open MySQL and create a new database using the following command:
```sql
CREATE DATABASE cosmeticsshop;
```

##### 🔹 **Import Sample Data**
https://github.com/PhatBee/Cosmetics_Shop/blob/master/dump-cosmeticsshop-202503021651.sql

#### 3️⃣ Run the JAR File with Database Configuration

##### 🔹 **On Linux/MacOS**
```bash
export DB_URL="jdbc:mysql://localhost:3306/cosmeticsshop"
export DB_USER="root"
export DB_PASS="88648468"
java -jar CosmeticSellingWebsite-0.0.1-SNAPSHOT.jar
```

##### 🔹 **On Windows (CMD)**
```cmd
set DB_URL=jdbc:mysql://localhost:3306/cosmeticsshop
set DB_USER=root
set DB_PASS=88648468
java -jar CosmeticSellingWebsite-0.0.1-SNAPSHOT.jar
```

---

#### 4️⃣ Verify the Application
After running the application, open your browser and visit:
- **Home Page:** [http://localhost:8081](http://localhost:8081)

---

#### 🎯 Contact & Contribution
If you encounter any issues or want to contribute, feel free to open an **Issue** or submit a **Pull Request** at:
🔗 [GitHub Issues](https://github.com/PhatBee/Cosmetics_Shop/issues)

🚀 **Happy coding!** 🎉



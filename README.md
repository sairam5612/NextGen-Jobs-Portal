# NextGen Jobs - Full Stack Job Portal

<div align="center">

**NextGen Jobs** is a modern, enterprise-grade job application portal designed to bridge the gap between top talent and world-class companies. Built with a decoupled architecture, it features a robust Spring Boot REST API backend and a responsive, theme-aware frontend using Vanilla JS and Bootstrap 5.

[Report Bug](https://github.com/sairam5612/NextGen-Jobs-Portal/issues) · [Request Feature](https://github.com/sairam5612/NextGen-Jobs-Portal/issues)

</div>

---

## 🚀 Features

### 🌟 Core Functionality
* **Role-Based Access Control (RBAC):** Secure login flows for Employers (Post & Manage Jobs) and Job Seekers (Search & Apply).
* **Advanced Search & Filter:** Dynamic filtering of jobs by Location, Experience Level, Work Mode (Remote/Hybrid), and Keywords using custom JPQL queries.
* **Smart Data Seeding:** Automatically populates the database with 50+ realistic job listings from top tech companies (Google, Tesla, Spotify) on startup to ensure the app is never empty.

### 🎨 User Experience (UI/UX)
* **Modern Glassmorphism Design:** A clean, translucent UI aesthetic with blurred backgrounds and soft shadows.
* **Dark Mode / Light Mode:** Fully responsive theme switching with persistent user preference using local storage.
* **Interactive Dashboard:** Single-page application (SPA) feel with no page reloads for searching or filtering.
* **Rich Job Details:** Modal-based job views with HTML-formatted descriptions and color-coded stats (Salary, Location).

### 🔒 Security & Backend
* **JWT Authentication:** Stateless, secure token-based session management using `jjwt`.
* **File Handling:** Secure PDF resume uploads with size validation and error handling.
* **Email Notifications:** Automated SMTP email confirmation sent to applicants upon successful submission.
* **Exception Handling:** Global exception handlers for robust error management (e.g., duplicate applications, file size limits).

---

## 🛠️ Tech Stack

| Component | Technology |
| :--- | :--- |
| **Backend** | Java 21, Spring Boot 3.3 (Web, Data JPA, Security, Mail) |
| **Database** | PostgreSQL |
| **Frontend** | HTML5, CSS3 (Custom Variables), JavaScript (ES6+ Fetch API), Bootstrap 5 |
| **Security** | Spring Security, JWT (JSON Web Tokens) |
| **Tools** | Maven, Swagger UI, Git, Postman |

---

## ⚙️ Installation & Setup

Follow these steps to set up the project locally.

### 1. Clone the Repository

```bash
git clone [https://github.com/sairam5612/NextGen-Jobs-Portal.git](https://github.com/sairam5612/NextGen-Jobs-Portal.git)
cd NextGen-Jobs-Portal
```

### 2. Configure Database
Create a PostgreSQL database named job_portal_db. Then, update src/main/resources/application.properties with your credentials:

```bash
spring.datasource.url=jdbc:postgresql://localhost:5432/job_portal_db
spring.datasource.username=your_postgres_username
spring.datasource.password=your_postgres_password
```
### 3. Configure Email (Optional)
To enable email notifications, add your SMTP credentials (e.g., Gmail App Password) to application.properties:
``` bash
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```
### 4. Run the Application
You can run the application using Maven:
```bash
mvn spring-boot:run
```


## 📝 License
Distributed under the MIT License. See LICENSE for more information.

<div align="center"> <sub>Built with ❤️ by <a href="https://github.com/sairam5612">sairam5612</a></sub> </div>








# Carbon Footprint Tracker 

A robust web application designed to help individuals estimate, monitor, and analyze their daily carbon emissions. By tracking activities across transportation, home energy consumption, and dietary habits, the application provides users with actionable insights to reduce their environmental impact and adopt sustainable living habits.

---

## Screenshots & Demo

Here is a visual overview of the application in action. 
<img width="1919" height="972" alt="image" src="https://github.com/user-attachments/assets/743d7f58-90a4-4864-aac0-98718437e37b" />


### 1. User Dashboard & Analytics
Review your daily, weekly, and monthly carbon footprints through intuitive summaries.
<img width="1919" height="972" alt="image" src="https://github.com/user-attachments/assets/1426efe1-5d96-4a1c-be29-5d3c76f001eb" />


### 2. Emission Calculator
Input form for daily commuting distance, fuel types, and utility usage metrics.
<img width="658" height="534" alt="image" src="https://github.com/user-attachments/assets/8a5eb75b-e9a4-44a4-9de0-a811b5835983" />


### 3. API Response / Database View
Sample backend response displaying calculated carbon metrics and structured data logs.
<img width="1555" height="533" alt="image" src="https://github.com/user-attachments/assets/c2edfa57-7998-4da7-a49c-49dfa228bcaa" />


---

## Features

* **Emission Calculators:** Calculate carbon footprints instantly based on daily commutes (vehicle type, distance), home utility usage (electricity, gas, water), and dietary choices.
* **Personalized Analytics:** Track your emission trends over time with categorized data breakdowns.
* **Sustainability Recommendations:** Receive tailored, data-driven tips and actionable habits to help lower your footprint.
* **History Log & Progress Tracking:** Securely log and review past records to track your environmental impact reduction over time.

---

## Tech Stack

* **Backend Framework:** Java, Spring Boot (REST APIs / Microservices Architecture)
* **Build Tool:** Apache Maven
* **Database:** PostgreSQL (Relational Data Storage & Management)
* **ORM Mapping:** Spring Data JPA / Hibernate

---

## ⚙️ Getting Started

Follow these steps to set up and run the project locally on your machine using Command Prompt (cmd).

### Prerequisites
* **Java Development Kit (JDK):** Version 17 or higher installed.
* **Apache Maven:** Installed and configured in your system environment variables.
* **PostgreSQL Server:** Installed, configured, and running locally.

### 1. Clone the Repository
Open your Command Prompt and execute:
```cmd
git clone [https://github.com/MakeItTimeless/CarbonFootPrintTracker.git](https://github.com/MakeItTimeless/CarbonFootPrintTracker.git)
cd CarbonFootPrintTracker 
```
### 2. Configure the Database
Open your PostgreSQL administration tool (like pgAdmin or psql shell).

Create a brand new database named carbon_tracker.

Open your project configuration file located at src/main/resources/application.properties and update it with your database credentials:
```cmd
spring.datasource.url=jdbc:postgresql://localhost:5432/carbon_tracker
spring.datasource.username=YOUR_POSTGRES_USERNAME
spring.datasource.password=YOUR_POSTGRES_PASSWORD
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 3. Build and Run the Application
In your Command Prompt, run the following commands sequentially to clean previous builds, install required Maven dependencies, and start the Spring Boot application:

```cmd
DOS
:: Clean target directories and install project dependencies
mvn clean install

:: Run the Spring Boot application server
mvn spring-boot:run
```

### Project Structure Overview
```cmd
src/
└── main/
    ├── java/com/project/carbontracker/
    │   ├── controller/   # REST API Endpoints for user interaction
    │   ├── service/      # Core business logic & carbon footprint calculation algorithms
    │   ├── repository/   # Database access layer interfaces (Spring Data JPA)
    │   └── model/        # Database Entities (User, EmissionLog, ActivityLog)
    └── resources/
        └── application.properties  # Centralized database and application configurations
```

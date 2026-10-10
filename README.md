Online Health Consultation Platform

Project Overview

The Online Health Consultation Platform is a Java Swing desktop application that helps patients manage healthcare appointments and health records. It provides separate dashboards for patients, healthcare professionals, and administrators.

Features

Patient

- Register and log in.
- Book healthcare appointments.
- View appointments.
- View health records.

Healthcare Professional

- Log in to a professional dashboard.
- View appointments.
- Access patient-related information.
- Manage consultation-related information where implemented.

Administrator

- Log in to the admin dashboard.
- View registered users.
- View appointments.

Technologies Used

- Java
- Java Swing (Graphical User Interface)
- JDBC (Java Database Connectivity)
- MySQL
- MySQL Connector/J
- Visual Studio Code

Project Structure

- "Main.java" — Application entry point.
- "gui/" — Login, registration, and dashboard screens.
- "model/" — Application data classes.
- "dao/" — Database access classes.
- "service/" — Service-layer classes.
- "util/" — Database connection utilities.
- "database/schema.sql" — Database and table creation script.
- "lib/" — MySQL Connector/J library.

Database Setup

1. Install and start MySQL Server.

2. Open MySQL Workbench and connect to your local MySQL server.

3. Open "database/schema.sql" and execute the script to create the database and tables.

4. Configure the database password in your local PowerShell terminal. Do not put your actual password in the source code.
   
   $env:OHC_DB_PASSWORD = Read-Host "Enter MySQL password"

5. Keep the same PowerShell terminal open when compiling and running the application. If you open a new terminal, configure the environment variable again.

Note: The current Java connection configuration uses the MySQL "root" account on "localhost:3306".

Running the Application

Open the project folder in VS Code and run these commands in the terminal from the project root:

Compile:

javac -cp ".;lib\mysql-connector-j-26.7.0.jar" -d . Main.java gui\*.java dao\*.java model\*.java util\*.java

Run:

java -cp ".;lib\mysql-connector-j-26.7.0.jar" Main

Security Note

Configure database credentials locally. Do not publish real passwords or private credentials in source code or on GitHub.

Project Status

The application includes GUI screens and database-connected features for patient, healthcare professional, and administrator workflows. Individual features should be tested before release.
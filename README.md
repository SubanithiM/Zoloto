
# Zoloto — E-Commerce Web Application

Zoloto is a Java-based e-commerce web application built using Java Servlets, JSP, JDBC, and MySQL. It provides a web interface for browsing products and using e-commerce functionality.

## Technology Stack

- Java 21
- Java Servlets and JSP
- JDBC
- MySQL
- Maven
- Apache Tomcat 10.1
- HTML, CSS, and JavaScript
- Docker

## Project Structure

```text
Zoloto/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/zoloto/
│       ├── resources/
│       └── webapp/
│           ├── assets/
│           └── WEB-INF/
├── pom.xml
├── .gitignore
├── .dockerignore
├── Dockerfile
└── README.md
```

The actual Java packages and web resources are organized according to the project's existing source structure.

## Prerequisites

To run the application locally, install:

- JDK 21
- Apache Maven
- MySQL Server
- Apache Tomcat 10.1
- Eclipse IDE or another Java IDE

Docker is required to build and run the container image.

## Local Setup

1. Clone or download this repository.
2. Create a MySQL database named `zoloto`.
3. Import your authorized database backup or execute the project's database schema scripts.
4. Configure database connection details in your local `src/main/resources/db.properties` file.
5. Ensure the MySQL server is running.
6. Build the application:

   ```bash
   mvn clean package
   ```

7. Deploy the generated WAR file from the `target/` directory to Tomcat 10.1.
8. Start Tomcat and open the application in your browser.

## Database Configuration

Do not commit database passwords or private credentials.

The application supports the following environment variables:

| Variable | Description |
|---|---|
| `DB_URL` | JDBC connection URL for MySQL |
| `DB_USERNAME` | Database username |
| `DB_PASSWORD` | Database password |

For local development, these values can be supplied through `db.properties`. For cloud deployment, configure them through the hosting provider's environment settings.

## Docker Deployment

Build the Docker image from the project root:

```bash
docker build -t zoloto .
```

Run the container locally:

```bash
docker run --rm -p 8080:8080 \
  -e DB_URL="your-jdbc-url" \
  -e DB_USERNAME="your-database-user" \
  -e DB_PASSWORD="your-database-password" \
  zoloto
```

Replace the example database values with valid credentials. The database must be reachable from the container.

Open:

http://localhost:8080/

## Cloud Deployment

The Dockerfile is intended for deployment on a container hosting platform such as Render.

Before deploying:

1. Push the source code to GitHub.
2. Provision a cloud MySQL database.
3. Migrate the required database schema and data.
4. Configure `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` securely.
5. Build and deploy the Docker image.
6. Test the application's pages and database operations.

Do not assume a free hosting plan includes persistent MySQL storage or unlimited usage.

## Security

- Never commit database credentials.
- Never commit `.env` files or SQL backup files.
- Use environment variables for cloud secrets.
- Use a secure database connection appropriate for your hosting provider.
- Keep production database credentials separate from local development credentials.

## Project Status

The application is being prepared for containerized deployment.

## License

No license has been specified yet. All rights reserved unless a license is added to this repository.

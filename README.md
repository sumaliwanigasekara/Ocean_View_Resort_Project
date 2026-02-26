
# Ocean View Resort Project

## Overview
Ocean View Resort is a JavaEE-based room reservation and management system with modules for authentication, guests, rooms, reservations, billing, and reports.

## Tech Stack
- Java 11
- JavaEE (Servlet/JSP/Web)
- Maven
- MySQL
- JUnit

## Features
- User login/logout with session handling
- Guest management
- Room management
- Reservation management
- Billing and payment confirmation
- Reports dashboard

## Project Structure
- `src/main/java` - backend code
- `src/main/webapp` - frontend pages/assets
- `src/test/java` - unit tests

## How to Run
1. Clone repository
2. Configure database in `src/main/resources/db.properties`
3. Build and run using IntelliJ/Tomcat (or Maven if installed)

## Testing
- Unit tests are in `src/test/java`
- Run tests using IntelliJ test runner (or `mvn test`)

## Branching Workflow
- `development` -> feature development
- `qa` -> integration testing
- `uat` -> user acceptance testing
- `master` -> production-ready release
- Promotion flow: `development -> qa -> uat -> master`

## Versioning
Semantic Versioning is used: `major.minor.patch`
Example:
- `v1.0.0` initial release
- `v1.1.0` feature release
- `v1.1.1` patch fix

## Repository
https://github.com/sumaliwanigasekara/Ocean_View_Resort_Project
# Hibernate Task

Spring Boot + Hibernate project for managing Trainer, Trainee and Training profiles.

---

# Technologies

- Java 17
- Spring Boot
- Spring Data JPA
- Hibernate
- H2 Database
- Maven
- Lombok
- Jakarta Validation
- JUnit 5
- Swagger / OpenAPI
- BCrypt Password Encoder

---

# Main Features

1. Create Trainer profile
2. Create Trainee profile
3. Trainer username/password authentication
4. Trainee username/password authentication
5. Get Trainer profile by username
6. Get Trainee profile by username
7. Trainer password change
8. Trainee password change
9. Update Trainer profile
10. Update Trainee profile
11. Activate/Deactivate Trainer profile
12. Activate/Deactivate Trainee profile
13. Delete Trainee profile by username with cascade delete of related trainings
14. Get Trainee trainings list by criteria
15. Get Trainer trainings list by criteria
16. Add training
17. Get trainers not assigned to trainee
18. Update trainee trainers list

---

# Database Design

The project follows the provided Hibernate task schema.

## Tables

- users
- trainers
- trainees
- trainings
- training_types
- trainee_trainer

---

# Relationships

- User to Trainer: One-to-One
- User to Trainee: One-to-One
- Trainee to Trainer: Many-to-Many
- Training to Trainee: Many-to-One
- Training to Trainer: Many-to-One
- Training to TrainingType: Many-to-One

---

# Important Rules

- Username and password are generated automatically during Trainer/Trainee creation.
- Passwords are stored using BCrypt hashing.
- All operations except creating Trainer/Trainee require authentication.
- Trainer and Trainee authentication are checked separately.
- Required fields are validated before create/update operations.
- Activate/Deactivate operations are not idempotent.
- Deleting Trainee is a hard delete.
- Deleting Trainee also deletes related trainings by cascade.
- Training duration is numeric.
- Training date and Trainee date of birth use date type.
- Training types are constant values and cannot be updated from the application.
- Transactions are used for create, update, delete and add training operations.
- Logging is implemented without exposing passwords.
- API responses use DTOs instead of exposing entity objects directly.
- JPQL queries are used for filtering training lists.

---

# Architecture

Controller Layer
        ↓
Service Layer
        ↓
Repository Layer
        ↓
H2 Database

# Swagger/OpenAPI documentation: http://localhost:8080/swagger-ui/index.html
# H2 in-memory database console: http://localhost:8080/h2-console



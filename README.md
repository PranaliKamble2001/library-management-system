# Library Management System

A console-based Library Management System built with **Java**, **JDBC** and **MySQL**.
It manages book inventory, member registration, and book issue/return with automatic
fine calculation. The project uses a layered architecture (OOP + DAO pattern) and a
normalized relational schema.

## Features

- **Books**: add, update, delete, list and search (by title, author or ISBN)
- **Members**: register, update, delete, list and search (by name or email)
- **Issue / Return**: issue a book to a member, return it, and view active, all and overdue loans

## Business Rules

| Rule | Behaviour |
|---|---|
| Loan period | 14 days from the issue date |
| Overdue fine | 5.00 per day late, calculated on return |
| Stock tracking | Issuing reduces available copies by 1, returning adds 1 back |
| No copies left | A book with 0 available copies cannot be issued |
| Duplicate email | A member cannot register with an email that already exists |
| Inactive members | Members with INACTIVE status cannot borrow books |
| Double return | A loan that is already returned cannot be returned again |

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Database | MySQL 8 |
| Database access | JDBC (`mysql-connector-j`) |
| Build tool | Maven |
| IDE | Eclipse |

## Architecture

Each layer only talks to the one below it:

```
Main (console UI)
   -> LibraryService (business rules)
        -> BookDAO / MemberDAO / LoanDAO (interfaces)
             -> BookDAOImpl / MemberDAOImpl / LoanDAOImpl (JDBC + SQL)
                  -> MySQL
```

OOP concepts used: encapsulation (private fields with getters/setters), abstraction
(DAO interfaces), inheritance (`LibraryException extends Exception`), enums for fixed
values, the Singleton pattern (`DBConnection`), and constructor chaining.

## Project Structure

```
library-management-system/
├── pom.xml
├── sql/
│   └── schema.sql                  # tables, constraints and sample data
└── src/main/
    ├── java/com/library/
    │   ├── model/                  # Book, Member, Loan
    │   ├── dao/                    # DAO interfaces and JDBC implementations
    │   ├── db/                     # DBConnection (shared connection)
    │   ├── service/                # LibraryService (business rules)
    │   ├── exception/              # LibraryException
    │   └── ui/                     # Main (console menu)
    └── resources/
        └── db.properties.example   # copy to db.properties and add your password
```

## Database Design

- `members`: member_id (PK), name, email (unique), phone, address, membership_date, status
- `books`: book_id (PK), isbn (unique), title, author, publisher, category, total_copies, available_copies
- `loans`: loan_id (PK), book_id (FK), member_id (FK), issue_date, due_date, return_date, fine_amount, status

A `CHECK` constraint keeps `available_copies` between 0 and `total_copies`.
Loan listings use SQL `JOIN`s to show book titles and member names instead of IDs.

## Getting Started

### Prerequisites

- JDK 17 or newer
- MySQL Server 8
- Maven (or an IDE with Maven support, such as Eclipse)

### 1. Create the database

Run `sql/schema.sql` in MySQL Workbench (File > Open SQL Script, then execute),
or from the command line:

```
mysql -u root -p < sql/schema.sql
```

This creates the `library_management` database with sample books and members.
It drops and recreates the database, so running it again resets all data.

### 2. Add your database credentials

Copy `src/main/resources/db.properties.example` to `src/main/resources/db.properties`
and put in your own MySQL password:

```
db.url=jdbc:mysql://localhost:3306/library_management?useSSL=false&serverTimezone=UTC
db.user=root
db.password=your_password_here
```

`db.properties` is listed in `.gitignore`, so your password is never uploaded.

### 3. Run it

From an IDE, run `com.library.ui.Main`.

Or from the command line:

```
mvn clean package
java -jar target/library-management-system.jar
```

## Sample Menu

```
--- MAIN MENU ---
1. Manage Books
2. Manage Members
3. Issue/ Return Books
0. Exit
```

## Possible Improvements

- Wrap issue and return in a database transaction so both writes succeed or fail together
- Login with Admin / Librarian / Member roles
- Book reservations for titles with no copies left
- Input validation (email and ISBN format)
- Unit tests with JUnit, using mock DAOs
- A GUI (Swing or JavaFX) or a web front end on top of the same service layer

## Author

Pranali Kamble

[GitHub](https://github.com/PranaliKamble2001)

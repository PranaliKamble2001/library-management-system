DROP DATABASE IF EXISTS library_management;
CREATE DATABASE library_management;
USE library_management;

CREATE TABLE members (
    member_id       INT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(100)        NOT NULL,
    email           VARCHAR(100)        NOT NULL UNIQUE,
    phone           VARCHAR(15),
    address         VARCHAR(255),
    membership_date DATE                NOT NULL DEFAULT (CURRENT_DATE),
    status          ENUM('ACTIVE','INACTIVE') NOT NULL DEFAULT 'ACTIVE',
    created_at      TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE books (
    book_id           INT AUTO_INCREMENT PRIMARY KEY,
    isbn              VARCHAR(20)        NOT NULL UNIQUE,
    title             VARCHAR(200)       NOT NULL,
    author            VARCHAR(150)       NOT NULL,
    publisher         VARCHAR(150),
    category          VARCHAR(80),
    total_copies      INT                NOT NULL DEFAULT 1,
    available_copies  INT                NOT NULL DEFAULT 1,
    created_at        TIMESTAMP          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CHECK (available_copies >= 0 AND available_copies <= total_copies)
);

CREATE TABLE loans (
    loan_id       INT AUTO_INCREMENT PRIMARY KEY,
    book_id       INT             NOT NULL,
    member_id     INT             NOT NULL,
    issue_date    DATE            NOT NULL DEFAULT (CURRENT_DATE),
    due_date      DATE            NOT NULL,
    return_date   DATE            NULL,
    fine_amount   DECIMAL(8,2)    NOT NULL DEFAULT 0.00,
    status        ENUM('ISSUED','RETURNED','OVERDUE') NOT NULL DEFAULT 'ISSUED',
    CONSTRAINT fk_loans_book   FOREIGN KEY (book_id)   REFERENCES books(book_id)     ON DELETE CASCADE,
    CONSTRAINT fk_loans_member FOREIGN KEY (member_id) REFERENCES members(member_id) ON DELETE CASCADE
);

CREATE INDEX idx_books_title    ON books(title);
CREATE INDEX idx_books_author   ON books(author);
CREATE INDEX idx_members_email  ON members(email);
CREATE INDEX idx_loans_status   ON loans(status);

-- ---------------------------------------------------------------------
-- Sample data
-- ---------------------------------------------------------------------
INSERT INTO members (name, email, phone, address) VALUES
('Pranali Kamble', 'pranali@example.com', '9000000001', 'Mumbai, India'),
('Shreya Kamble', 'shreya@example.com', '9000000002', 'Pune, India'),
('Anagha Gaykar', 'anagha@example.com', '9000000003', 'Nashik, India'),
('Samiksha Patil', 'samiksha@example.com', '9000000004', 'Thane, India'),
('Priyanshi Upadhyay', 'priyanshi@example.com', '9000000005', 'Delhi, India'),
('Rupali Salave', 'rupali@example.com', '9000000006', 'Kolhapur, India');

INSERT INTO books (isbn, title, author, publisher, category, total_copies, available_copies) VALUES
('978-0132350884', 'Clean Code', 'Robert C. Martin', 'Prentice Hall', 'Software Engineering', 3, 3),
('978-0201633610', 'Design Patterns', 'Gamma, Helm, Johnson, Vlissides', 'Addison-Wesley', 'Software Engineering', 2, 2),
('978-9332584675', 'Database System Concepts', 'Silberschatz, Korth, Sudarshan', 'McGraw-Hill', 'DBMS', 4, 4),
('978-0134685991', 'Effective Java', 'Joshua Bloch', 'Addison-Wesley', 'Java', 2, 2),
('978-0596009205', 'Head First Java', 'Kathy Sierra, Bert Bates', 'O''Reilly', 'Java', 3, 3),
('978-1260440232', 'Java: The Complete Reference', 'Herbert Schildt', 'McGraw-Hill', 'Java', 3, 3),
('978-0262033848', 'Introduction to Algorithms', 'Cormen, Leiserson, Rivest, Stein', 'MIT Press', 'Algorithms', 2, 2),
('978-1617292231', 'Grokking Algorithms', 'Aditya Bhargava', 'Manning', 'Algorithms', 2, 2),
('978-0135957059', 'The Pragmatic Programmer', 'Andrew Hunt, David Thomas', 'Addison-Wesley', 'Software Engineering', 2, 2),
('978-0134757599', 'Refactoring', 'Martin Fowler', 'Addison-Wesley', 'Software Engineering', 2, 2),
('978-0134494166', 'Clean Architecture', 'Robert C. Martin', 'Prentice Hall', 'Software Engineering', 2, 2),
('978-1449373320', 'Designing Data-Intensive Applications', 'Martin Kleppmann', 'O''Reilly', 'Databases', 2, 2),
('978-1492057611', 'Learning SQL', 'Alan Beaulieu', 'O''Reilly', 'DBMS', 3, 3),
('978-0133970777', 'Fundamentals of Database Systems', 'Elmasri, Navathe', 'Pearson', 'DBMS', 3, 3),
('978-1119800361', 'Operating System Concepts', 'Silberschatz, Galvin, Gagne', 'Wiley', 'Operating Systems', 3, 3),
('978-0132126953', 'Computer Networks', 'Andrew S. Tanenbaum', 'Pearson', 'Networking', 2, 2),
('978-0136681557', 'Computer Networking: A Top-Down Approach', 'Kurose, Ross', 'Pearson', 'Networking', 2, 2),
('978-1593279288', 'Python Crash Course', 'Eric Matthes', 'No Starch Press', 'Python', 3, 3),
('978-1492032649', 'Hands-On Machine Learning with Scikit-Learn, Keras, and TensorFlow', 'Aurelien Geron', 'O''Reilly', 'AI and Machine Learning', 2, 2),
('978-0984782857', 'Cracking the Coding Interview', 'Gayle Laakmann McDowell', 'CareerCup', 'Interview Prep', 2, 2);
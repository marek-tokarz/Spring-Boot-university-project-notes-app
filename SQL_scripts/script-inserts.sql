-- Use the database
USE notes_db;

-- Insert data into members table
INSERT INTO members (user_id, pw, active) VALUES
('john_doe', '{bcrypt}$2a$10$abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJ', 1),
('jane_smith', '{bcrypt}$2a$10$zyxwvutsrqponmlkjihgfedcba0987654321ZYXWVUTSRQ', 1),
('mike_wilson', '{bcrypt}$2a$10$1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJ', 1),
('sarah_jones', '{bcrypt}$2a$10$qwertyuiopasdfghjklzxcvbnm1234567890QWERTYUIOP', 0),
('tom_brown', '{bcrypt}$2a$10$asdfghjklzxcvbnmqwertyuiop0987654321ASDFGHJKLZ', 1);

-- Insert data into roles table
INSERT INTO roles (user_id, role) VALUES
('john_doe', 'ROLE_USER'),
('john_doe', 'ROLE_ADMIN'),
('jane_smith', 'ROLE_USER'),
('jane_smith', 'ROLE_MODERATOR'),
('mike_wilson', 'ROLE_USER'),
('sarah_jones', 'ROLE_USER'),
('tom_brown', 'ROLE_USER');

ALTER TABLE category AUTO_INCREMENT = 1;

-- Insert data into category table
INSERT INTO category (name) VALUES
('Work'),
('Personal'),
('Ideas'),
('Shopping'),
('Travel'),
('Health'),
('Projects');

ALTER TABLE note AUTO_INCREMENT = 1;

-- Insert data into note table
INSERT INTO note (title, content, user_id) VALUES
('Meeting Notes', 'Discussed project timeline and deliverables for Q2 2026.', 'john_doe'),
('Grocery List', 'Milk, eggs, bread, cheese, vegetables, fruits.', 'jane_smith'),
('App Idea', 'Create a note-taking app with categories and search functionality.', 'mike_wilson'),
('Vacation Planning', 'Research hotels in Paris. Book flights for June 2026.', 'jane_smith'),
('Workout Routine', 'Monday: Cardio, Tuesday: Strength, Wednesday: Rest.', 'tom_brown'),
('Project Proposal', 'Outline for new client presentation including budget and timeline.', 'john_doe'),
('Book Recommendations', 'Read "Atomic Habits", "Deep Work", "The Pragmatic Programmer".', 'mike_wilson'),
('Birthday Gift Ideas', 'Watch for dad, perfume for mom, headphones for brother.', 'sarah_jones'),
('Website Redesign', 'Update homepage layout, improve mobile responsiveness.', 'john_doe'),
('Meal Prep Sunday', 'Chicken breast, rice, broccoli, sweet potatoes for the week.', 'jane_smith');

-- Insert data into note_category table (many-to-many relationships)
INSERT INTO note_category (note_id, category_id) VALUES
(1, 1),  -- Meeting Notes -> Work
(2, 4),  -- Grocery List -> Shopping
(3, 3),  -- App Idea -> Ideas
(3, 7),  -- App Idea -> Projects
(4, 5),  -- Vacation Planning -> Travel
(4, 2),  -- Vacation Planning -> Personal
(5, 6),  -- Workout Routine -> Health
(6, 1),  -- Project Proposal -> Work
(6, 7),  -- Project Proposal -> Projects
(7, 2),  -- Book Recommendations -> Personal
(8, 4),  -- Birthday Gift Ideas -> Shopping
(8, 2),  -- Birthday Gift Ideas -> Personal
(9, 1),  -- Website Redesign -> Work
(9, 7),  -- Website Redesign -> Projects
(10, 6),  -- Meal Prep Sunday -> Health
(10, 2); -- Meal Prep Sunday -> Personalid
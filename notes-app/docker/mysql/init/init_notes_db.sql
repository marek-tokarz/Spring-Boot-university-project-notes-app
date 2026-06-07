-- init_notes_db.sql

-- 1) Reset bazy
DROP DATABASE IF EXISTS notes_db;
CREATE DATABASE notes_db CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE notes_db;

-- 2) Drop tabel (na wszelki wypadek) + FK off
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS note_category;
DROP TABLE IF EXISTS note;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS members;

SET FOREIGN_KEY_CHECKS = 1;

-- 3) Tworzenie tabel

CREATE TABLE `members` (
                           `user_id` VARCHAR(50) NOT NULL,
                           `pw` CHAR(68) NOT NULL,
                           `active` TINYINT NOT NULL,
                           PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `roles` (
                         `user_id` VARCHAR(50) NOT NULL,
                         `role` VARCHAR(50) NOT NULL,
                         UNIQUE KEY `authorities5_idx_1` (`user_id`, `role`),
                         CONSTRAINT `authorities5_ibfk_1`
                             FOREIGN KEY (`user_id`) REFERENCES `members` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `category` (
                            `id` INT AUTO_INCREMENT PRIMARY KEY,
                            `name` VARCHAR(100) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `note` (
                        `id` INT AUTO_INCREMENT PRIMARY KEY,
                        `title` VARCHAR(100) NOT NULL,
                        `content` TEXT NOT NULL,
                        `creation_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        `modification_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        `user_id` VARCHAR(50) CHARACTER SET latin1,
                        CONSTRAINT `fk_note_member`
                            FOREIGN KEY (`user_id`) REFERENCES `members`(`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `note_category` (
                                 `note_id` INT NOT NULL,
                                 `category_id` INT NOT NULL,
                                 PRIMARY KEY (`note_id`, `category_id`),
                                 CONSTRAINT `fk_nc_note`
                                     FOREIGN KEY (`note_id`) REFERENCES `note`(`id`) ON DELETE CASCADE,
                                 CONSTRAINT `fk_nc_category`
                                     FOREIGN KEY (`category_id`) REFERENCES `category`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 4) Dane startowe

-- Docelowy hash hasła dla wszystkich:
-- $2a$12$lGdE0Cm5CVMpkynXo0nZZuLnUOL1y8BwiABhPqrGEQbXSdDVAh13u
SET @BCRYPT := '{bcrypt}$2a$12$lGdE0Cm5CVMpkynXo0nZZuLnUOL1y8BwiABhPqrGEQbXSdDVAh13u';

INSERT INTO members (user_id, pw, active) VALUES
                                              ('john_doe',   @BCRYPT, 1),
                                              ('jane_smith', @BCRYPT, 1),
                                              ('mike_wilson',@BCRYPT, 1),
                                              ('sarah_jones',@BCRYPT, 0),
                                              ('tom_brown',  @BCRYPT, 1);

INSERT INTO roles (user_id, role) VALUES
                                      ('john_doe', 'ROLE_USER'),
                                      ('john_doe', 'ROLE_ADMIN'),
                                      ('jane_smith', 'ROLE_USER'),
                                      ('jane_smith', 'ROLE_MODERATOR'),
                                      ('mike_wilson', 'ROLE_USER'),
                                      ('sarah_jones', 'ROLE_USER'),
                                      ('tom_brown', 'ROLE_USER');

ALTER TABLE category AUTO_INCREMENT = 1;

INSERT INTO category (name) VALUES
                                ('Work'),
                                ('Personal'),
                                ('Ideas'),
                                ('Shopping'),
                                ('Travel'),
                                ('Health'),
                                ('Projects');

ALTER TABLE note AUTO_INCREMENT = 1;

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

INSERT INTO note_category (note_id, category_id) VALUES
                                                     (1, 1),
                                                     (2, 4),
                                                     (3, 3),
                                                     (3, 7),
                                                     (4, 5),
                                                     (4, 2),
                                                     (5, 6),
                                                     (6, 1),
                                                     (6, 7),
                                                     (7, 2),
                                                     (8, 4),
                                                     (8, 2),
                                                     (9, 1),
                                                     (9, 7),
                                                     (10, 6),
                                                     (10, 2);

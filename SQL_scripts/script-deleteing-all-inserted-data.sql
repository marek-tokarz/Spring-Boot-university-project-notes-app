-- Use the database
USE notes_db;

SET SQL_SAFE_UPDATES = 0;

-- Delete data from note_category table
DELETE FROM note_category;

-- Delete data from note table
DELETE FROM note;

-- Delete data from category table
DELETE FROM category;

-- Delete data from roles table
DELETE FROM roles;

-- Delete data from members table
DELETE FROM members;

SET SQL_SAFE_UPDATES = 1;
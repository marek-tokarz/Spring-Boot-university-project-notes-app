-- Aktualizacja haseł dla wszystkich użytkowników
-- Nowe hasło (hash): $2a$12$lGdE0Cm5CVMpkynXo0nZZuLnUOL1y8BwiABhPqrGEQbXSdDVAh13u

USE notes_db;

UPDATE members SET pw = '{bcrypt}$2a$12$lGdE0Cm5CVMpkynXo0nZZuLnUOL1y8BwiABhPqrGEQbXSdDVAh13u' WHERE user_id = 'jane_smith';
UPDATE members SET pw = '{bcrypt}$2a$12$lGdE0Cm5CVMpkynXo0nZZuLnUOL1y8BwiABhPqrGEQbXSdDVAh13u' WHERE user_id = 'john_doe';
UPDATE members SET pw = '{bcrypt}$2a$12$lGdE0Cm5CVMpkynXo0nZZuLnUOL1y8BwiABhPqrGEQbXSdDVAh13u' WHERE user_id = 'mike_wilson';
UPDATE members SET pw = '{bcrypt}$2a$12$lGdE0Cm5CVMpkynXo0nZZuLnUOL1y8BwiABhPqrGEQbXSdDVAh13u' WHERE user_id = 'sarah_jones';
UPDATE members SET pw = '{bcrypt}$2a$12$lGdE0Cm5CVMpkynXo0nZZuLnUOL1y8BwiABhPqrGEQbXSdDVAh13u' WHERE user_id = 'tom_brown';
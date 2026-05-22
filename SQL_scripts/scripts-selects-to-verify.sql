-- Use the database
USE notes_db;

-- Verify the data with some SELECT queries
SELECT * FROM members;
SELECT * FROM roles;
SELECT * FROM category;
SELECT * FROM note;
SELECT * FROM note_category;

-- Query to see notes with their categories
SELECT 
    n.id,
    n.title,
    n.content,
    n.user_id,
    GROUP_CONCAT(c.name SEPARATOR ', ') AS categories
FROM note n
LEFT JOIN note_category nc ON n.id = nc.note_id
LEFT JOIN category c ON nc.category_id = c.id
GROUP BY n.id, n.title, n.content, n.user_id
ORDER BY n.id;

-- Query to see notes with user information
SELECT 
    n.id,
    n.title,
    n.content,
    n.creation_date,
    n.modification_date,
    m.user_id,
    m.active AS user_active
FROM note n
INNER JOIN members m ON n.user_id = m.user_id
ORDER BY n.creation_date DESC;

-- Query to see users with their roles
SELECT 
    m.user_id,
    m.active,
    GROUP_CONCAT(r.role SEPARATOR ', ') AS roles
FROM members m
LEFT JOIN roles r ON m.user_id = r.user_id
GROUP BY m.user_id, m.active
ORDER BY m.user_id;
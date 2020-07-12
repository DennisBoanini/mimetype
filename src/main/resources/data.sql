DROP TABLE IF EXISTS MIME_TYPE;
 
CREATE TABLE MIME_TYPE (
    id INT AUTO_INCREMENT  PRIMARY KEY,
    type VARCHAR(250) NOT NULL,
    extension VARCHAR(10) NOT NULL,
    description VARCHAR(5500)
);

CREATE TABLE USERS (
    id bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username varchar(255) NOT NULL,
    password varchar(255) NOT NULL
);

INSERT INTO MIME_TYPE (type, extension, description) VALUES
    ('application/pdf', 'pdf', 'Adobe Portable Document Format (PDF)'),
    ('image/png', 'png', 'Portable Network Graphics'),
    ('application/json', 'json', 'JSON format'),
    ('image/jpeg', 'jpeg', 'JPEG images'),
    ('image/jpeg', 'jpg', 'JPEG images');

INSERT INTO USERS (username, password) VALUES
    ('user', '$2a$10$Q4Wnt0U3LsDyFeaz3pKvJuxm5904fllPsyQkoHZRA2VRc9ZGZLLwi'); -- password is "user"
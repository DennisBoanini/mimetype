DROP TABLE IF EXISTS MIME_TYPE;
 
CREATE TABLE MIME_TYPE (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  type VARCHAR(250) NOT NULL,
  extension VARCHAR(10) NOT NULL,
  description VARCHAR(5500)
);
 
INSERT INTO MIME_TYPE (type, extension, description) VALUES
  ('application/pdf', 'pdf', 'Adobe Portable Document Format (PDF)'),
  ('image/png', 'png', 'Portable Network Graphics'),
  ('application/json', 'json', 'JSON format'),
  ('image/jpeg', 'jpeg', 'JPEG images'),
  ('image/jpeg', 'jpg', 'JPEG images');

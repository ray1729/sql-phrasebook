CREATE TABLE author (
       author_id IDENTITY PRIMARY KEY,
       lastname  VARCHAR NOT NULL,
       forenames VARCHAR NOT NULL DEFAULT ''
);

CREATE TABLE book (
       book_id  IDENTITY PRIMARY KEY,
       title    VARCHAR NOT NULL,
       pub_year INT NOT NULL
);

CREATE TABLE book_author (
       book_id INT NOT NULL REFERENCES book(book_id),
       author_id INT NOT NULL REFERENCES author(author_id)
);

INSERT INTO author (lastname, forenames) VALUES
('Christopher','John'),
('Wyndham', 'John');

INSERT INTO book (title, pub_year) VALUES
('The Year of the Comet', 1955),
('The Death of Grass', 1956),
('The Caves of Night', 1958),
('The Day of the Triffids', 1951),
('The Kraken Wakes', 1953),
('The White Mountains', 1967);

INSERT INTO book_author(book_id, author_id)
SELECT book_id, author_id
FROM book CROSS JOIN author
WHERE author.lastname = 'Christopher'
AND book.title IN ('The Year of the Comet', 'The Death of Grass', 'The Caves of Night', 'The White Mountains');

INSERT INTO book_author(book_id, author_id)
SELECT book_id, author_id
FROM book CROSS JOIN author
WHERE author.lastname = 'Wyndham'
AND book.title IN ('The Day of the Triffids', 'The Kraken Wakes');

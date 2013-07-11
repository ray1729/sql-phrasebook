-- tag: select-all-book-ids
SELECT book_id
FROM book;

-- tag: select-books-by-author
SELECT book.*
FROM book
JOIN book_author USING(book_id)
JOIN author USING(author_id)
WHERE author.last_name = ${last-name}
AND author.first_name = ${first-name};

-- tag: select-authors-by-book-titles
SELECT author.*
FROM author
JOIN book_author USING(author_id)
JOIN book USING(book_id)
WHERE book.title IN (${titles})

-- tag: select-books-by-publication-years
SELECT book.*
FROM book
WHERE pub_year IN (${publication-years});

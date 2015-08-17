(ns uk.org.1729.sql_phrasebook_test
  (:require [uk.org.1729.sql-phrasebook.core :refer [sql-phrasebook]]
            [midje.sweet :refer :all]))

(facts "sql-phrasebook"
       (let [pb (sql-phrasebook "test/phrasebook.sql")]

         (fact "it returns a function"
               (fn? pb)
               => truthy)

         (fact "select-all-book-ids returns the expected vector"
               (pb "select-all-book-ids")
               => ["SELECT book_id FROM book WHERE NOT book_author = '--'"])

         (fact "select-book-titless-by-author returns the expected vector"
               (pb "select-book-titles-by-author" {:last-name "Christopher"})
               => ["SELECT book.title FROM book JOIN book_author ON book.book_id = book_author.book_id JOIN author ON author.author_id = book_author.author_id WHERE author.lastname = ?"
                   "Christopher"])

         (fact "select-authors-by-book-titles returns the expected vector"
               (pb "select-authors-by-book-titles" {:titles ["The Death of Grass" "The White Mountains"]})
               => ["SELECT author.* FROM author JOIN book_author ON book_author.author_id = author.author_id JOIN book ON book.book_id = book_author.book_id WHERE book.title IN (?,?)"
                   "The Death of Grass"
                   "The White Mountains"])

         (fact "select-books-by-publication-years returns the expected vector"
               (pb "select-books-by-publication-years" {:publication-years [1958 1959 1967]})
               => ["SELECT book.* FROM book WHERE pub_year IN (?,?,?)"
                   1958 1959 1967])

         (fact "missing parameter throws exception"
               (pb "select-book-titles-by-author" {})
               => (throws java.lang.AssertionError #"Assert failed: Query parameter last-name missing"))))

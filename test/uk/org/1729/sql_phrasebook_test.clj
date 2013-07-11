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
               => ["SELECT book_id FROM book"])

         (fact "select-books-by-author returns the expected vector"
               (pb "select-books-by-author" {:first-name "John" :last-name "Christopher"})
               => ["SELECT book.* FROM book JOIN book_author USING(book_id) JOIN author USING(author_id) WHERE author.last_name = ? AND author.first_name = ?"
                   "Christopher"
                   "John"])

         (fact "select-authors-by-book-titles returns the expected vector"
               (pb "select-authors-by-book-titles" {:titles ["The Death of Grass" "The White Mountains"]})
               => ["SELECT author.* FROM author JOIN book_author USING(author_id) JOIN book USING(book_id) WHERE book.title IN (?,?)"
                   "The Death of Grass"
                   "The White Mountains"])

         (fact "select-books-by-publication-years returns the expected vector"
               (pb "select-books-by-publication-years" {:publication-years [1958 1959 1967]})
               => ["SELECT book.* FROM book WHERE pub_year IN (?,?,?)"
                   1958 1959 1967])))

(ns uk.org.1729.sql-phrasebook-sugar-test
  (:require [uk.org.1729.sql-phrasebook.sugar :refer [import-phrasebook]]
            [clojure.java.jdbc :as jdbc]
            [midje.sweet :refer :all])
  (:import com.googlecode.flyway.core.Flyway
           org.h2.jdbcx.JdbcDataSource))

(defn init-db
  []
  (let [datasource (doto (JdbcDataSource.) (.setURL "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"))]
    (doto (Flyway.) (.setDataSource datasource) (.migrate))
    {:datasource datasource}))

(facts "sql-phrasebook.sugar"
 
       (fact "import-phrasebook"
             (import-phrasebook "test/phrasebook.sql")
             => truthy)

       (jdbc/with-db-connection [conn (init-db)]

         (fact "select-book-titles-by-author returns the expected data"
               (select-book-titles-by-author conn {:last-name "Wyndham"})
               => (just #{{:title "The Day of the Triffids"} {:title "The Kraken Wakes"}}))

         (let [authors (select-authors-by-book-titles conn {:titles "The Day of the Triffids"})]
           (fact "select-authors-by-book-titles returns one author for singleton"
                 (count authors) => 1)
           (fact "select-authors-by-book-titles returns the expected author"
                 (first authors) => (contains {:lastname "Wyndham"})))
         
         (let [books (select-books-by-publication-years conn {:publication-years [1955 1967]})]
           (fact "select-books-by-publication-years returns the expected titles"
                 (map :title books) => (just #{"The Year of the Comet" "The White Mountains"})))))


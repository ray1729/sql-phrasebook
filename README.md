# sql-phrasebook

A Clojure library providing similar functionality to Perl's
[Data::Phrasebook::SQL](http://search.cpan.org/~barbie/Data-Phrasebook-0.33/lib/Data/Phrasebook/SQL.pm).

This library provides a convenient mechanism for looking up a query in
an external dictionary and extracting bind parameters from a Clojure
map, returning a vector suitable for passing straight to
`clojure.java.jdbc/query`. The phrasebook can be managed outside of your
Clojure source code and shared across applications.

## Installation

Available from [Clojars](https://clojars.org/uk.org.1729/sql-phrasebook).

## Usage

The phrasebook is a simple SQL file containing named SQL queries;
each query is preceded by an SQL comment line 'tagging' the query; for
example:

    -- tag: select-users-by-id
    SELECT * FROM user WHERE user_id IN (:user-ids)

(See `resources/test/phrasebook.sql` for a fuller example.)

This file is loaded using `clojure.java.io/resource` so may reside anywhere
in your classpath. With this in hand:

    (require '[uk.org.1729.sql-phrasebook.core :refer [sql-phrasebook]])

    (def pb (sql-phrasebook "test/phrasebook.sql"))

    (pb "select-users-by-id" {:user-ids [123 456 789})

    ;=> ["SELECT * FROM user WHERE user_id IN (?,?,?)" 123 456 789]

    (require '[clojure.java.jdbc :as jdbc])

    (jdbc/query my-db (pb "select-users-by-id" {:user-ids [123 456 789]}))

### ...for those with a sweet tooth

    (require '[uk.org.1729.sql-phrasebook.sugar :refer [import-phrasebook]]
             '[clojure.java.jdbc :as jdbc])

    (def db {:subprotocol "mysql"
             :subname "//127.0.0.1:3306/clojure_test"
             :user "clojure_test"
             :password "clojure_test"})

    (import-phrasebook "test/phrasebook.sql")

    (select-book-titles-by-author db {:last-name "Wyndham"})
    ;=> [{:title "The Day of the Triffids"} {:title "The Kraken Wakes"}]

    (select-authors-by-book-titles db {:titles ["The Day of the Triffids" "The Kraken Wakes"]})
    ; => [{:author_id 2 :lastname "Wyndham" :forenames "John"}]
    
## License

Copyright © 2013 Ray Miller <ray@1729.org.uk>.

Distributed under the Eclipse Public License, the same as Clojure.

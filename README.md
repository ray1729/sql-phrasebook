# sql-phrasebook

A Clojure library providing similar functionality to Perl's
[Data::Phrasebook::SQL](http://search.cpan.org/~barbie/Data-Phrasebook-0.33/lib/Data/Phrasebook/SQL.pm).

This library provides a convenient mechanism for looking up a query in
an external dictionary and extracting bind parameters from a Clojure
map, returning a vector suitable for passing straight to
`clojure.java.jdbc/query`. The phrasebook can be managed outside of your
Clojure source code and shared across applications.


## Usage

The phrasebook is a simple text file containing named SQL queries;
each query is preceded by a line 'tagging' the query name in `[]`; for
example:

    [select-users-by-id]
    SELECT * FROM user WHERE user_id IN (:user-ids)

(See `resources/phrasebook.test` for a fuller example.)

This file is loaded using `clojure.java.io/resource` so may reside anywhere
in your classpath. With this in hand:

    (require '[uk.org.1729.sql-phrasebook.core :refer [sql-phrasebook]])

    (def pb (sql-phrasebook "phrasebook.txt"))

    (pb "select-users-by-id" {:user-ids [123 456 789})

    ;=> ["SEELCT * FROM user WHERE user_id IN (?,?,?)" 123 456 789]

    (require '[clojure.java.jdbc :as jdbc])

    (jdbc/query my-db (pb "select-users-by-id" {:user-ids 123 456 789}))

## Note

The parser treats any string of word characters in the query starting
with a colon as a bind parameter name. This precludes such literal
strings in the query. It would be fairly simple to add an escaping
mechanism if such literals are required, but I haven't needed this
yet.

## License

Copyright © 2013 Ray Miller <ray@1729.org.uk>.

Distributed under the Eclipse Public License, the same as Clojure.

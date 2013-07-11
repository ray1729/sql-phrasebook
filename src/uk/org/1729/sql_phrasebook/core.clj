(ns uk.org.1729.sql-phrasebook.core
  (:require [uk.org.1729.sql-phrasebook.parse :refer [read-phrasebook]]
            [clojure.string :as str]))

(defn- bind-param-names
  "Extract the names of the bind parameters from a query string."
  [query]
  (map second (re-seq #"\$\{([\w-]+)\}" query)))

(defn- ensure-collection
  "Wrap `c` in a vector if it is not already a collection."
  [c]
  (if (coll? c) c (vector c)))

(defn- cleanup-whitespace
  "Trim leading and trailing whitespace and replace runs of whitespace with a single space."
  [s]
  (-> s
      (str/replace #"^\s+" "")
      (str/replace #"\s+$" "")
      (str/replace #"\s+" " ")))

(defn query-and-bind-params
  "Given a parameterized query string and map of parameters, return a
   vector containing the query string (with placeholders in place of
   parameter names) as first element, and bind parameters as
   subsequent elements. This vector is suitable for passing to
   clojure.java.jdbc/query"
  [query params]
  (loop [query query bind-params [] param-names (bind-param-names query)]
    (if (seq param-names)
      (let [param-name  (first param-names)
            param-value (ensure-collection (get params (keyword param-name)))
            replacement (str/join "," (repeat (count param-value) "?"))
            query       (str/replace-first query (str "${" param-name "}") replacement)]
        (recur query (into bind-params param-value) (rest param-names)))
      (into [(cleanup-whitespace query)] bind-params))))

(defn sql-phrasebook
  "Parse the phrasebook `resource-name` and return a function that,
  given a query name and parameter map, returns a vector of query and
  bind parameters suitable for clojure.java.jdbc/query."
  [resource-name]
  (let [pb (read-phrasebook resource-name)]
    (fn lookup-query
      ([query-name]
         (lookup-query query-name {}))
      ([query-name query-params]
         (if-let [query (pb query-name)]
           (query-and-bind-params query query-params)
           (throw (IllegalArgumentException. (str "Phrasebook " resource-name " has no query " query-name))))))))

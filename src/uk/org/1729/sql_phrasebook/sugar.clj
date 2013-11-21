(ns uk.org.1729.sql-phrasebook.sugar
  (:require [uk.org.1729.sql-phrasebook.core :refer [query-and-bind-params]]
            [uk.org.1729.sql-phrasebook.parse :refer [read-phrasebook]]
            [clojure.java.jdbc :as jdbc]))

(defmacro import-phrasebook
  [path]
  `(do ~@(map (fn [[query sql]]
                (let [sym (symbol query)]
                  `(defn ~sym
                     ([db#] (~sym db# {}))
                     ([db# params#] (jdbc/query db# (query-and-bind-params ~sql params#))))))
              (read-phrasebook path))))

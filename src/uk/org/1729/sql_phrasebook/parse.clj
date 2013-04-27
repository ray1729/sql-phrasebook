(ns uk.org.1729.sql-phrasebook.parse
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def ^:private query-tag-rx #"^\[\s*([\w-]+)\s*\]\s*$")

(defn- matches-query-tag?
  [line]
  (boolean (re-matches query-tag-rx line)))

(defn- parse-query-tag
  [line]
  (second (re-matches query-tag-rx line)))

(defn- parse-query-body
  [lines]
  (str/join " " (remove empty? lines)))

(defn read-phrasebook
  [resource-name]
  (with-open [rdr (io/reader (io/resource resource-name))]
    (into {}
          (for [[[tag] body] 
                (partition 2 (partition-by matches-query-tag? (line-seq rdr)))]
            [(parse-query-tag tag) (parse-query-body body)]))))

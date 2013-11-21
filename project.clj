(defproject uk.org.1729/sql-phrasebook "0.2.0-SNAPSHOT"
  :description "An SQL phrasebook allowing you to collect queries in a shared dictionary rather than scattering them throughout your code"
  :url "https://github.com/ray1729/sql-phrasebook"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/java.jdbc "0.3.0-beta1"]]
  :profiles {:dev {:dependencies [[midje "1.5.1"]]
                   :plugins      [[lein-midje "3.0.0"]]}})


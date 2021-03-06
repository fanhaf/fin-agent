(defproject fin-agent "0.1.0-SNAPSHOT"
  :description "Utility service for handling finance ops."
  :url "http://fanhaf-fin-agent.herokuapp.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.1.4"]
                 [ring/ring-jetty-adapter "1.1.6"]
                 [hiccup "1.0.3"]
                 [clojure-csv/clojure-csv "2.0.1"]
                 ]
  :profiles {:dev {:dependencies [[speclj "3.3.0"]]}}
  :test-paths ["spec"]
  :plugins [[lein-ring "0.7.5"]
            [speclj "3.3.0"]]
  :ring {:handler fin-agent.core/application}
  :uberjar-name "fin-agent-standalone.jar"
)

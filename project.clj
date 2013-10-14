(defproject finagent "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [compojure "1.1.4"]
                 [ring/ring-jetty-adapter "1.1.6"]
                 [hiccup "1.0.3"]
                 [clojure-csv/clojure-csv "2.0.0-alpha1"]
]
  :plugins [[lein-ring "0.7.5"]]
  :ring {:handler finagent.core/application}
)

(ns fin-agent.parsers.generic-spec
  (:use [fin-agent.parsers.generic])
  (:require [fin-agent.parsers.mbank :as mbank]
            [speclj.core :refer :all]
            [clojure-csv.core :as csv]))

(def mbank-report-csv
  "l1
l2;l3
h1;h2;h3;h4;h5;h6;h7;h8;h9
2014-01-25;2014-01-25;ZAKUP  PRZY UZYCIU KARTY;\"INDIE     GO GO\";\"  \";'';-2 060,50;56 403,14;
2014-01-27;2014-01-27;ZAKUP  PRZY UZYCIU KARTY;\"CARREFOUR EXPRESS\";\"  \";'';-22,71;56 380,43;
f1;f2;f3;f4;f5;f6;f7;f8;f9
l5
l6")

(def mbank-report (csv/parse-csv mbank-report-csv :delimiter \;))

(def parsed-operations
  [{:date "2014-01-25" :amount "-2060,50" :description "ZAKUP PRZY UZYCIU KARTY: INDIE GO GO"}
  {:date "2014-01-27" :amount "-22,71" :description "ZAKUP PRZY UZYCIU KARTY: CARREFOUR EXPRESS"}])

(describe "Operations joining function"
  (it "should use find operations in provided string then use provided \"to-operation\" function to parse all operations and return joined maps of operations"
      (should (= (to-operation-list mbank/filter-operations-source mbank/to-operation mbank-report) parsed-operations))))

(run-specs)

(ns fin-agent.parsers.mbank-spec
  (:use [fin-agent.parsers.mbank])
  (:require [speclj.core :refer :all]
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
(def operations-source (filter-operations-source mbank-report))


(describe "CSV filter functions"
          (it "should locate operation sections with 9 columns in parsed csv report"
              (should 
                (= (count (first operations-source)) 9)
                )))

(describe "CSV filter functions"
          (it "should locate two records of operations in operation test data"
              (should
                (= (count operations-source) 2))))

(describe "CSV filter functions"
          (it "the first string of the first record in test data should be \"2014-01-25\""
              (should (= (first (first operations-source))
                         "2014-01-25"))))

(def operation-record (first operations-source))

(describe "Operation type parsing"
          (it "should find that operation-type in test record is \"ZAKUP PRZY UZYCIU KARTY\""
              (should (= (operation-type operation-record)
                         "ZAKUP PRZY UZYCIU KARTY"))))

(describe "Operation description parsing"
          (it "should find that operation-description in test record is \"INDIE GO GO\""
              (should (= (operation-description operation-record)
                         "INDIE GO GO"))))

(describe "Operation amount parsing"
          (it "should filter whitespaces in amount string"
              (should (= (prepare-amount "-2 060,50")
                         "-2060,50"))))

(def parsed-operations
  {:date "2014-01-25" :amount "-2060,50" :description "ZAKUP PRZY UZYCIU KARTY: INDIE GO GO"})

(describe "to-operation function"
          (it "should prepare a correct map of values from test operation source"
              (should (= (to-operation operation-record)
                         parsed-operations))))

(run-specs)


(ns fin-agent.parsers.pekao-arch-spec
  (:use [fin-agent.parsers.pekao-arch])
  (:require [speclj.core :refer :all]
            [clojure-csv.core :as csv]))

(def pekao-report-csv
"Data waluty	Szczegó³y odbiorcy/nadawcy	Rachunek odbiorcy/nadawcy	Tytu³em	Kwota operacji	Waluta	Typ operacji
20130911	Elmak	17500012  66175000120000000010953901	Urlop	896,93	PLN	PRZELEW KRAJOWY
20130910	TRAWERS LODZ PL 32195698 71655875 *********3019288			-699,00	PLN	TRANSAKCJA KART¥ P£ATNICZ¥
")

(def pekao-report (csv/parse-csv pekao-report-csv :delimiter \tab))
(def operations-source (filter-operations-source pekao-report))

(describe "CSV filter functions"
  (it "should locate operations section with two records"
      (should (= (count operations-source) 2))))

(def operation-record (first operations-source))
(describe "CSV filter functions"
  (it "should find first record that have 7 fields"
      (should (= (count operation-record) 7))))

(describe "CSV filter functions"
  (it "should find records and first field in records should be \"20130911\""
      (should (= (first operation-record) "20130911"))))

(describe "Operation date parsing"
          (it "should find that operation-date in test record is \"2013-09-11\""
              (should= (operation-date operation-record)
                         "2013-09-11")))

(describe "Operation type parsing"
          (it "should find that operation-type in test record is \"PRZELEW KRAJOWY\""
              (should= (operation-type operation-record)
                         "PRZELEW KRAJOWY")))

(describe "Operation recipient parsing"
          (it "should find that operation-recipient in test recored is \"Elmak\""
              (should= (operation-recipient operation-record)
                         "Elmak")))

(describe "Operation description parsing"
          (it "should find that operation-description in test record is \"Urlop\""
              (should= (operation-description operation-record)
                         "Urlop")))

(describe "Operation amount parsing"
          (it "should find that amount of operation in test record is \"896,93\""
              (should= (operation-amount operation-record) 
                         "896,93")))

(def parsed-operations
  {:date "2013-09-11" :amount "896,93" :description "PRZELEW KRAJOWY: Elmak: Urlop"})

(describe "to-operation function"
          (it "should prepare a correct map of values from test operation source"
              (should= (to-operation operation-record)
                         parsed-operations)))

(run-specs)

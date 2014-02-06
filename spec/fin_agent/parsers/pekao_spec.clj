(ns fin-agent.parsers.pekao-spec
  (:use [fin-agent.parsers.pekao])
  (:require [speclj.core :refer :all]
            [clojure-csv.core :as csv]))

(def pekao-report-csv
"Data ksiegowania	Data waluty	Szczeg odbiorcy/nadawcy	Rachunek odbiorcy/nadawcy	Tytuem	Kwota operacji	Waluta	Numer referencyjny	Typ operacji
20131031	20131030	Tadeusz Janiak	83124055851111000048853921	tel. 2013-10	-39,43	PLN	4201030605608183	PRZELEW INTERNET
20131031	20131031	APTEKA ZARZEWSKA EWA K LODZ 42210328 71110053 *********3019288			-15,90	PLN		TRANSAKCJA KART P£ATNICZ
")

(def pekao-report (csv/parse-csv pekao-report-csv :delimiter \tab))
(def operations-source (filter-operations-source pekao-report))

(describe "CSV filter functions"
  (it "should locate operations section with two records"
      (should (= (count operations-source) 2))))

(def operation-record (first operations-source))
(describe "CSV filter functions"
  (it "should find first record that have 9 fields"
      (should (= (count operation-record) 9))))

(describe "CSV filter functions"
  (it "should find records and first field in records should be \"20131031\""
      (should (= (first operation-record) "20131031"))))

(describe "Operation date parsing"
          (it "should find that operation-date in test record is \"2013-10-31\""
              (should (= (operation-date operation-record)
                         "2013-10-31"))))

(describe "Operation type parsing"
          (it "should find that operation-type in test record is \"PRZELEW INTERNET\""
              (should (= (operation-type operation-record)
                         "PRZELEW INTERNET"))))

(describe "Operation recipient parsing"
          (it "should find that operation-recipient in test recored is \"Tadeusz Janiak\""
              (should (= (operation-recipient operation-record)
                         "Tadeusz Janiak"))))

(describe "Operation description parsing"
          (it "should find that operation-description in test record is \"tel. 2013-10\""
              (should (= (operation-description operation-record)
                         "tel. 2013-10"))))

(describe "Operation amount parsing"
          (it "should find that amount of operation in test record is \"-39,43\""
              (should (= (operation-amount operation-record) 
                         "-39,43"))))

(def parsed-operations
  {:date "2013-10-31" :amount "-39,43" :description "PRZELEW INTERNET: Tadeusz Janiak: tel. 2013-10"})

(describe "to-operation function"
          (it "should prepare a correct map of values from test operation source"
              (should (= (to-operation operation-record)
                         parsed-operations))))

(run-specs)

(ns fin-agent.parsers.pekao-spec
  (:use [fin-agent.parsers.pekao])
  (:require [speclj.core :refer :all]
            [clojure-csv.core :as csv]))

(def pekao-report-csv-old
"Data ksiegowania	Data waluty	Szczeg odbiorcy/nadawcy	Rachunek odbiorcy/nadawcy	Tytuem	Kwota operacji	Waluta	Numer referencyjny	Typ operacji
20131031	20131030	Tadeusz Janiak	83124055851111000048853921	tel. 2013-10	-39,43	PLN	4201030605608183	PRZELEW INTERNET
20131031	20131031	APTEKA ZARZEWSKA EWA K LODZ 42210328 71110053 *********3019288			-15,90	PLN		TRANSAKCJA KART P£ATNICZ
")

(def pekao-report-csv
"Data księgowania;Data waluty;Nadawca / Odbiorca;Rachunek docelowy / źródłowy;Tytułem;Kwota operacji;Waluta;Numer referencyjny;Typ operacji;Kategoria
03.09.2020;03.09.2020;SumUp *Katarzyna Durm lodz;'86124030731111001036284341;*********0001806;-12,00;PLN;'C992024713295743;TRANSAKCJA KARTĄ PŁATNICZĄ;Artykuły spożywcze
03.09.2020;03.09.2020;PH PAPIER E&S S.C. Lodz;'86124030731111001036284341;*********0001806;-4,00;PLN;'C992024713293230;TRANSAKCJA KARTĄ PŁATNICZĄ;Artykuły szkolne/biurowe
")

(def pekao-report (csv/parse-csv pekao-report-csv :delimiter \;))
(def operations-source (filter-operations-source pekao-report))

(describe "CSV filter functions"
  (it "should locate operations section with two records"
      (should (= (count operations-source) 2))))

(def operation-record (first operations-source))
(describe "CSV filter functions"
  (it "should find first record that have 10 fields"
      (should (= (count operation-record) 10))))

(describe "CSV filter functions"
  (it "should find records and first field in records should be \"03.09.2020\""
      (should (= (first operation-record) "03.09.2020"))))

(describe "Operation date parsing"
          (it "should find that operation-date in test record is \"2020.09.03\""
              (should (= (operation-date operation-record)
                         "2020-09-03"))))

(describe "Operation type parsing"
          (it "should find that operation-type in test record is \"TRANSAKCJA KARTĄ PŁATNICZĄ\""
              (should (= (operation-type operation-record)
                         "TRANSAKCJA KARTĄ PŁATNICZĄ"))))

(describe "Operation recipient parsing"
          (it "should find that operation-recipient in test recored is \"SumUp *Katarzyna Durm lodz\""
              (should (= (operation-recipient operation-record)
                         "SumUp *Katarzyna Durm lodz"))))

(describe "Operation description parsing"
          (it "should find that operation-description in test record is \"*********0001806\""
              (should (= (operation-description operation-record)
                         "*********0001806"))))

(describe "Operation amount parsing"
          (it "should find that amount of operation in test record is \"-12,00\""
              (should (= (operation-amount operation-record) 
                         "-12,00"))))

(def parsed-operations
  {:date "2020-09-03" :amount "-12,00" :description "TRANSAKCJA KARTĄ PŁATNICZĄ: SumUp *Katarzyna Durm lodz: *********0001806"})

(describe "to-operation function"
          (it "should prepare a correct map of values from test operation source"
              (should (= (to-operation operation-record)
                         parsed-operations))))

(run-specs)

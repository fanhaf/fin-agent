(ns fin-agent.formatters.qif-spec
  (:use [fin-agent.formatters.qif])
  (:require [speclj.core :refer :all]))

(def operation-record
  {:date "2014-01-25" :amount "-2060,50" :description "ZAKUP PRZY UZYCIU KARTY: INDIE GO GO"})

(def operation-qif
  "D2014-01-25\nPZAKUP PRZY UZYCIU KARTY: INDIE GO GO\nT-2060,50\n^\n")

(describe "to-qif function"
  (it "should translate operation record to qif record"
      (should (= (to-qif operation-record) operation-qif))))


(describe "qif-header function"
  (it "should return header with mBank account name when provided this account name"
      (should (= (qif-header "mBank") "!Account\nNmBank\nTBank\n^\n!Type:Bank\n"))))

(run-specs)

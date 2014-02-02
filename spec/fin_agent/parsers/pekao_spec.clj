(ns fin-agent.parsers.pekao-spec
  (:use [fin-agent.parsers.pekao])
  (:require [speclj.core :refer :all]
            [clojure-csv.core :as csv]))


(describe "CSV filter functions"
  (it "should locate operations section"
      (should (= 1 0))))

(run-specs)

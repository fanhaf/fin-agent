(ns fin-agent.parsers.mbank
  (:use [clojure.string :only (join split)]))

;####################
(defn filter-operation-columns [report] (filter #(= (count %) 9) report))
(defn truncate-header [operation-report] (drop 1 operation-report))
(defn truncate-footer [operation-report] (drop-last operation-report))

(defn filter-operations-source [report]
  (-> report
    filter-operation-columns
    truncate-header
    truncate-footer))
;####################

(defn filter-whitespaces [string] (filter #(not= \space %) string))

(defn words [string] (split string #"[ \t]"))
(defn filter-empty [list] (filter #(not-empty %) list))
(defn filter-duplicate-whitespace [string]
   (join " " (filter-empty (words string))))

(defn operation-type [trans-src] (filter-duplicate-whitespace (nth trans-src 2)))
(defn operation-description [trans-src] (filter-duplicate-whitespace (nth trans-src 3)))
;####################

(defn prepare-amount [amount-string] (apply str (filter-whitespaces amount-string)))
(defn prepare-description [trans-src]
  (let [op-type (operation-type trans-src)
        op-desc (operation-description trans-src)]
    (apply str op-type ": " op-desc)))
;####################

(defn to-operation [trans-src]
  (let [date (nth trans-src 1)
        amount (prepare-amount (nth trans-src 6))
        description (prepare-description trans-src)
        ]
    {:date date :amount amount :description description}))


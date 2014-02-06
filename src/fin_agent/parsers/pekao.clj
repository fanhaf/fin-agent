(ns fin-agent.parsers.pekao
  )

;####################
(defn filter-operation-columns [report] (filter #(= (count %) 9) report))
(defn truncate-header [operation-report] (drop 1 operation-report))
(defn truncate-footer [operation-report] (drop-last operation-report))

(defn filter-operations-source [report]
  (-> report
    filter-operation-columns
    truncate-header))
;####################

(defn str-insert
  "Insert c in string s at index i."
  [s c i]
  (str (subs s 0 i) c (subs s i)))

(defn operation-date [trans-src] 
  (let [date-string (nth trans-src 0)]
    (str-insert (str-insert date-string \- 6) \- 4)))

(defn operation-type [trans-src] (nth trans-src 8))

(defn operation-recipient [trans-src] (nth trans-src 2))

(defn operation-description [trans-src] (nth trans-src 4))

(defn operation-amount [trans-src] (nth trans-src 5))

(defn prepare-description [trans-src]
  (let [op-type (operation-type trans-src)
        op-recp (operation-recipient trans-src)
        op-desc (operation-description trans-src)]
    (apply str op-type ": " op-recp ": " op-desc)))

(defn to-operation [trans-src]
  (let [date (operation-date trans-src)
        amount (operation-amount trans-src)
        description (prepare-description trans-src)
        ]
    {:date date :amount amount :description description}))
 

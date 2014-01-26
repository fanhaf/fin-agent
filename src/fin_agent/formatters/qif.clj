(ns fin-agent.formatters.qif)

(def qif-trans-format "D%s\nP%s\nT%s\n^\n")
(defn to-qif [operation]
  (format qif-trans-format (operation :date) (operation :description) (operation :amount)))

(defn operations-in-qif [operation-list]
  (map to-qif operation-list))

(defn operations-to-qif-list [report] 
  (let [operation-list (filter-operations-source report)]
    (->> operation-list
      (map to-operation)
      (map to-qif))))

(def qif-header "!Account\nNmBank\nTBank\n^\n!Type:Bank\n")
(defn report-as-qif [report]
  (apply str qif-header (operations-to-qif-list report)))

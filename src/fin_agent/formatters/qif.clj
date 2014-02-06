(ns fin-agent.formatters.qif)

(def qif-trans-format "D%s\nP%s\nT%s\n^\n")
(defn to-qif [operation]
  (format qif-trans-format (operation :date) (operation :description) (operation :amount)))

(defn operations-to-qif-list [operation-list] 
      (map to-qif operation-list))

(def qif-header-format "!Account\nN%s\nTBank\n^\n!Type:Bank\n")
(defn qif-header [account-name] 
  (format qif-header-format account-name))

(defn report-as-qif [report]
  (apply str qif-header (operations-to-qif-list report)))

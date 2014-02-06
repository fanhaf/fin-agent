(ns fin-agent.parsers.generic)

(defn to-operation-list [operation-filter-fun to-operation-fun source]
  (let [operations-source (operation-filter-fun source)]
    (map to-operation-fun operations-source)))

(ns fin-agent.controllers.csvtranslator
  (:use [compojure.core :only (defroutes GET POST)]
        [clojure.java.io]
        [hiccup.core]
        [clojure.string :only (join split)])
  (:require [clojure-csv.core :as csv])
  (:import [java.io File])
  )

(defn home-page []
    (html [:form {:action "/translator/mbank" :method "post" :enctype "multipart/form-data"}
                    [:input {:name "file" :type "file" :size "20"}]
                    [:input {:type "submit" :name "submit" :value "submit"}]]))

; ----------------
(defn filter-operation-columns [report] (filter #(= (count %) 9) report))
(defn truncate-header [operation-report] (drop 1 operation-report))
(defn truncate-footer [operation-report] (drop-last operation-report))

(defn filter-operations-source [report] 
  (-> report 
    filter-operation-columns
    truncate-header
    truncate-footer))

(defn filter-whitespaces [string] (filter #(not= \space %) string))

(defn words [string] (split string #"[ \t]"))
(defn filter-empty [list] (filter #(not-empty %) list))
(defn filter-duplicate-whitespace [string]
   (join " " (filter-empty (words string))))

(defn operation-type [trans-src] (filter-duplicate-whitespace (nth trans-src 2)))
(defn operation-description [trans-src] (filter-duplicate-whitespace (nth trans-src 3)))

(defn prepare-amount [amount-string] (apply str (filter-whitespaces amount-string)))
(defn prepare-description [trans-src] 
  (let [op-type (operation-type trans-src)
        op-desc (operation-description trans-src)]
    (apply str op-type ": " op-desc)))

(defn to-operation [trans-src]
  (let [date (nth trans-src 1)
        amount (prepare-amount (nth trans-src 6)) 
        description (prepare-description trans-src)
        ]
    {:date date :amount amount :description description}))

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

(defn qif-response [report report-filename]
  {:status 200
   :headers {"Content-Type" "text/plain; charset=\"UTF-8\""
             "Content-Disposition" (str "attachment; filename=" report-filename)}
   :body (report-as-qif report)})

(defroutes routes
           (GET "/translator/mbank" []
                (home-page))
           (POST "/translator/mbank" {params :params}
                 (let [file-param (params "file")
                       temp-file (file-param :tempfile)
                       report-string (slurp temp-file :encoding "windows-1250")
                       report (csv/parse-csv report-string :delimiter \;)
                       ]
                   (qif-response report (str (file-param :filename) ".qif"))
                   )))

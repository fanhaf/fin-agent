(ns fin-agent.controllers.csvtranslator
  (:use [compojure.core :only (defroutes GET POST)]
        [clojure.java.io]
        [hiccup.core]
        [clojure.string :only (join split)])
  (:require [clojure-csv.core :as csv])
  (:import [java.io File])
  )

(defn home-page []
    (html [:form {:action "/translator/pekao" :method "post" :enctype "multipart/form-data"}
                    [:input {:name "file" :type "file" :size "20"}]
                    [:input {:type "submit" :name "submit" :value "submit"}]]))

(defn parse-csv-account [filename]
  (let [x (slurp filename :encoding "Windows-1250")]
    (csv/parse-csv x :delimiter \;)))

(defn operations-table [parsed-report]
  (filter #(= (count %) 9) parsed-report))

(defn operations [table]
  (drop 1 (drop-last (operations-table table))))

(defn show-operations [filename]
  (let [report (parse-csv-account filename)]
    (operations report)))

(def qif-trans-format "D%s\nP%s\nT%s\n^\n")

(defn coerce-to-qif-trans [op]
  (let [date (nth op 1)
        description (join " " (distinct (split (str (nth op 2) ": " (nth op 3)) #" ")))
        amount (nth op 6)]
    (format qif-trans-format date description amount)))

(defn as-qif [trans-table]
  (map coerce-to-qif-trans trans-table))

(defn upload-file [file]
  (let [file-name (file :filename)
        size (file :size)
        actual-file (file :tempfile)]
    (copy actual-file (File. (format "/tmp/upload" file-name)))))

(defn output-file [file-content]
  (spit "/tmp/transactions.qif" (apply str file-content)))

(defroutes routes
           (GET "/translator/pekao" []
                (home-page))
           (POST "/translator/pekao" {params :params}
                 (let [file (get params "file")]
                   (upload-file file))
                 (output-file (as-qif (show-operations "/tmp/upload")))
                 (file "/tmp/transactions.qif"))
           )


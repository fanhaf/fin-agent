(ns fin-agent.controllers.mbank
  (:use [compojure.core :only (defroutes GET POST)]
        [clojure.java.io]
        [hiccup.core])
  (:require [fin-agent.parsers.mbank :as mbank]
            [fin-agent.parsers.generic :as generic]
            [fin-agent.formatters.qif :as qif]
            [clojure-csv.core :as csv])
  (:import [java.io File]))

(defn home-page []
    (html [:form {:action "/translator/mbank" :method "post" :enctype "multipart/form-data"}
                    [:input {:name "file" :type "file" :size "20"}]
                    [:input {:type "submit" :name "submit" :value "submit"}]]))

(defn qif-response [operations qif-filename]
  {:status 200
   :headers {"Content-Type" "text/plain; charset=\"UTF-8\""
             "Content-Disposition" (str "attachment; filename=" qif-filename)}
   :body (qif/report-as-qif operations)})

(defroutes routes
           (GET "/translator/mbank" []
                (home-page))
           (POST "/translator/mbank" {params :params}
                 (let [file-param (params "file")
                       temp-file (file-param :tempfile)
                       report-string (slurp temp-file :encoding "windows-1250")
                       report (csv/parse-csv report-string :delimiter \;)
                       operations (generic/to-operation-list mbank/filter-operations-source mbank/to-operation report)
                       ]
                   (qif-response operations (str (file-param :filename) ".qif"))
                   )))

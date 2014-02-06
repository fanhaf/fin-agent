(ns fin-agent.controllers.pekao
  (:use [compojure.core :only (defroutes GET POST)]
        [clojure.java.io]
        [hiccup.core])
  (:require [fin-agent.parsers.pekao :as pekao]
            [fin-agent.parsers.generic :as generic]
            [fin-agent.formatters.qif :as qif]
            [clojure-csv.core :as csv])
  (:import [java.io File]))

(defn home-page []
    (html [:form {:action "/translator/pekao" :method "post" :enctype "multipart/form-data"}
                    [:input {:name "file" :type "file" :size "20"}]
                    [:input {:type "submit" :name "submit" :value "submit"}]]))

(defn qif-response [account-header operations qif-filename]
  {:status 200
   :headers {"Content-Type" "text/plain; charset=\"UTF-8\""
             "Content-Disposition" (str "attachment; filename=" qif-filename)}
   :body (qif/report-as-qif account-header operations)})

(defroutes routes
           (GET "/translator/pekao" []
                (home-page))
           (POST "/translator/pekao" {params :params}
                 (let [file-param (params "file")
                       temp-file (file-param :tempfile)
                       report-string (slurp temp-file :encoding "windows-1250")
                       report (csv/parse-csv report-string :delimiter \tab)
                       operations (generic/to-operation-list pekao/filter-operations-source pekao/to-operation report)
                       ]
                   (qif-response "Pekao-net" operations (str (file-param :filename) ".qif"))
                   )))

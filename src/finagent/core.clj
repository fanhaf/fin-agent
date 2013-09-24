(ns finagent.core
  (:use [compojure.core :only (defroutes)])
  (:require [finagent.controllers.csvtranslator :as translator]
            [ring.middleware.params :as params]
            [ring.middleware.multipart-params :as multipart-params])
  )

(defroutes routes
  translator/routes)

(def application (-> routes
                   params/wrap-params
                   multipart-params/wrap-multipart-params))

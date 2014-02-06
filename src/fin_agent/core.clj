(ns fin-agent.core
  (:use [compojure.core :only (defroutes)])
  (:require [fin-agent.controllers.mbank :as mbank-controller]
            [fin-agent.controllers.pekao :as pekao-controller]
            [ring.middleware.params :as params]
            [ring.middleware.multipart-params :as multipart-params]
            [ring.adapter.jetty :as jetty])
  )

(defroutes routes
  mbank-controller/routes
  pekao-controller/routes
  )

(def application (-> routes
                   params/wrap-params
                   multipart-params/wrap-multipart-params))

(defn -main [port]
    (jetty/run-jetty application {:port (Integer. port) :join? false}))

(ns pik-notifications.handler
  (:require [clojure.tools.logging :as log]
            [compojure.api.sweet :refer :all]
            [compojure.route]
            [compojure.api.exception :as ex]
            [ring.util.http-response :as response]
            [ring.logger :as logger]
            [pik-notifications.routes.home :refer [home-routes]]
            [pik-notifications.routes.google :refer [google-routes]]
            [pik-notifications.routes.db :refer [db-routes]]))


(defn custom-ex-handler [f type]
  (fn [^Exception e data request]
    (let [msg (.getMessage e)]
      (log/error msg)
      (f {:message msg :type type}))))


(def api-routes
  (api
    {:coercion nil
     :exceptions
     {:handlers
       {::ex/default (custom-ex-handler response/internal-server-error :error)
        ::ex/request-validation (custom-ex-handler response/bad-request :error)}}}

    (context "/api" []
      #'home-routes

      (context "/v1" []
        :coercion :spec
        #'google-routes
        #'db-routes))

    (undocumented
      (compojure.route/not-found (response/not-found "resource not found")))))


(defn app []
  (-> #'api-routes
      logger/wrap-with-logger))

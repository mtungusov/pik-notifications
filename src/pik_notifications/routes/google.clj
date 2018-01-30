(ns pik-notifications.routes.google
  (:require [compojure.api.sweet :refer [defroutes GET POST]]
            [ring.util.http-response :refer [ok]]
            [spec-tools.core :as st]
            [pik-notifications.validator :as v]
            [pik-notifications.ext-api.fcm :refer [send-all]]))


(defroutes google-routes
  (POST "/google" []
    :body [n (st/spec ::v/notification-request)]
    (ok {:result (send-all n)})))

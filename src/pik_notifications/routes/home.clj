(ns pik-notifications.routes.home
  (:require [compojure.api.sweet :refer [defroutes GET]]
            [ring.util.http-response :refer [ok]]))


(defroutes home-routes
  (GET "/" [] (ok "Notification Service for APNS and FCM"))
  (GET "/ping" [] (ok {:result "pong"})))

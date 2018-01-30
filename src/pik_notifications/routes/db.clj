(ns pik-notifications.routes.db
  (:require [compojure.api.sweet :refer [defroutes GET]]
            [ring.util.http-response :refer [ok]]
            [pik-notifications.db.core :refer [invalid-tokens]]))


(defroutes db-routes
  (GET "/invalid-tokens" []
    (ok {:ivalid-tokens (invalid-tokens)})))

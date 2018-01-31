(ns pik-notifications.routes.db
  (:require [compojure.api.sweet :refer [defroutes GET POST]]
            [ring.util.http-response :refer [ok]]
            [pik-notifications.db.core :refer [invalid-tokens]]
            [spec-tools.core :as st]
            [pik-notifications.validator :as v]
            [pik-notifications.db.core :refer [delete-invalid-tokens]]))


(defroutes db-routes
  (GET "/invalid-tokens" []
    (ok {:ivalid-tokens (invalid-tokens)}))
  (POST "/delete-invalid-tokens" []
    :body [c (st/spec ::v/delete-invalid-tokens-request)]
    (let [confirm-str (:confirm-delete-invalid-tokens c)
          res (delete-invalid-tokens confirm-str)]
      (ok {:result (when res "success")}))))

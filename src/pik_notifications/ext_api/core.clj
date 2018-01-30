(ns pik-notifications.ext-api.core
  (:require [slingshot.slingshot :refer [try+ throw+]]
            [org.httpkit.client :refer [post]]
            [jsonista.core :as j]
            [safely.core :refer [safely]]))


(defn to-json [v]
  (j/write-value-as-string v))


(defn from-json [s]
  (j/read-value s (j/object-mapper {:decode-key-fn true})))


(defn post-try [url params]
  (try+
    (let [{:keys [status body headers error] :as resp} @(post url params)]
      (if error
        (throw+ "my-network-error")
        (if (re-matches #"5\d{2}" (str status))
          (throw+ "my-5xx-error")
          (cond-> {:status status}
            (= 401 status) (assoc :error "API Authentication Error")
            (= 400 status) (assoc :error "Invalid Request")
            (= 200 status) (assoc :body (from-json body))))))))


(defn post-safely [url params]
  (safely
    (post-try url params)
    :on-error
    :log-errors true
    :max-retry 10
    :retry-delay [:random-exp-backoff :base 500 :+/- 0.50 :max 180000]))

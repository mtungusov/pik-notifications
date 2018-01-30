(ns pik-notifications.ext-api.fcm
  (:require [pik-notifications.config :refer [settings]]
            [pik-notifications.ext-api.core :as c]
            [pik-notifications.db.core :as db]))


(def api-url "https://fcm.googleapis.com/fcm/send")

(defn api-key []
  (get settings :google-api-key))


(def max-ids-in-notification 1000)

(def dry-run true)

(defn default-params []
  {:insecure? true
   :headers {"Authorization" (str "key=" (api-key))
             "Content-Type" "application/json"}})


(defn- make-notification [ids text]
  {:registration_ids ids
   :data {:text text
          :sound :default}
   :dry_run dry-run})


(defn- make-request [notification]
  (assoc (default-params) :body (c/to-json notification)))


(defn- process-result [[k v]]
  (let [err-type (:error v)]
    (case err-type
      "InvalidRegistration" (db/conj-invalid-id k)
      "NotRegistered" (db/conj-not-registered-id k))))
      ;"Unavailable" (println k " -send- " err-type))))


(defn- update-incorrect-ids [ids results]
  (->> (zipmap ids results)
       (filter #(contains? (second %) :error))
       (map process-result)))


(defn- process-resp-body! [ids body]
  (let [{:keys [success results]} body]
    (doall
      (update-incorrect-ids ids results))
    {:success success}))


(defn send-notification [ids text]
  (let [filtered-ids (db/filter-ids ids)]
    (when-not (empty? filtered-ids)
      (let [req (make-request (make-notification filtered-ids text))
            {:keys [status body error] :as resp} (c/post-safely api-url req)]
        (cond-> {:status status}
          error (assoc :error error)
          (= 200 status) (assoc :result (process-resp-body! filtered-ids body)))))))




;; "notification-req":{"alert":"Привет!","tokens":["tok1" "tok2"]}

(defn send-all [{:keys [alert tokens]}]
  (->> (partition max-ids-in-notification max-ids-in-notification nil tokens)
      (map #(send-notification % alert))
      (remove nil?)
      (reduce conj [])))

;(def ids ["001" "APA91bGms1iKIPdKURo7CAi1bGx4kZ_1EB-8m950An3LPDkvMRkxRKofz7yvuZGvzrFpDl6hjOz5zjNbMA1ue2xfBknnGU-dwkUVC74A-rYHIOdydX2PgDZ4IPdT2C_Gjl6eHq_5c356" "APA91bHj6HU0Zvftt8bGmWo1zf95s0M0KT3K4_6U55LN9tByUM_J3r3yqW41ehelDMb2cRZ0iiR0xcvo3g1nS-rKTWGi8PK4Ln8K9zRVIBtB_HIp6iZFlCqtpOP9P7H5wp6e_4cSXAL5" "234" "456"])

;(def r (send-notification ids "t-0"))
;(identity r)

;(partition max-ids-in-notification ids)
;(partition max-ids-in-notification max-ids-in-notification nil ids)

;(identity @db/invalid-ids)

;(send-all ids "t-1")

;(def resp (post api-url (make-request (make-notification ids "test-0"))))
;(:body @resp)

;@(post api-url (make-request (make-notification ids "test-0")))

;(c/post-try api-url (make-request (make-notification ids "test-1")))
;(c/post-safely api-url (make-request (make-notification ids "test-1")))
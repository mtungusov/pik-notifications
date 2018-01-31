(ns pik-notifications.db.core
  (:require [clojure.set :refer [union difference]]
            [pik-notifications.config :refer [settings]]))


(def invalid-g-ids (atom #{}))
;#{id1 id2 id3 ...}

(def not-registered-g-ids (atom #{}))
;#{id1 id2 id3 ...}


(defn filter-ids [ids]
  (vec (difference (set ids) (union @invalid-g-ids @not-registered-g-ids))))


(defn conj-invalid-id [id]
  (swap! invalid-g-ids conj id))


(defn conj-not-registered-id [id]
  (swap! not-registered-g-ids conj id))


(defn invalid-tokens []
  {:google (union @invalid-g-ids @not-registered-g-ids)})

(defn delete-invalid-tokens [confirm-str]
  (when (= confirm-str (:confirm-delete-invalid-tokens settings))
    (reset! invalid-g-ids #{})
    (reset! not-registered-g-ids #{})))

;(swap! invalid-g-ids conj 1)
;(identity @invalid-g-ids)
;(swap! not-registered-g-ids conj 6)
;(identity @not-registered-g-ids)
;(:confirm-delete-invalid-tokens settings)

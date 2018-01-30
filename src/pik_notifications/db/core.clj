(ns pik-notifications.db.core
  (:require [clojure.set :refer [union difference]]))


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

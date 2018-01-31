(ns pik-notifications.validator
  (:require [clojure.spec.alpha :as s]))


(def token-regex #"^[a-zA-Z0-9_-]+$")

(def alert-regex #"^\S+.*\S+$")


(s/def ::token
  (s/and string? #(re-matches token-regex %)))

;(s/explain ::token "1a-qw_Asd-0")


(s/def
  ::tokens
  (s/coll-of ::token
             :min-count 1))
             ;:max-count 1000))


(s/def ::alert
  (s/and string?
         #(<= 1 (count %) 1024)
         #(re-matches alert-regex %)))

;(s/explain ::alert "Щф фыва 12")


(s/def
  ::notification-request
  (s/keys :req-un [::tokens ::alert]))

;(s/explain
;  ::notification-request
;  {:tokens ["qwer" "123we"]
;   :alert "test 1"})

(s/def
  ::confirm-delete-invalid-tokens
  string?)

;(s/explain ::confirm-delete-invalid-tokens "")

(s/def
  ::delete-invalid-tokens-request
  (s/keys :req-un [::confirm-delete-invalid-tokens]))

;(s/explain ::delete-invalid-tokens-request {:confirm-delete-invalid-tokens "qwe"})
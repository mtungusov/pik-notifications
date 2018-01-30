(ns pik-notifications.core
  (:require [clojure.tools.logging :as log]
            [mount.core :as mount]
            [org.httpkit.server :refer [run-server]]
            [pik-notifications.config :refer [settings]]
            [pik-notifications.handler :as handler])
  (:gen-class))


(defonce state (atom {:running false
                      :server nil}))


(defn- init [args]
  (log/info "Notification Service for APNS and FCM starting...")
  (swap! state assoc :running true)
  (mount/start))


(defn- start-server []
  (log/info "Start Server")
  (when-let [port (get-in settings [:server :port])]
    (swap! state :server (run-server (handler/app) {:port port}))
    (log/info "Listen at: " port)))


(defn- stop []
  (log/info "Stopping...")
  (swap! state assoc :running false)
  (when-let [server (:server @state)]
    (server :timeout 1000)
    (swap! state assoc :server nil)
    (log/info "Server stopped"))
  (shutdown-agents)
  (Thread/sleep 1000)
  (log/info "Stopped"))


(defn -main [& args]
  (init args)
  (.addShutdownHook (Runtime/getRuntime)
                    (Thread. stop))
  (start-server))

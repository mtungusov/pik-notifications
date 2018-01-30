(defproject pik-notifications "0.1.0-SNAPSHOT"
  :description "Notification Service for APNS and FCM"

  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/tools.logging "0.4.0"]
                 [org.slf4j/slf4j-log4j12 "1.7.25"]
                 [log4j/log4j "1.2.17" :exclusions [javax.mail/mail
                                                    javax.jms/jms
                                                    com.sun.jmdk/jmxtools
                                                    com.sun.jmx/jmxri]]
                 [mount "0.1.11"]
                 [cprop "0.1.11"]
                 [http-kit "2.2.0"]
                 [metosin/compojure-api "2.0.0-alpha17"]
                 [metosin/spec-tools "0.5.1"]
                 [metosin/jsonista "0.1.1"]
                 [ring-logger "0.7.7"]
                 [slingshot "0.12.2"]
                 [com.brunobonacci/safely "0.3.0"]]


  :main pik-notifications.core
  :profiles {:uberjar {:omit-source true
                       :aot :all
                       :uberjar-name "pik-notifications.jar"}})

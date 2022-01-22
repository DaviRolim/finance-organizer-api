(ns organize-expenses.components.server
  (:require [io.pedestal.http :as http]
            [io.pedestal.interceptor :as i]
            [io.pedestal.test :as test]
            [cheshire.core :as json]
            [datomic.client.api :as d]
            [com.stuartsierra.component :as component]))




(defn- service-map-base [routes] {::http/routes routes
                                  ::http/port   8890
                                  ::http/type   :jetty
                                  ::http/join?  false})

(defn- service-map
  [service-map-base db-interceptor]
  (-> service-map-base
      (http/default-interceptors)
      (update ::http/interceptors conj (i/interceptor db-interceptor))))

(defn- start-server [server service-map]
  (reset! server (http/start (http/create-server service-map))))

(defprotocol ServerProvider
  (test-request [self verb url]
    "Execute http tests using REPL")
  (test-request-with-body [self verb url body]))

(defrecord Server [database routes]
  component/Lifecycle
  (start [this]
    (println "Starting Server")
    (let [db-interceptor {:name  :db-interceptor
                          :enter (fn
                                   [context]
                                   (-> context
                                       (assoc-in [:request :conn] (:conn database))
                                       (assoc-in [:request :db] (d/db (:conn database)))))}
          s-map (service-map (service-map-base (:routes routes)) db-interceptor)
          server (atom nil)]
      (start-server server s-map)
      (assoc this :server server)))

  (stop [this]
    (println "Stopping Server")
    (http/stop @(:server this))
    (dissoc this :server))

  ServerProvider
  (test-request [this verb url]
    (let [server (:server this)]
      (test/response-for (::http/service-fn @server) verb url)))
  (test-request-with-body [this verb url body]
    (let [server (:server this)]
      (test/response-for (::http/service-fn @server), verb, url,
                         :headers {"Content-Type" "application/json"},
                         :body (json/encode body)))))

(defn new-server []
  (map->Server {}))


;(defn stop-server []
;  (http/stop @server))
;
;(defn restart-server []
;  (stop-server)
;  (start-server))
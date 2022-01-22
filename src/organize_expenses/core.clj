(ns organize-expenses.core
  (:require [com.stuartsierra.component :as component]
            [organize-expenses.components.server :as server-component]
            [organize-expenses.components.routes :as routes-component]
            [organize-expenses.components.db :as db-component]
            [clojure.edn :as edn])
  (:gen-class)
  (:import (java.util Date UUID)))

(def system-atom (atom nil))

(defn- build-system-map []
  (component/system-map
    :database (db-component/new-database)
    :routes (routes-component/new-routes)
    :server (component/using (server-component/new-server) [:database :routes])))

(defn start-system! [system-map]
  (->> system-map
       component/start
       (reset! system-atom)))

(defn stop-system! []
  (swap!
    system-atom
    (fn [s] (when s (component/stop s)))))

(defn -main
  "The entry-point for 'gen-class'"
  [& _args]
  (start-system! (build-system-map)))


;; Below some data to test using REPL (dev)
;(def uuid (UUID/randomUUID))
;(def finance-record-income
;  {:id          (UUID/randomUUID)
;   :description "Salary"
;   :value       9822.28
;   :type        "income"
;   :created-at  (new Date)})
;(def finance-record-expense
;  {:id          (UUID/randomUUID)
;   :description "Mouse Ergo Logitech MX3"
;   :value       529.99
;   :type        "expense"
;   :created-at  (new Date)})


; Testing Post
;(server-component/test-request-with-body (:server @system-atom) :post "/finance-record" finance-record-income)
;(server-component/test-request-with-body (:server @system-atom) :post "/finance-record" finance-record-expense)
;;;;; Testing Get
;(edn/read-string
;  (:body (server-component/test-request (:server @system-atom) :get "/finance-records/income")))
;;; Testing Patch
;(server-component/test-request-with-body (:server @system-atom) :patch "/finance-record" finance-record-income)
;;; Testing Get One
;(edn/read-string
;  (:body (server-component/test-request (:server component-result) :get (str "/finance-record/" uuid))))
;;; Testing Delete
;(server-component/test-request (:server component-result) :delete (str "/finance-record/"uuid))

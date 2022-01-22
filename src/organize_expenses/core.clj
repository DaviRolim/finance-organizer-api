(ns organize-expenses.core
  (:require [com.stuartsierra.component :as component]
            [organize-expenses.components.server :as server-component]
            [organize-expenses.components.routes :as routes-component]
            [organize-expenses.components.db :as db-component]
            [clojure.edn :as edn])
  (:gen-class)
  (:import (java.util Date UUID)))

(defn- system-map []
  (component/system-map
    :database (db-component/new-database)
    :routes (routes-component/new-routes)
    :server (component/using (server-component/new-server) [:database :routes])))

(def component-result (component/start (system-map)))

(def uuid (UUID/randomUUID))
(def income-example {:id          uuid
                     :description "Sales"
                     :value       100.00
                     :created-at  (new Date)})
(def income-example-updated {:id   uuid
                     :description "Salary"
                     :value       11090.00
                     :created-at  (new Date)})


; Testing Post
;(server-component/test-request-with-body (:server component-result) :post "/income" income-example)
;;;; Testing Get
;(edn/read-string
;  (:body (server-component/test-request (:server component-result) :get "/incomes")))
;;; Testing Patch
;(server-component/test-request-with-body (:server component-result) :patch "/income" income-example-updated)
;;; Testing Get One
;(edn/read-string
;  (:body (server-component/test-request (:server component-result) :get (str "/income/" uuid))))
;;; Testing Delete
;(server-component/test-request (:server component-result) :delete (str "/income/"uuid))

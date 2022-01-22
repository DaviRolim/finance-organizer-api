(ns organize-expenses.components.routes
  (:require [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
            [com.stuartsierra.component :as component]
            [organize-expenses.ports.http_in :as http-in]
            ))


(defrecord Routes []
  component/Lifecycle

  (start [this]
    (println "Starting Routes")
    (let [routes
          (route/expand-routes
            #{["/finance-records/:type" :get http-in/get-finance-record-history :route-name :list-finance-records]
              ["/finance-record" :post [(body-params/body-params) http-in/save-finance-record!] :route-name :save-finance-record]
              ["/finance-record" :patch [(body-params/body-params) http-in/update-finance-record!] :route-name :update-finance-record]
              ["/finance-record/:id" :get [http-in/get-finance-record-detail] :route-name :get-finance-record]
              ["/finance-record/:id" :delete [http-in/delete-finance-record-entry!] :route-name :delete-finance-record]
              }
            )]
      (assoc this :routes routes)))
  (stop [this]
    (println "Stppping Routes")
    (dissoc this :routes)))

  (defn new-routes []
    (->Routes))
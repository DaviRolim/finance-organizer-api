(ns organize-expenses.components.routes
  (:require [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
            [com.stuartsierra.component :as component]
            [organize-expenses.ports.http_in.income :as httpin.income]
            [organize-expenses.ports.http_in.expense :as httpin.expense]
            ))


(defrecord Routes []
  component/Lifecycle

  (start [this]
    (println "Starting Routes")
    (let [routes
          (route/expand-routes
            #{["/incomes" :get httpin.income/get-income-history :route-name :list-incomes]
              ["/income" :post [(body-params/body-params) httpin.income/save-income!] :route-name :save-income]
              ["/income" :patch [(body-params/body-params) httpin.income/update-income!] :route-name :update-income]
              ["/income/:id" :get [httpin.income/get-income-detail] :route-name :get-income]
              ["/income/:id" :delete [httpin.income/delete-income-entry!] :route-name :delete-income]
              ["/expenses" :get httpin.expense/get-income-history :route-name :list-incomes]
              ["/expense" :post [(body-params/body-params) httpin.expense/save-income!] :route-name :save-income]
              ["/expense" :patch [(body-params/body-params) httpin.expense/update-income!] :route-name :update-income]
              ["/expense/:id" :get [httpin.expense/get-income-detail] :route-name :get-income]
              ["/expense/:id" :delete [httpin.expense/delete-income-entry!] :route-name :delete-income]
              }
            )]
      (assoc this :routes routes) ))
  (stop [this]
    (println "Stppping Routes")
    (dissoc this :routes)))

(defn new-routes []
  (->Routes))
(ns organize-expenses.components.routes
  (:require [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
            [com.stuartsierra.component :as component]
            [organize-expenses.ports.http_in :as http-in]
            [clojure.data.json :as json]))
;; TODO Put this on my ports ns and import on the server component (route
(def coerce-body
  {:name ::coerce-body
   :leave
   (fn [context]
     (let [accepted (get-in context [:request :accept :field] "application/json")
           response (get context :response)
           body (get response :body)
           coerced-body (case accepted
                          "text/html" body
                          "text/plain" body
                          "application/edn" (pr-str body)
                          "application/json" (json/write-str body))
           updated-response (assoc response
                              :headers {"Content-Type" accepted}
                              :body coerced-body)]
       (assoc context :response updated-response)))})

(defrecord Routes []
  component/Lifecycle

  (start [this]
    (println "Starting Routes")
    (let [routes
          (route/expand-routes
            #{["/finance-records/:type" :get [coerce-body http-in/get-finance-record-history] :route-name :list-finance-records]
              ["/finance-records/:type/:year/:month" :get [coerce-body http-in/get-finance-records-month] :route-name :list-finance-records-by-month]
              ["/finance-record" :post [coerce-body (body-params/body-params) http-in/save-finance-record!] :route-name :save-finance-record]
              ["/finance-record/:id" :patch [coerce-body (body-params/body-params) http-in/update-finance-record!] :route-name :update-finance-record]
              ["/finance-record/:id" :get [coerce-body http-in/get-finance-record-detail] :route-name :get-finance-record]
              ["/finance-record/:id" :delete [coerce-body http-in/delete-finance-record-entry!] :route-name :delete-finance-record]
              ["/summary/:year/:month" :get [coerce-body http-in/get-summary] :route-name :summary]
              }
            )]
      (assoc this :routes routes)))
  (stop [this]
    (println "Stppping Routes")
    (dissoc this :routes)))

  (defn new-routes []
    (->Routes))
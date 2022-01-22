(ns organize-expenses.datomic.dev-config
  (:require [datomic.client.api :as d]))
;
;(def client (d/client {:server-type :dev-local
;                       :storage-dir :mem
;                       :system "api-finance"}))
;(d/create-database client {:db-name "finance"})
;(def conn (d/connect client {:db-name "finance"}))

(defn get-client []
  (d/client {:server-type :dev-local
              :storage-dir :mem
              :system "api-finance"}))

(defn create-db [client name]
  (d/create-database client {:db-name name}))

(defn get-db [conn]
  (d/db conn))

(defn get-conn [client name]
  (d/connect client {:db-name name}))

(def income-schema
  [{:db/ident :income/id
    :db/unique :db.unique/identity
    :db/valueType :db.type/uuid
    :db/cardinality :db.cardinality/one
    :db/doc "ID of the income record"}
   {:db/ident :income/description
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "Description of the income record"}
   {:db/ident :income/value
    :db/valueType :db.type/float
    :db/cardinality :db.cardinality/one
    :db/doc "Value of the income record"}
   {:db/ident :income/month
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one
    :db/doc "MOnth of the income record"}
   {:db/ident :income/year
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one
    :db/doc "Year of the income record"}
   {:db/ident :income/created-at
    :db/valueType :db.type/instant
    :db/cardinality :db.cardinality/one
    :db/doc "timestamp of the income record"}])

(def expense-schema
  [{:db/ident :expense/id
    :db/unique :db.unique/identity
    :db/valueType :db.type/uuid
    :db/cardinality :db.cardinality/one
    :db/doc "ID of the expense record"}
   {:db/ident :expense/description
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "Description of the expense record"}
   {:db/ident :expense/value
    :db/valueType :db.type/float
    :db/cardinality :db.cardinality/one
    :db/doc "Value of the expense record"}
   {:db/ident :expense/month
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one
    :db/doc "Value of the expense record"}
   {:db/ident :expense/year
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one
    :db/doc "Value of the expens record"}
   {:db/ident :expense/created-at
    :db/valueType :db.type/instant
    :db/cardinality :db.cardinality/one
    :db/doc "timestamp of the expense record"}])

(defn start-db-schemas [conn]
  "Im using in memory db so I have to initialize the schema every time I start the application"
  (d/transact conn {:tx-data income-schema})
  (d/transact conn {:tx-data expense-schema}) )

;(d/transact conn {:tx-data income-schema})
;(d/transact conn {:tx-data expense-schema})

;(defn delete-database []
;  (d/delete-database client {:db-name "finance"}))

;(defn list-databases []
;  (d/list-databases client {}))

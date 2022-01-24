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

(def finance-record-schema
  [{:db/ident :finance-record/id
    :db/unique :db.unique/identity
    :db/valueType :db.type/uuid
    :db/cardinality :db.cardinality/one
    :db/doc "ID of the finance-record record"}
   {:db/ident :finance-record/description
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "Description of the finance-record record"}
   {:db/ident :finance-record/value
    :db/valueType :db.type/float
    :db/cardinality :db.cardinality/one
    :db/doc "Value of the finance-record record"}
   {:db/ident :finance-record/month
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one
    :db/doc "Month of the finance-record record"}
   {:db/ident :finance-record/year
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one
    :db/doc "Year of the finance-record record"}
   {:db/ident :finance-record/type
    :db/valueType :db.type/keyword
    :db/cardinality :db.cardinality/one
    :db/doc "Type (:income or :expense) of the finance-record record"}
   {:db/ident :finance-record/category
    :db/valueType :db.type/keyword
    :db/cardinality :db.cardinality/one
    :db/doc "Category (:food, :health, :home, :transport, :education, :leisure, :unforeseen, :others) of the finance-record record"}
   {:db/ident :finance-record/created-at
    :db/valueType :db.type/instant
    :db/cardinality :db.cardinality/one
    :db/doc "timestamp of the finance-record record"}])


(defn start-db-schemas [conn]
  "Im using in memory db so I have to initialize the schema every time I start the application"
  (d/transact conn {:tx-data finance-record-schema}))

;(d/transact conn {:tx-data finance-record-schema})
;(d/transact conn {:tx-data expense-schema})

;(defn delete-database []
;  (d/delete-database client {:db-name "finance"}))

;(defn list-databases []
;  (d/list-databases client {}))

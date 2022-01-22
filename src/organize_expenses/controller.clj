(ns organize-expenses.controller
  (:require [schema.core :as s]
            [organize-expenses.datomic.operations :as db.ops]
            [organize-expenses.logic :as logic]))

(defn get-all-finance-records [db finance-record-type]
  (db.ops/find-all-by-type db (keyword finance-record-type)))

(defn get-finance-record-info [db id]
  (db.ops/find-by-id! db id))

(defn add-finance-record-db! [db conn finance-record]
  (let [duplicated? (logic/duplicated-description-same-month db finance-record)]
    (when-not duplicated? (db.ops/upsert-one! conn finance-record))))

(defn remove-finance-record-db! [conn id]
  (db.ops/retract-one! conn id))

(defn update-finance-record-db! [conn finance-record]
  (db.ops/upsert-one! conn finance-record))
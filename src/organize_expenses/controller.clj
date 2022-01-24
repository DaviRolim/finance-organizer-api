(ns organize-expenses.controller
  (:require [schema.core :as s]
            [organize-expenses.datomic.operations :as db.ops]
            [organize-expenses.logic :as logic])
  (:use clojure.pprint))

(defn get-all-finance-records [db finance-record-type]
  (db.ops/find-all-by-type db (keyword finance-record-type)))

(defn get-all-finance-records-by-description [db type description]
  (db.ops/find-by-description db (keyword type) description))

(defn get-all-finance-records-type-month-year [db type month year]
  (db.ops/find-all-by-type-month-year db (keyword type) (Integer/parseInt month) (Integer/parseInt year)))

(defn get-finance-record-info [db id]
  (db.ops/find-by-id! db id))

(defn add-finance-record-db! [db conn finance-record]
  (let [duplicated? (logic/duplicated-description-same-month db finance-record)]
    (when-not duplicated? (db.ops/upsert-one! conn finance-record))))

(defn remove-finance-record-db! [conn id]
  (db.ops/retract-one! conn id))

(defn update-finance-record-db! [conn finance-record]
  (db.ops/upsert-one! conn finance-record))

(defn get-month-summary [db month year]                     ;; TODO Move this to the logic namespace
  (let [all-incomes (get-all-finance-records-type-month-year db :income month year)
        all-expenses (get-all-finance-records-type-month-year db :expense month year)
        total-income-month (apply + (map :value all-incomes))
        total-expenses-month (apply + (map :value all-expenses))
        final-balance (- total-income-month total-expenses-month)
        grouped-category (mapv (fn [[k,v]] {k (reduce + (map :value v))}) (group-by :category all-expenses))
        ] {:total-income total-income-month
           :total-expenses total-expenses-month
           :final-balance final-balance
           :grouped-category grouped-category}))
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
  (let [description (:finance-record/description finance-record)
        year (:finance-record/year finance-record)
        month (:finance-record/month finance-record)
        type (:finance-record/type finance-record)
        descriptions-this-month (db.ops/find-description-by-month-year-and-type db month year type)
        duplicated? (logic/duplicated-description-same-month? description descriptions-this-month)]
    (when-not duplicated? (db.ops/upsert-one! conn finance-record))))

(defn remove-finance-record-db! [conn id]
  (db.ops/retract-one! conn id))

(defn update-finance-record-db! [conn finance-record]
  (db.ops/upsert-one! conn finance-record))

(defn get-month-summary [db month year]
  (let [all-incomes (get-all-finance-records-type-month-year db :income month year)
        all-expenses (get-all-finance-records-type-month-year db :expense month year)]
    (logic/summary-report all-incomes all-expenses)))
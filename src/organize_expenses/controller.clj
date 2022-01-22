(ns organize-expenses.controller
  (:require [schema.core :as s]
            [organize-expenses.datomic.operations :as db.ops]
            [organize-expenses.logic :as logic]))

(defn get-all-incomes [db]
  (db.ops/find-all! db))

(defn get-income-info [db id]
  (db.ops/find-by-id! db id))

(defn add-income-db! [db conn income]
  (let [duplicated? (logic/duplicated-description-same-month db income)]
    (println "controller" duplicated?)
    (when-not duplicated? (db.ops/upsert-one! conn income))))

(defn remove-income-db! [conn id]
  (db.ops/retract-one! conn id))

(defn update-income-db! [conn income]
  (db.ops/upsert-one! conn income))
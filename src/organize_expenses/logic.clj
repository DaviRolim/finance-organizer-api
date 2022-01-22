(ns organize-expenses.logic
  (:require [organize-expenses.datomic.operations :as db.ops]))


(defn duplicated-description-same-month [db entry]
  (let [description (:finance-record/description entry)
        year (:finance-record/year entry)
        month (:finance-record/month entry)
        type (:finance-record/type entry)
        descriptions-this-month (db.ops/find-description-by-month-year-and-type db month year type)]
    (println descriptions-this-month)
    (some #{description} (mapv :description descriptions-this-month))))

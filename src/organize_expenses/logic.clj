(ns organize-expenses.logic
  (:require [organize-expenses.datomic.operations :as db.ops]))


(defn duplicated-description-same-month [db entry]
  (let [description (:income/description entry)
        year (:income/year entry)
        month (:income/month entry)
        descriptions-this-month (db.ops/find-description-by-year-and-month db year month)]
    (some #{description} (mapv :description descriptions-this-month))))

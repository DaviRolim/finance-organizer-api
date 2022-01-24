(ns organize-expenses.logic
  (:require [organize-expenses.datomic.operations :as db.ops]))


(defn duplicated-description-same-month? [description descriptions-this-month]
  (some #{description} (mapv :description descriptions-this-month)))

(defn summary-report [all-incomes all-expenses]
  (let [total-income-month (apply + (map :value all-incomes))
        total-expenses-month (apply + (map :value all-expenses))
        final-balance (- total-income-month total-expenses-month)
        grouped-category (mapv (fn [[k,v]] {k (reduce + (map :value v))}) (group-by :category all-expenses))
        ] {:total-income total-income-month
           :total-expenses total-expenses-month
           :final-balance final-balance
           :grouped-category grouped-category}))
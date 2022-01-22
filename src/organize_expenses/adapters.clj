(ns organize-expenses.adapters
  (:require [schema.core :as s]
            [organize-expenses.schemas.db :as s.db]
            [clj-time.core :as t]
            [clj-time.coerce :as c]
            [organize-expenses.schemas.income :as s.income]
            [organize-expenses.schemas.expense :as s.expense]
            )
  (:use clojure.instant)
  (:import (java.util UUID)))



(s/defn wire-income->db :- s.db/income-db
  [income :- s.income/income-in]
  (let [{:keys [id description value created-at]} income
        created-at->date (read-instant-date created-at)
        income-month (t/month (c/from-date created-at->date))
        income-year (t/year (c/from-date created-at->date))]
    {:income/id          (UUID/fromString id)
     :income/description description
     :income/value       value
     :income/month       income-month
     :income/year        income-year
     :income/created-at  created-at->date})
  )

(s/defn wire-expense->db :- s.db/expense-db
  [expense :- s.expense/expense-in]
  (let [{:keys [id description value created-at]} expense
        created-at->date (read-instant-date created-at)
        income-month (t/month (c/from-date created-at->date))
        income-year (t/year (c/from-date created-at->date))]
    {:expense/id          (UUID/fromString id)
     :expense/description description
     :expense/value       value
     :expense/month       income-month
     :expense/year        income-year
     :expense/created-at  created-at->date})
  )

(defn db->wire-income [income-db]
  "It receives a data structure with Income-db schema and returns an Income"
  (+ 1 1))

(defn wire-expense->db [] )

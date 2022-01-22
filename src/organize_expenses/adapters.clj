(ns organize-expenses.adapters
  (:require [schema.core :as s]
            [organize-expenses.schemas.db :as s.db]
            [clj-time.core :as t]
            [clj-time.coerce :as c]
            [organize-expenses.schemas.finance-record :as s.finance-record]
            )
  (:use clojure.instant)
  (:import (java.util UUID)))



(s/defn wire-finance-record->db :- s.db/finance-record-db
  [finance-record :- s.finance-record/finance-record-in]
  (let [{:keys [id description value type created-at]} finance-record
        created-at->date (read-instant-date created-at)
        finance-record-month (t/month (c/from-date created-at->date))
        finance-record-year (t/year (c/from-date created-at->date))]
    {:finance-record/id          (UUID/fromString id)
     :finance-record/description description
     :finance-record/value       value
     :finance-record/month       finance-record-month
     :finance-record/year        finance-record-year
     :finance-record/type        (keyword type)
     :finance-record/created-at  created-at->date})
  )

(defn db->wire-finance-record [finance-record-db]
  "It receives a data structure with finance-record-db schema and returns an finance-record"
  (+ 1 1))


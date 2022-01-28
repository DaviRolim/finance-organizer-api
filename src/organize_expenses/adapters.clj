(ns organize-expenses.adapters
  (:require [schema.core :as s]
            [organize-expenses.schemas.db :as s.db]
            [organize-expenses.schemas.finance-record :as schemas]
            [clj-time.core :as t]
            [clj-time.coerce :as c])
  (:use clojure.instant)
  (:import (java.util UUID Date)))

(s/defn wire-in->internal :- schemas/internal
  [finance-record :- schemas/wire-in]
  (let [{:keys [description value type category]} finance-record
        id (UUID/randomUUID)
        created-at (Date.)
        finance-record-month (t/month (c/from-date created-at))
        finance-record-year (t/year (c/from-date created-at))
        category-or-default (if category (keyword category) :other)]
    #:finance-record{:id id
     :description description
     :value       value
     :month       finance-record-month
     :year        finance-record-year
     :type        (keyword type)
     :category    category-or-default
     :created-at  created-at}))

(s/defn internal->db :- s.db/finance-record-db
  [internal :- schemas/internal]
  internal)  ; Returning internal here because internal and db have the same data structure for now.

(s/defn db->internal :- schemas/internal
  [finance-record-db :- s.db/finance-record-db]
  "It receives a data structure with finance-record-db schema and returns an finance-record"
  finance-record-db)  ; Returning db here because db and internal have the same data structure for now.

(s/defn internal->wire :- schemas/wire
  [finance-record :- schemas/internal]
  (let [{:keys [id description type category value]} finance-record]
    {:id id
     :description description
     :value value
     :type type
     :category category})
  )
(ns organize-expenses.schemas.db
  (:require [schema.core :as s]))

(def finance-record-db
  {:finance-record/id           s/Uuid
   :finance-record/description  s/Str
   :finance-record/value        s/Num
   :finance-record/month        s/Num
   :finance-record/year         s/Num
   :finance-record/created-at   s/Inst})




(def expense-db
  {:expense/id          s/Uuid
   :expense/description s/Str
   :expense/value       s/Num
   :expense/month       s/Num
   :expense/year        s/Num
   :expense/created-at  s/Inst})
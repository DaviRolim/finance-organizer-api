(ns organize-expenses.schemas.db
  (:require [schema.core :as s]))

(def income-db
  {:income/id           s/Uuid
   :income/description  s/Str
   :income/value        s/Num
   :income/month        s/Num
   :income/year         s/Num
   :income/created-at   s/Inst})


(def expense-db
  {:expense/id          s/Uuid
   :expense/description s/Str
   :expense/value       s/Num
   :expense/month       s/Num
   :expense/year        s/Num
   :expense/created-at  s/Inst})
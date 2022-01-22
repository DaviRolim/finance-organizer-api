(ns organize-expenses.schemas.income
  (:require [schema.core :as s]))


(def income-in
  {:id s/Str
   :description s/Str
   :value s/Num
   :created-at s/Inst})
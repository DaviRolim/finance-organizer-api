(ns organize-expenses.schemas.expense
  (:require [schema.core :as s]))

;id description value created-at

(def expense-in
  {:id s/Uuid
   :description s/Str
   :value s/Num
   :created-at s/Inst})

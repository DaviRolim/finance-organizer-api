(ns organize-expenses.schemas.finance-record
  (:require [schema.core :as s]))

;id description value created-at

(def finance-record-in
  {:id s/Str
   :description s/Str
   :value s/Num
   :type s/Str
   :created-at s/Inst})
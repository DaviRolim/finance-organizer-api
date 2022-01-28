(ns organize-expenses.schemas.finance-record
  (:require [schema.core :as s]
            [organize-expenses.schemas.aux :as aux]))

(s/def PosNum (s/constrained s/Num #(>= % 0) 'bigger-than-zero))

(def wire-in
  {:description s/Str
   :value       PosNum
   :type        s/Str
   :category    (s/optional-key s/Str)
   })

(def wire
  {:id s/Str
   :description s/Str
   :value PosNum
   :type s/Str
   :category    (s/optional-key s/Str)
   }) ; TODO create conditional schema instead of optional key


(def internal
  #:finance-record{:id          s/Uuid
                   :description s/Str
                   :value       s/Num
                   :type        (s/enum :income :expense)
                   :month       s/Num
                   :year        s/Num
                   :category    (s/enum aux/categories)
                   :created-at  s/Inst})
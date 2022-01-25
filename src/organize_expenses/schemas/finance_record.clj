(ns organize-expenses.schemas.finance-record
  (:require [schema.core :as s]))

;id description value created-at

(s/def PosNum (s/constrained s/Num #(>= % 0) 'bigger-than-zero))

(def financial-record
  {:id s/Uuid
   :value PosNum
   :description s/Str
   :type (s/enum :income :expense)
   :category (s/enum :other :food :transport :tech :house)
   })


(def finance-record-in
  {:id s/Str
   :description s/Str
   :value s/Num
   :type s/Str
   :category s/Str
   })
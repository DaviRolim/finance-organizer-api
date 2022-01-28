(ns organize-expenses.schemas.db
  (:require [schema.core :as s]
            [organize-expenses.schemas.aux :as aux]))

(def finance-record-db
  #:finance-record{:id           s/Uuid
   :description  s/Str
   :value        s/Num
   :type        (s/enum :income :expense)
   :month        s/Num
   :year         s/Num
   :category     (s/enum aux/categories)
   :created-at   s/Inst})

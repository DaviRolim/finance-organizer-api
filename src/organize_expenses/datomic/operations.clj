(ns organize-expenses.datomic.operations
  (:require [datomic.client.api :as d])
  (:use clojure.pprint))


(defn upsert-one!
  "Upsert or insert one record using map"
  [conn income]
  (d/transact conn {:tx-data [income]}))
;(defn upsert-one!
;  "Upsert or insert one record using map"
;  [conn {:keys [id description value created-at]}]
;  (println id description value created-at)
;  (d/transact conn {:tx-data [{:income/id           id
;                               :income/description  description
;                               :income/value        value
;                               :income/created-at   created-at}]}))

(defn upsert-many!
  "Update or insert receives a connection and array of arrays"
  [conn incomes]
  (d/transact conn {:tx-data incomes}))

(defn retract-one!
  "Retract all the fields based on the :income/id"
  [conn id]
  (try
    (d/transact conn {:tx-data
                      [[:db/retract [:income/id id] :income/id]
                       [:db/retract [:income/id id] :income/description]
                       [:db/retract [:income/id id] :income/value]
                       [:db/retract [:income/id id] :income/created-at]]})
    (catch Exception e {})))

(defn find-all!
  [db]
  (d/q '[:find ?id ?description ?value ?month ?year ?created-at
         :keys id description value month year created-at
         :where
         [?e :income/id ?id]
         [?e :income/description ?description]
         [?e :income/value ?value]
         [?e :income/month ?month]
         [?e :income/year ?year]
         [?e :income/created-at ?created-at]]
       db))

(defn find-description-by-year-and-month
  [db year month]
  (println year month)
  (d/q '[:find ?description
         :keys description
         :in $ ?year ?month
         :where
         [?e :income/year ?year]
         [?e :income/month ?month]
         [?e :income/description ?description]
         ]
       db year month))

(defn find-by-id!
  "Will return only entities that have all the fields specified in the where
  That is, if they don't have :income/description they will be ignored"
  [db id]
  (-> (d/q '[:find ?id ?description ?value ?created-at
             :keys id description value created-at
             :in $ ?id
             :where
             [?e :income/id ?id]
             [?e :income/description ?description]
             [?e :income/value ?value]
             [?e :income/created-at ?created-at]]
           db id)
      first))

(defn pull-by-id!
  "Will return all the entity that has :income/id even if they don't have :income/description"
  [db id]
  (-> (d/q '[:find (pull ?e [*])
             :in $ ?id
             :where
             [?e :income/id ?id]]
           db id)
      first first))
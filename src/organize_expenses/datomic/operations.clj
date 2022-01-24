(ns organize-expenses.datomic.operations
  (:require [datomic.client.api :as d])
  (:use clojure.pprint))


(defn upsert-one!
  "Upsert or insert one record using map"
  [conn finance-record]
  (d/transact conn {:tx-data [finance-record]}))
;(defn upsert-one!
;  "Upsert or insert one record using map"
;  [conn {:keys [id description value created-at]}]
;  (println id description value created-at)
;  (d/transact conn {:tx-data [{:finance-record/id           id
;                               :finance-record/description  description
;                               :finance-record/value        value
;                               :finance-record/created-at   created-at}]}))

(defn upsert-many!
  "Update or insert receives a connection and array of arrays"
  [conn finance-records]
  (d/transact conn {:tx-data finance-records}))

(defn retract-one!
  "Retract all the fields based on the :finance-record/id"
  [conn id]
  (try
    (d/transact conn {:tx-data
                      [[:db/retract [:finance-record/id id] :finance-record/id]
                       [:db/retract [:finance-record/id id] :finance-record/description]
                       [:db/retract [:finance-record/id id] :finance-record/value]
                       [:db/retract [:finance-record/id id] :finance-record/month]
                       [:db/retract [:finance-record/id id] :finance-record/year]
                       [:db/retract [:finance-record/id id] :finance-record/type]
                       [:db/retract [:finance-record/id id] :finance-record/created-at]]})
    (catch Exception e {})))

(defn find-all!
  [db]
  (d/q '[:find ?id ?description ?value ?month ?year ?type ?created-at
         :keys id description value month year ?type created-at
         :where
         [?e :finance-record/id ?id]
         [?e :finance-record/description ?description]
         [?e :finance-record/value ?value]
         [?e :finance-record/month ?month]
         [?e :finance-record/year ?year]
         [?e :finance-record/type ?type]
         [?e :finance-record/created-at ?created-at]]
       db))

(defn find-all-by-type
  [db type]
  (d/q '[:find ?id ?description ?value ?month ?year ?type ?category ?created-at
         :keys id description value month year type category created-at
         :in $ ?type
         :where
         [?e :finance-record/type ?type]
         [?e :finance-record/id ?id]
         [?e :finance-record/description ?description]
         [?e :finance-record/value ?value]
         [?e :finance-record/month ?month]
         [?e :finance-record/year ?year]
         [?e :finance-record/category ?category]
         [?e :finance-record/created-at ?created-at]]
       db type))

(defn find-all-by-type-month-year
  [db type month year]
  (d/q '[:find ?id ?description ?value ?month ?year ?type ?category ?created-at
         :keys id description value month year type category created-at
         :in $ ?month ?year ?type
         :where
         [?e :finance-record/type ?type]
         [?e :finance-record/id ?id]
         [?e :finance-record/description ?description]
         [?e :finance-record/value ?value]
         [?e :finance-record/category ?category]
         [?e :finance-record/month ?month]
         [?e :finance-record/year ?year]
         [?e :finance-record/created-at ?created-at]]
       db month year type))

(defn find-description-by-month-year-and-type
  [db month year type]
  (d/q '[:find ?description
         :keys description
         :in $ ?year ?month ?type
         :where
         [?e :finance-record/year ?year]
         [?e :finance-record/month ?month]
         [?e :finance-record/type ?type]
         [?e :finance-record/description ?description]]
       db year month type))

(defn find-by-description
  [db type search]
  (d/q '[:find ?id ?description ?value ?month ?year ?type ?category ?created-at
         :keys id description value month year type category created-at
         :in $ ?type ?search
         :where [?e :finance-record/description ?description]
         [(.contains ^String ?description ?search)]
         [?e :finance-record/type ?type]
         [?e :finance-record/id ?id]
         [?e :finance-record/value ?value]
         [?e :finance-record/month ?month]
         [?e :finance-record/year ?year]
         [?e :finance-record/category ?category]
         [?e :finance-record/created-at ?created-at]
         ]
       db type search))

(defn find-by-id!
  "Will return only entities that have all the fields specified in the where
  That is, if they don't have :finance-record/description they will be ignored"
  [db id]
  (-> (d/q '[:find ?id ?description ?value ?created-at
             :keys id description value created-at
             :in $ ?id
             :where
             [?e :finance-record/id ?id]
             [?e :finance-record/description ?description]
             [?e :finance-record/value ?value]
             [?e :finance-record/created-at ?created-at]]
           db id)
      first))

(defn pull-by-id!
  "Will return all the entity that has :finance-record/id even if they don't have :finance-record/description"
  [db id]
  (-> (d/q '[:find (pull ?e [*])
             :in $ ?id
             :where
             [?e :finance-record/id ?id]]
           db id)
      first first))
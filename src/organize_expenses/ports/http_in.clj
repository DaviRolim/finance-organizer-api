(ns organize-expenses.ports.http_in
  (:require [organize-expenses.adapters :as adapters]
            [organize-expenses.controller :as controller]
            [clojure.data.json :as json])
  (:use clojure.pprint)
  (:import (java.util UUID)))

(defn get-finance-record-history [request]
  (let [db (:db request)
        type (get-in request [:path-params :type])
        description (get-in request [:query-params :description])
        finance-records (if-not description
                          (controller/get-all-finance-records db type)
                          (controller/get-all-finance-records-by-description db type description))
        result (map adapters/db->wire-finance-record finance-records)]     ;(map adapters/db->wire-finance-record finance-records)
    {:status 200 :body result :headers {"Content-Type" "application/json"}}))

(defn get-finance-records-month [request]
  (let [db (:db request)
        type (get-in request [:path-params :type])
        month (get-in request [:path-params :month])
        year (get-in request [:path-params :year])
        finance-records (controller/get-all-finance-records-type-month-year db type month year)
        result (map adapters/db->wire-finance-record finance-records)]     ;(map adapters/db->wire-finance-record finance-records)
    {:status 200 :body result :headers {"Content-Type" "application/json"}}))


(defn get-finance-record-detail [request]
  (let [db (:db request)
        finance-record-id (get-in request [:path-params :id])
        finance-record-uuid (UUID/fromString finance-record-id)
        result (controller/get-finance-record-info db finance-record-uuid)]
    {:status 200 :body result}))

(defn save-finance-record! [request]
  (let [finance-record (assoc (:json-params request) :id (UUID/randomUUID))
        conn (:conn request)
        db (:db request)]
    (->> finance-record
         (adapters/wire-finance-record->db)
         (controller/add-finance-record-db! db conn))
    {:status 201 :body {:payload finance-record, :message "Entry saved"}}))


(defn delete-finance-record-entry! [request]
  (let [finance-record-id (get-in request [:path-params :id])
        finance-record-uuid (UUID/fromString finance-record-id)
        conn (:conn request)]
    (controller/remove-finance-record-db! conn finance-record-uuid)
    {:status 200 :body {:message "Entry deleted"}}))

(defn update-finance-record! [request]
  (let [body-params (:json-params request)
        id (get-in request [:path-params :id])
        finance-record (assoc body-params :id (UUID/fromString id))
        conn (:conn request)]
    (->> finance-record
         (adapters/wire-finance-record->db)
         (controller/update-finance-record-db! conn))
    {:status 201 :body {:payload finance-record, :message "Entry updated"}}))

(defn get-summary [request]
  (let [db (:db request)
        month (get-in request [:path-params :month])
        year (get-in request [:path-params :year])
        finance-records (controller/get-month-summary db month year)
        ]     ;(map adapters/db->wire-finance-record finance-records)
    {:status 200 :body finance-records :headers {"Content-Type" "application/json"}}))


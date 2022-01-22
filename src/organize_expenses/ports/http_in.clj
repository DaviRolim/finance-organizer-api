(ns organize-expenses.ports.http_in
  (:require [organize-expenses.adapters :as adapters]
            [organize-expenses.controller :as controller])
  (:use clojure.pprint)
  (:import (java.util UUID)))

(defn get-finance-record-history [request]
  (let [db (:db request)
        type (get-in request [:path-params :type])
        finance-records (controller/get-all-finance-records db type)
        result finance-records]     ;(map adapters/db->wire-finance-record finance-records)
    {:status 200 :body result}))

(defn get-finance-record-detail [request]
  (let [db (:db request)
        finance-record-id (get-in request [:path-params :id])
        finance-record-uuid (UUID/fromString finance-record-id)
        result (controller/get-finance-record-info db finance-record-uuid)]
    {:status 200 :body result}))

(defn save-finance-record! [request]
  (let [finance-record (:json-params request)
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
  (let [finance-record (:json-params request)
        conn (:conn request)]
    (->> finance-record
         (adapters/wire-finance-record->db)
         (controller/update-finance-record-db! conn))
    {:status 201 :body {:payload finance-record, :message "Entry updated"}}))





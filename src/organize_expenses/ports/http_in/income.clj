(ns organize-expenses.ports.http_in.income
  (:require [organize-expenses.adapters :as adapters]
            [organize-expenses.controller :as controller])
  (:use clojure.pprint)
  (:import (java.util UUID)))

(defn get-income-history
  [{:keys [db]}]
  (let [incomes (controller/get-all-incomes db)
        result incomes]     ;(map adapters/db->wire-income incomes)
    {:status 200 :body result}))

(defn get-income-detail [request]
  (let [db (:db request)
        income-id (get-in request [:path-params :id])
        income-uuid (UUID/fromString income-id)
        result (controller/get-income-info db income-uuid)]
    {:status 200 :body result}))

(defn save-income! [request]
  (let [income (:json-params request)
        conn (:conn request)
        db (:db request)]
    (->> income
         (adapters/wire-income->db)
         (controller/add-income-db! db conn))
    {:status 201 :body {:payload income, :message "Entry saved"}}))


(defn delete-income-entry! [request]
  (let [income-id (get-in request [:path-params :id])
        income-uuid (UUID/fromString income-id)
        conn (:conn request)]
    (controller/remove-income-db! conn income-uuid)
    {:status 200 :body {:message "Entry deleted"}}))

(defn update-income! [request]
  (let [income (:json-params request)
        conn (:conn request)]
    (->> income
         (adapters/wire-income->db)
         (controller/update-income-db! conn))
    {:status 201 :body {:payload income, :message "Entry updated"}}))





(ns organize-expenses.ports.http_in.expense
  (:require [organize-expenses.adapters :as adapters]
            [organize-expenses.controller :as controller])
  (:use clojure.pprint)
  (:import (java.util UUID)))

(defn get-expense-history
  [{:keys [db]}]
  (let [expenses (controller/get-all-expenses db)
        result expenses]     ;(map adapters/db->wire-expense expenses)
    {:status 200 :body result}))

(defn get-expense-detail [request]
  (let [db (:db request)
        expense-id (get-in request [:path-params :id])
        expense-uuid (UUID/fromString expense-id)
        result (controller/get-expense-info db expense-uuid)]
    {:status 200 :body result}))

(defn save-expense! [request]
  (let [expense (:json-params request)
        conn (:conn request)
        db (:db request)]
    (->> expense
         (adapters/wire-expense->db)
         (controller/add-expense-db! db conn))
    {:status 201 :body {:payload expense, :message "Entry saved"}}))


(defn delete-expense-entry! [request]
  (let [expense-id (get-in request [:path-params :id])
        expense-uuid (UUID/fromString expense-id)
        conn (:conn request)]
    (controller/remove-expense-db! conn expense-uuid)
    {:status 200 :body {:message "Entry deleted"}}))

(defn update-expense! [request]
  (let [expense (:json-params request)
        conn (:conn request)]
    (->> expense
         (adapters/wire-expense->db)
         (controller/update-expense-db! conn))
    {:status 201 :body {:payload expense, :message "Entry updated"}}))





(ns organize-expenses.components.db
  (:require  [com.stuartsierra.component :as component]
             [organize-expenses.datomic.dev-config :as db.config]))

(defrecord Database []
  component/Lifecycle
  (start [this]
    (let [client (db.config/get-client)
          created? (db.config/create-db client "finance")
          conn (db.config/get-conn client "finance")]
      (println "Starting Database")
      (db.config/start-db-schemas conn)
      (assoc this :conn conn)))

  (stop [this]
    (println "Stppping Database")
    (dissoc this :conn)))

(defn new-database []
  (->Database))
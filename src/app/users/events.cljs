(ns app.users.events
  (:require [re-frame.core :refer [reg-event-db]]
            [app.firebase.db :as db]))

(reg-event-db
  :save-users
  (fn [db [_ {:keys [data]}]]
    (assoc db :users (->> data
                         (db/docs->clj)
                         (map (juxt
                                (comp keyword :uid)
                                #(assoc (:profile %) :uid (keyword (:uid %)))))
                         (into {})))))

(reg-event-db
  :save-permissions
  (fn [db [_ {:keys [data]}]]
    (assoc db :permissions (->> data
                                (db/docs->clj)
                                (map (juxt (comp keyword :id) (comp keyword :role)))
                                (into {})))))

(ns app.invitations.events
  (:require [re-frame.core :refer [reg-event-db]]
            [app.firebase.db :as db]
            [app.helpers :as h]))

(defn docs->invitations
  "Extract invitations from provided snapshot"
  [snapshot]
  (->> snapshot
       (db/docs->clj)
       (h/index-by :id)))

(reg-event-db
  :save-invitations
  (fn [db [_ {:keys [data]}]]
    (assoc db :invitations (docs->invitations data))))

(ns app.dicks.events
  (:require [re-frame.core :refer [reg-event-db reg-event-fx reg-fx]]
            [app.firebase.db :as db]))

(defn docs->dicks
  [snapshot]
  (->> snapshot
       (db/docs->clj)
       (map (juxt
              :id
              #(update % :team-id keyword)))
       (into {})))

(reg-event-db
  :save-dicks
  (fn [db [_ {:keys [data]}]]
    (assoc db :dicks (docs->dicks data))))

(reg-event-db
  :give-dick
  (fn [db [_ uid]]
    (let [receiver (get-in db [:users uid])]
      (-> db
          (assoc-in [:nav :active-modal] :dick-creator)
          (assoc :dicked-user receiver)))))

(reg-fx
  :save-dick
  (fn [{:keys [sender receiver desc team-id]}]
    (db/save! (db/ref (db/collection :dicks)) {:sender sender
                                               :receiver receiver
                                               :desc desc
                                               :team-id team-id
                                               :created-at (db/server-timestamp)})))

(reg-event-fx
  :save-dick
  (fn [{:keys [db]} [_ {:keys [desc]}]]
    (let [sender (get-in db [:auth :profile])
          receiver (get db :dicked-user)
          team-id (get-in db [:nav :active-team])]
      {:save-dick {:sender sender
                   :receiver receiver
                   :desc desc
                   :team-id team-id}})))

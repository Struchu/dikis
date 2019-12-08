(ns app.users.events
  (:require [re-frame.core :refer [reg-event-db reg-event-fx reg-fx]]
            [app.firebase.db :as db]))

(defn docs->users
  "Extracts user data from provided snapshot"
  [snapshot]
  (->> snapshot
    (db/docs->clj)
    (map (juxt
           (comp keyword :uid)
           #(assoc (:profile %) :uid (keyword (:uid %)))))
    (into {})))

(defn docs->permissions
  [snapshot]
  (->> snapshot
    (db/docs->clj)
    (map (juxt (comp keyword :id) (comp keyword :role)))
    (into {})))

(reg-event-db
  :save-users
  (fn [db [_ {:keys [data]}]]
    (assoc db :users (docs->users data))))

(reg-event-db
  :save-permissions
  (fn [db [_ {:keys [data]}]]
    (assoc db :permissions (docs->permissions data))))

(reg-fx
  :invite-user
  (fn [{:keys [email team-id name picture-url]}]
    (let [invitation-ref (db/ref (db/collection :invitations))]
      (db/save! invitation-ref {:email email
                                :team {:team-id team-id
                                       :name name
                                       :picture-url picture-url}}))))

(reg-event-fx
  :invite-user
  (fn [{:keys [db]} [_ {:keys [email]}]]
    (let [active-team (get-in db [:nav :active-team])
          {:keys [team-id name picture-url]} (get-in db [:teams active-team])]
      {:invite-user {:team-id team-id
                     :name name
                     :picture-url picture-url
                     :email email}})))

(ns app.teams.events
  (:require [re-frame.core :refer [->interceptor reg-cofx reg-fx inject-cofx
                                   reg-event-db reg-event-fx]]
            [app.firebase.db :as db]
            [app.helpers :as h]))

(reg-cofx
  :team-id
  (fn [cofx _]
    (assoc cofx :team-id (str (random-uuid)))))

(reg-fx
  :create-team
  (fn [{:keys [uid team-id name picture-url profile]}]
    (let [user-team-collection (db/collection :user-team)
          user-team-ref (db/ref user-team-collection (db/user-team-id uid team-id))]
      (db/save! user-team-ref {:uid uid
                               :team-id team-id
                               :name name
                               :picture-url picture-url
                               :role :admin
                               :profile profile
                               :invitation :accepted}))))

(reg-event-fx
  :create-team
  (inject-cofx :team-id)
  (fn [{:keys [db team-id]} [_ {:keys [name picture-url]}]]
    (let [uid (get-in db [:auth :uid])
          {:keys [photo-url display-name]} (get-in db [:auth :profile])]
      {:create-team {:uid uid
                     :team-id team-id
                     :name name
                     :profile {:photo-url photo-url
                               :display-name display-name}
                     :picture-url picture-url}})))

(defn keywordize-teams
  [teams]
  (let [teams-with-keywords (map #(-> %
                                      (update :role keyword)
                                      (update :invitation keyword)) teams)]
    (h/index-by :team-id teams-with-keywords)))

(reg-event-db
  :save-teams
  (fn [db [_ {:keys [data]}]]
    (assoc db :teams (keywordize-teams data))))

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
    (let [team-role-ref (db/ref (db/collection :team-role) team-id)
          team-member-ref (db/ref (db/collection team-role-ref :members) uid)]
      (db/batch!
        #(db/save! % (db/ref (db/collection :user-team)) {:uid uid
                                                          :team-id team-id
                                                          :name name
                                                          :picture-url picture-url
                                                          :profile profile})
        #(db/save! % team-role-ref {:creator uid})
        #(db/save! % team-member-ref {:role :admin})))))

(reg-event-fx
  :create-team
  (inject-cofx :team-id)
  (fn [{:keys [db team-id]} [_ {:keys [name picture-url]}]]
    (let [uid (get-in db [:auth :uid])
          {:keys [photo-url display-name email]} (get-in db [:auth :profile])]
      {:create-team {:uid uid
                     :team-id team-id
                     :name name
                     :profile {:email email
                               :photo-url photo-url
                               :display-name display-name}
                     :picture-url picture-url}})))

(reg-event-db
  :save-teams
  (fn [db [_ {:keys [data]}]]
    (assoc db :teams (->> data
                         (db/docs)
                         (map db/doc->clj)
                         (h/index-by :team-id)))))

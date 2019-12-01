(ns app.teams.events
  (:require [re-frame.core :refer [->interceptor reg-cofx inject-cofx
                                   reg-event-db reg-event-fx]]
            [app.helpers :as h]))

(reg-cofx
  :team-id
  (fn [cofx _]
    (assoc cofx :team-id (str (random-uuid)))))

(reg-event-fx
  :create-team
  (inject-cofx :team-id)
  (fn [{:keys [db team-id]} [_ {:keys [name picture-url]}]]
    (let [uid (get-in db [:auth :uid])]
      {:firebase/create-team {:uid uid
                              :team-id team-id
                              :name name
                              :picture-url picture-url}})))

(def convert-teams
  (->interceptor
    :id :convert-teams
    :before (fn [context]
              (update-in context [:coeffects :event] (fn [[id {:keys [data]}]]
                                                       [id {:data (h/index-by :team-id data)}])))))

(reg-event-db
  :save-teams
  [convert-teams]
  (fn [db [_ {:keys [data]}]]
    (assoc db :teams data)))

(ns app.auth.events
  (:require [re-frame.core :refer [reg-event-fx]]
            [app.firebase.db :as db]
            [app.router :as router]))

(reg-event-fx
  :set-user
  (fn [{:keys [db]} [_ {:keys [uid display-name photo-url email]}]]
    (let [profile {:uid uid
                :display-name display-name
                :photo-url photo-url
                :email email}
          user-team-query (-> (db/collection :user-team)
                              (db/where :uid "==" uid)
                              (db/where :invitation "==" :accepted))]
      {:db (-> db
               (assoc-in [:auth :uid] uid)
               (assoc-in [:auth :profile] profile))
       :navigate-to {:path (router/path-for :teams)}
       :firebase/save-user profile
       :firebase/subscribe-to {:query user-team-query
                               :event :save-teams
                               :key :user-team}})))

(reg-event-fx
  :clear-user
  (fn [{:keys [db]} _]
    {:db (-> db
             (assoc-in [:auth :uid] nil)
             (assoc-in [:auth :profile] nil))
     :navigate-to {:path (router/path-for :index)}}))

(reg-event-fx
  :sign-in-with-google
  (fn [_ _]
    {:firebase/sign-in-with-google nil}))

(reg-event-fx
  :sign-out
  (fn [_ _]
    {:firebase/sign-out nil}))

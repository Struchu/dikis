(ns app.auth.events
  (:require [re-frame.core :refer [reg-event-fx reg-fx path]]
            [app.firebase.db :as db]
            [app.router :as router]))

(def auth-interceptors [(path :auth)])

(reg-fx
  :save-user
  (fn [{:keys [uid display-name photo-url email]}]
    (let [users-collection (db/collection :users)
          user-ref (db/ref users-collection uid)]
      (db/save! user-ref {:uid uid
                          :display-name display-name
                          :photo-url photo-url
                          :email email}))))

(reg-event-fx
  :set-user
  auth-interceptors
  (fn [{auth :db} [_ {:keys [uid display-name photo-url email]}]]
    (let [profile {:uid uid
                   :display-name display-name
                   :photo-url photo-url
                   :email email}]
      {:db (-> auth
               (assoc :uid uid)
               (assoc :profile profile))
       :navigate-to {:path (router/path-for :teams)}
       :save-user profile
       :firebase/subscribe-to {:query [:user-team
                                       :uid "==" uid
                                       :invitation "==" :accepted]
                               :event :save-teams
                               :key :user-team}})))

(reg-event-fx
  :clear-user
  (fn [{:keys [db]} _]
    (let [subscriptions (-> db
                            (:firebase)
                            (vals))]
    {:db (-> db
             (assoc-in [:auth :uid] nil)
             (assoc-in [:auth :profile] nil)
             (assoc :firebase {})
             (assoc :teams {}))
     :firebase/clear-subscriptions {:subscriptions subscriptions}
     :navigate-to {:path (router/path-for :index)}})))

(reg-event-fx
  :sign-in-with-google
  (fn [_ _]
    {:firebase/sign-in-with-google nil}))

(reg-event-fx
  :sign-out
  (fn [_ _]
    {:firebase/sign-out nil}))

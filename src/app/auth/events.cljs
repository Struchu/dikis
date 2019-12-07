(ns app.auth.events
  (:require [re-frame.core :refer [reg-event-db reg-event-fx reg-fx path]]
            [app.firebase.db :as db]
            [app.router :as router]))

(def auth-interceptors [(path :auth)])

(reg-event-db
  :set-user
  auth-interceptors
  (fn [{auth :db} [_ {:keys [uid display-name photo-url email]}]]
    (let [profile {:uid uid
                   :display-name display-name
                   :photo-url photo-url
                   :email email}]
      (-> auth
          (assoc :uid uid)
          (assoc :profile profile)))))

(reg-event-fx
  :clear-user
  (fn [{:keys [db]} _]
    (let [subscriptions (-> db
                            (:firebase)
                            (vals))]
    {:db (-> db
             (assoc-in [:auth :uid] nil)
             (assoc-in [:auth :profile] nil)
             (assoc :teams {}))
     :app.firebase.events/observation {:action :clean}
     :navigate-to {:path (router/path-for :index)}})))

(reg-event-fx
  :sign-in-with-google
  (fn [_ _]
    {:app.firebase.events/sign-in-with-google nil}))

(reg-event-fx
  :sign-out
  (fn [_ _]
    {:app.firebase.events/sign-out nil}))

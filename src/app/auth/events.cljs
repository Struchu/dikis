(ns app.auth.events
  (:require [re-frame.core :refer [reg-event-fx]]))

(reg-event-fx
  :set-user
  (fn [{:keys [db]} [_ {:keys [uid display-name photo-url email]}]]
    {:db (-> db
             (assoc-in [:auth :uid] uid)
             (assoc-in [:auth :profile] {:uid uid
                                         :display-name display-name
                                         :photo-url photo-url
                                         :email email}))
     :navigate-to {:path "/"}}))

(reg-event-fx
  :clear-user
  (fn [{:keys [db]} _]
    {:db (-> db
             (assoc-in [:auth :uid] nil)
             (assoc-in [:auth :profile] nil))
     :navigate-to {:path "/"}}))

(reg-event-fx
  :sign-in-with-google
  (fn [_ _]
    {:firebase-sign-in-with-google nil}))

(reg-event-fx
  :sign-out
  (fn [_ _]
    {:firebase-sign-out nil}))

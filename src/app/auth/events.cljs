(ns app.auth.events
  (:require [re-frame.core :refer [after reg-event-db reg-event-fx reg-cofx reg-fx path]]
            [cljs.reader :refer [read-string]]
            [app.firebase.db :as db]
            [app.router :as router]))

(def user-key "dikis-user")

(defn set-user-ls!
  [auth]
  (when (:uid auth) (.setItem js/localStorage user-key (str auth))))

(defn remove-user-ls!
  []
  (.removeItem js/localStorage user-key))

(reg-cofx
  :local-store-user
  (fn [cofx _]
    (assoc cofx :local-store-user
           (read-string (.getItem js/localStorage user-key)))))

(def auth-interceptors [(path :auth) (after set-user-ls!)])

(def remove-user-interceptors [(after remove-user-ls!)])

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
  remove-user-interceptors
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

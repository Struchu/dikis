(ns app.firebase.events
  (:require [re-frame.core :refer [dispatch reg-fx reg-event-db reg-event-fx ]]
            [app.firebase.db :as db]
            ["firebase/app" :as firebase]))

(reg-fx
  :firebase/sign-in-with-google
  (fn [_]
    (let [provider (firebase/auth.GoogleAuthProvider.)]
      (.signInWithPopup (firebase/auth) provider))))

(reg-fx
  :firebase/sign-out
  (fn [_]
    (.signOut (firebase/auth))))

(reg-fx
  :firebase/save-user
  (fn [{:keys [uid display-name photo-url email]}]
    (let [users-collection (db/collection :users)
          user-ref (db/ref users-collection uid)]
      (db/save! user-ref {:uid uid
                          :display-name display-name
                          :photo-url photo-url
                          :email email}))))

(reg-fx
  :firebase/create-team
  (fn [{:keys [uid team-id name picture-url]}]
    (let [user-team-collection (db/collection :user-team)
          user-team-ref (db/ref user-team-collection (db/user-team-id uid team-id))]
      (db/save! user-team-ref {:uid uid
                               :team-id team-id
                               :name name
                               :picture-url picture-url
                               :role :admin
                               :invitation :accepted}))))

(defn handle-snapshot
  [event snapshot]
  (let [data (map #(js->clj (.data %) :keywordize-keys true) (.-docs snapshot))]
    (dispatch [event {:data data}])))

(reg-fx
  :firebase/subscribe-to
  (fn [{:keys [query event key]}]
    (let [subscription (.onSnapshot query (partial handle-snapshot event))]
      (dispatch [:firebase/save-subscription {:key key
                                              :subscription subscription}]))))

(reg-fx
  :firebase/clear-subscriptions
  (fn [{:keys [subscriptions]}]
    (doseq [sub subscriptions] (sub))))

(reg-event-fx
  :firebase/save-subscription
  (fn [{:keys [db]} [_ {:keys [subscription key]}]]
    (let [sub (get-in db [:firebase key])]
      {:db (assoc-in db [:firebase key] subscription)
       :firebase/clear-subscriptions (when sub [sub])})))

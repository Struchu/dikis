(ns app.firebase.events
  (:require [re-frame.core :refer [dispatch reg-fx reg-event-db reg-event-fx ]]
            [app.firebase.db :as db]
            ["firebase/app" :as firebase]))

(reg-fx
  ::sign-in-with-google
  (fn [_]
    (let [provider (firebase/auth.GoogleAuthProvider.)]
      (.signInWithPopup (firebase/auth) provider))))

(reg-fx
  ::sign-out
  (fn [_]
    (.signOut (firebase/auth))))

(def observation-fx
  (let [subscriptions (atom {})
          save-listener (fn [id subject event]
                          (swap! subscriptions assoc
                                 id (.onSnapshot subject #(dispatch [event {:data %}]))))]
      (fn handler
        [{:keys [action id subject event override] :or {override false}}]
        (condp = action
          :start (let [subscription (get @subscriptions id)]
                   (do
                     (when (and subscription override)
                       (handler {:action :stop :id id}))
                     (when (or override (not subscription))
                       (save-listener id subject event))))
          :stop (do ((get @subscriptions id))
                    (swap! subscriptions dissoc id))
          :clean (doseq [id (keys @subscriptions)]
                   (handler {:action :stop :id id}))))))

(reg-fx ::observation observation-fx)

(reg-fx
  ::observations
  (fn [observations]
    (doseq [observation observations] (observation-fx observation))))

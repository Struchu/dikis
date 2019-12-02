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

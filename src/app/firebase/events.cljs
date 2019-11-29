(ns app.firebase.events
  (:require [re-frame.core :refer [reg-fx reg-event-fx]]
            [app.firebase.db :as db]
            ["firebase/app" :as firebase]))

(reg-fx
  :firebase-sign-in-with-google
  (fn [_]
    (let [provider (firebase/auth.GoogleAuthProvider.)]
      (.signInWithPopup (firebase/auth) provider))))

(reg-fx
  :firebase-sign-out
  (fn [_]
    (.signOut (firebase/auth))))

(reg-fx
  :save-user
  (fn [{:keys [uid display-name photo-url email]}]
    (let [users-collection (db/collection :users)
          user-ref (db/ref users-collection uid)]
      (db/save! user-ref {:uid uid
                          :display-name display-name
                          :photo-url photo-url
                          :email email}))))

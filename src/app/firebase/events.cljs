(ns app.firebase.events
  (:require [re-frame.core :refer [reg-fx reg-event-fx]]
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

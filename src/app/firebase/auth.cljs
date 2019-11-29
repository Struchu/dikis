(ns app.firebase.auth
  (:require [re-frame.core :as rf]
            ["firebase/app" :as firebase]))

(defn on-auth-state-changed
  []
  (.onAuthStateChanged
    (firebase/auth)
    (fn [user]
      (if user
        (let [uid (.-uid user)
              display-name (.-displayName user)
              photo-url (.-photoURL user)
              email (.-email user)]
          (rf/dispatch [:set-user {:uid uid
                                  :display-name display-name
                                  :photo-url photo-url
                                  :email email}]))
        (rf/dispatch [:clear-user])))))

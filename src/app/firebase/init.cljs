(ns app.firebase.init
  (:require ["firebase/app" :as firebase]
            ["firebase/auth"]
            ["firebase/firestore"]))

(defn firebase-init
  []
  (if (zero? (alength firebase/apps))
    (firebase/initializeApp
      #js {:apiKey "AIzaSyBMXGvHGCKxuu7KFiOZ4XZoL7u9LQCPf-w"
           :authDomain "dikis-dc389.firebaseapp.com"
           :databaseURL "https://dikis-dc389.firebaseio.com"
           :projectId "dikis-dc389"
           :appId "1:75688135782:web:5474e76549aee0e4f8f1a7"})
    (firebase/app)))

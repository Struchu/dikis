(ns app.firebase.db
  (:require ["firebase/app" :as firebase]))

(defn collection
  ([col] (.collection (firebase/firestore) (name col)))
  ([ref sub-col] (.collection ref (name sub-col))))

(defn ref
  [collection name]
  (.doc collection name))

(defn save!
  [ref document]
  (.set ref (clj->js document)))

(defn users-collection
  []
  (collection :users))

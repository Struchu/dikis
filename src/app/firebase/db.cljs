(ns app.firebase.db
  (:require ["firebase/app" :as firebase]))

(defn collection
  "Returns collection with name derived from col-keyword"
  [col-keyword]
  (.collection (firebase/firestore) (name col-keyword)))

(defn ref
  "Gets reference to document with name inside collection col"
  [collection name]
  (.doc collection name))

(defn where
  "Adds where clause to the given query"
  [query field relation value]
  (.where query (name field) relation (if (keyword? value) (name value) value)))

(defn save!
  "Saves document doc data in document represented by reference ref"
  [ref doc]
  (.set ref (clj->js doc)))

(defn add-to!
  "Adds given document doc to collection col"
  [col doc]
  (.add col (clj->js doc)))

(defn user-team-id
  "Creates ID for user-team object by concatenating the given uid and
   team-id"
  [uid team-id]
  (str uid "_" team-id))

(ns app.firebase.db
  (:require ["firebase/app" :as firebase]))

(defn collection
  "Returns collection with name derived from col-keyword"
  ([col-keyword] (.collection (firebase/firestore) (name col-keyword)))
  ([ref col-keyword] (.collection ref (name col-keyword))))

(defn ref
  "Gets reference to document with name inside collection col"
  ([collection] (.doc collection))
  ([collection name] (.doc collection name)))

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

(defn batch
  "Starts batched write"
  []
  (.batch (firebase/firestore)))

(defn saveb!
  "Saves given doc to reference ref within batch write"
  [batch ref doc]
  (.set batch ref (clj->js doc)))

(defn deleteb!
  "Deletes given reference within batch write"
  [batch ref]
  (.delete batch ref))

(defn commit!
  "Commits given batch write"
  [batch]
  (.commit batch))

(defn docs
  "Returns documents stored in snapshot"
  [snapshot]
  (.-docs snapshot))

(defn doc->clj
  "Converts given document snapshot into Clojure structure"
  [snapshot]
  (let [data (.data snapshot)
        id (.-id snapshot)]
    (-> data
        (js->clj :keywordize-keys true)
        (assoc :id id))))

(defn docs->clj
  "Convenience function that converts given query snapshot into clojure
  collection"
  [snapshot]
  (->> snapshot (docs) (map #(doc->clj %))))

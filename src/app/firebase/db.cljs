(ns app.firebase.db
  (:require ["firebase/app" :as firebase]))

(defn collection
  "Returns collection with name derived from col-keyword"
  ([col-keyword] (.collection (firebase/firestore) (name col-keyword)))
  ([ref col-keyword] (.collection ref (name col-keyword))))

(defn ref
  "Gets reference to document with name inside collection col"
  ([collection] (.doc collection))
  ([collection ref-name] (.doc collection (name ref-name))))

(defn where
  "Adds where clause to the given query"
  [query field relation value]
  (.where query (name field) relation (if (keyword? value) (name value) value)))

(defn save!
  "Saves document doc data in document represented by reference ref"
  ([ref doc] (.set ref (clj->js doc)))
  ([batch ref doc] (.set batch ref (clj->js doc))))

(defn delete!
  "Deletes given document"
  ([ref] (.delete ref))
  ([batch ref] (.delete batch ref)))

(defn batch!
  [& ops]
  (let [apply-ops (partial reduce #(%2 %1))]
    (-> (.batch (firebase/firestore))
        (apply-ops ops)
        (.commit))))

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
        (assoc :id (keyword id)))))

(defn docs->clj
  "Convenience function that converts given query snapshot into clojure
  collection"
  [snapshot]
  (->> snapshot (docs) (map #(doc->clj %))))

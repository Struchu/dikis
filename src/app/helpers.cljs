(ns app.helpers)

(defn index-by
  [key coll]
  (->> coll
       (map (juxt (comp keyword key) identity))
       (into {})))

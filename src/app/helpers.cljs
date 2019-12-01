(ns app.helpers)

(defn index-by
  [key coll]
  (->> coll
       (map (juxt key identity))
       (into {})))

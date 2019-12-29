(ns app.rules.helpers
  (:require [cljs.test :refer [async deftest is use-fixtures]]
            [promesa.core :as p]
            ["@firebase/testing" :as firebase]
            ["fs" :as fs]))

(def project-id "dikis")

(def rules (.readFileSync fs "firestore.rules" "utf8"))

(defn admin-app
  []
  (->> #js {:projectId project-id}
       (.initializeAdminApp firebase)
       (.firestore)))

(defn authed-app
  [auth]
    (->> #js {:projectId project-id
              :auth auth}
         (.initializeTestApp firebase)
         (.firestore)))

(defn is-successfull
  [promise done]
  (p/handle
    (.assertSucceeds firebase promise)
    (fn [_ error]
      (is (nil? error))
      (done))))

(defn is-failed
  [promise done]
  (p/handle
    (.assertFails firebase promise)
    (fn [_ error]
      (is (nil? error))
      (done))))

(defn load-firestore-rules!
  [done]
  (-> (.loadFirestoreRules firebase #js {:projectId project-id
                                        :rules rules})
      (p/then #(done))))

(defn delete-firebase-apps!
  [done]
  (let [deletes (p/all (for [app (.apps firebase)]
                         (.delete app)))]
    (p/then deletes #(done))))

(defn clear-firestore-data!
  [done]
  (-> (.clearFirestoreData firebase #js {:projectId project-id})
      (p/then #(done))))

(defn doc
  [app & path]
  (reduce (fn [ref [coll id]] (.. ref (collection coll) (doc id))) app path))

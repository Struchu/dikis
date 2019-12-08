(ns app.teams.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :teams
  (fn [db _]
    (->> db
        (:teams)
        (vals)
        (sort-by :name))))

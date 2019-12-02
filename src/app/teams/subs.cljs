(ns app.teams.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :teams
  (fn [db _]
    (-> db
        (get :teams)
        (vals))))

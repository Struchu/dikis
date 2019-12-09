(ns app.dicks.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :dicked-user-name
  (fn [db _]
    (get-in db [:dicked-user :display-name])))


(reg-sub
  :dicks
  (fn [db _]
    (-> db
        (:dicks)
        (vals))))

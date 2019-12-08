(ns app.users.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :users
  (fn [db _]
    (->> db
         (:users)
         (vals)
         (sort-by :display-name))))

(reg-sub
  :permissions
  (fn [db _]
    (get db :permissions)))

(reg-sub
  :admin?
  :<- [:uid]
  :<- [:permissions]
  (fn [[uid permissions] _]
    (= (get permissions uid) :admin)))

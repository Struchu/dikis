(ns app.invitations.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :invitations
  (fn [db _]
    (->> db
         (:invitations)
         (vals)
         (sort-by #(get-in % [:profile :name])))))

(reg-sub
  :invitation-count
  :<- [:invitations]
  (fn [invitations _]
    (count invitations)))

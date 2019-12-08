(ns app.auth.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :logged-in?
  :<- [:uid]
  (fn [uid _]
    (boolean uid)))

(reg-sub
  :uid
  (fn [db _]
    (get-in db [:auth :uid])))

(reg-sub
  :profile
  (fn [db _]
    (get-in db [:auth :profile])))

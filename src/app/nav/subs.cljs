(ns app.nav.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :navbar-open?
  (fn [db _]
    (get-in db [:nav :navbar-open?])))

(reg-sub
  :active-page
  (fn [db _]
    (get-in db [:nav :active-page])))

(reg-sub
  :active-modal
  (fn [db _]
    (get-in db [:nav :active-modal])))

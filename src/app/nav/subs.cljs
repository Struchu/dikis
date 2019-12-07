(ns app.nav.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :nav
  (fn [db _]
    (get db :nav)))

(reg-sub
  :navbar-open?
  :<- [:nav]
  (fn [nav _]
    (get nav :navbar-open?)))

(reg-sub
  :active-page
  :<- [:nav]
  (fn [nav _]
    (get nav :active-page)))

(reg-sub
  :active-modal
  :<- [:nav]
  (fn [nav _]
    (get nav :active-modal)))

(reg-sub
  :active-team
  :<- [:nav]
  (fn [nav _]
    (get nav :active-team)))

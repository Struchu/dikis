(ns app.nav.events
  (:require [re-frame.core :refer [reg-event-db reg-fx]]
            [app.router :as router]))

(reg-fx
  :navigate-to
  (fn [{:keys [path]}]
    (router/set-token! path)))

(reg-event-db
  :toggle-navbar-open
  (fn [db _]
    (update-in db [:nav :navbar-open?] not)))

(reg-event-db
  :route-changed
  (fn [db [_ {:keys [handler]}]]
    (assoc-in db [:nav :active-page] handler)))

(reg-event-db
  :open-modal
  (fn [db [_ modal-name]]
    (assoc-in db [:nav :active-modal] modal-name)))

(reg-event-db
  :close-modal
  (fn [db _]
    (assoc-in db [:nav :active-modal] nil)))

(ns app.nav.events
  (:require [re-frame.core :refer [reg-event-db reg-event-fx reg-fx path]]
            [app.router :as router]))

(def nav-interceptors [(path :nav)])

(reg-fx
  :navigate-to
  (fn [{:keys [path]}]
    (router/set-token! path)))

(reg-event-db
  :toggle-navbar-open
  nav-interceptors
  (fn [nav _]
    (update nav :navbar-open? not)))

(reg-event-fx
  :route-changed
  nav-interceptors
  (fn [{nav :db} [_ {:keys [handler route-params]}]]
    (let [nav (assoc nav :active-page handler)]
      (case handler
        :dicks
        {:db (assoc nav :active-team (keyword (:team-id route-params)))}
        
        {:db (dissoc nav :active-team)}))))

(reg-event-db
  :open-modal
  nav-interceptors
  (fn [nav [_ modal-name]]
    (assoc nav :active-modal modal-name)))

(reg-event-db
  :close-modal
  nav-interceptors
  (fn [nav _]
    (assoc nav :active-modal nil)))

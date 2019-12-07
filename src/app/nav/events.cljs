(ns app.nav.events
  (:require [re-frame.core :refer [reg-event-db reg-event-fx reg-fx path]]
            [app.firebase.db :as db]
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
  (fn [{:keys [db]} [_ {:keys [handler route-params]}]]
    (let [next-page (assoc-in db [:nav :active-page] handler)
          uid (get-in db [:auth :uid])]
      (case handler
        :teams
        {:db (assoc-in next-page [:nav :active-team] nil)
         :app.firebase.events/observations [{:action :clean}
                                            {:action :start
                                             :id :user-team
                                             :subject (-> :user-team
                                                          (db/collection)
                                                          (db/where :uid "==" uid))
                                             :event :save-teams}]}

        :dicks
        {:db (assoc-in next-page [:nav :active-team] (keyword (:team-id route-params)))}

        :users
        {:db (assoc-in next-page [:nav :active-team] (keyword (:team-id route-params)))
         :app.firebase.events/observations [{:action :start
                                             :id :users
                                             :override true
                                             :subject (-> :user-team
                                                          (db/collection)
                                                          (db/where :team-id "==" (:team-id route-params)))
                                             :event :save-users}
                                            {:action :start
                                             :id :permissions
                                             :override true
                                             :subject (-> :team-role
                                                          (db/collection)
                                                          (db/ref (:team-id route-params))
                                                          (db/collection :members))
                                             :event :save-permissions}]}

        {:db (assoc-in next-page [:nav :active-team] nil)}))))

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

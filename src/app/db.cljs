(ns app.db
  (:require [re-frame.core :as rf]))

(def initial-app-db {:nav {:navbar-open? false
                           :active-page nil
                           :active-team nil
                           :active-modal nil}
                     :auth {:uid nil
                            :profile nil}
                     :teams {}
                     :users {}
                     :invitations {}
                     :permissions {}})

(rf/reg-event-fx
  :initialize-db
  [(rf/inject-cofx :local-store-user)]
  (fn [{:keys [local-store-user]} _]
    {:db (assoc-in initial-app-db [:auth] local-store-user)}))


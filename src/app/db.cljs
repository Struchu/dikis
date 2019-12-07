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
                     :permissions {}})

(rf/reg-event-db
  :initialize-db
  (fn [_ _]
    initial-app-db))


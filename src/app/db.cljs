(ns app.db
  (:require [re-frame.core :as rf]))

(def initial-app-db {:nav {:navbar-open? true
                           :active-page nil}
                     :auth {:uid nil
                            :profile nil}})

(rf/reg-event-db
  :initialize-db
  (fn [_ _]
    initial-app-db))

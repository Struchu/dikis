(ns app.teams.events
  (:require [re-frame.core :refer [reg-event-fx]]))

(reg-event-fx
  :create-team
  (fn [{:keys [db uuid]} [_ {:keys [name picture-url]}]]
    {}))

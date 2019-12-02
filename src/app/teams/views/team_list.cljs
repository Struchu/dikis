(ns app.teams.views.team-list
  (:require [app.teams.views.team-card :refer [team-card]]))

(defn team-list
  [teams]
  [:div.columns.is-multiline.is-mobile
    (for [{:keys [team-id] :as team} teams]
         ^{:key team-id}
         [:div.column.is-one-quarter
           [team-card team]])])

(ns app.teams.views.team-list
  (:require [app.teams.views.team-card :refer [team-card]]
            [app.teams.views.team-creator :refer [team-creator]]))

(defn team-list
  [teams]
  [:div.columns.is-multiline.is-mobile
    [:div.column.is-one-quarter
     [team-creator]]
    (for [{:keys [team-id] :as team} teams]
         ^{:key team-id}
         [:div.column.is-one-quarter
           [team-card team]])])

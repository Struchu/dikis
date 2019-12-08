(ns app.teams.views.team-list
  (:require [re-frame.core :as rf]
            [app.teams.views.team-card :refer [team-card]]
            [app.teams.views.team-creator :refer [team-creator]]))

(defn team-list
  []
  (let [teams @(rf/subscribe [:teams])]
    [:div.columns.is-multiline
      [:div.column.is-one-quarter
       [team-creator]]
      (for [{:keys [team-id] :as team} teams]
        ^{:key team-id}
        [team-card team])]))

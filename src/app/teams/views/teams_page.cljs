(ns app.teams.views.teams-page
  (:require [re-frame.core :as rf]
            [app.teams.views.team-creator :refer [team-creator]]
            [app.teams.views.team-list :refer [team-list]]))

(defn teams-page
  []
  (let [teams @(rf/subscribe [:teams])]
    [:div.section>div.container
     [:div.level
      [:div.level-left
       [:h1.title "Teams"]]
      [:div.level-right
        [:div.level-item [team-creator]]]]
     [team-list teams]]))

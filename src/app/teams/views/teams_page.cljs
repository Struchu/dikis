(ns app.teams.views.teams-page
  (:require [app.teams.views.team-creator :refer [team-creator]]))

(defn teams-page
  []
  [:div.section>div.container
   [:div.level
    [:div.level-left
     [:h1.title "Teams"]]
    [:div.level-right
      [:p.level-item [team-creator]]]]])

(ns app.teams.views.teams-page
  (:require [re-frame.core :as rf]
            [app.components.teams-tab-bar :refer [teams-tab-bar]]
            [app.teams.views.team-list :refer [team-list]]))

(defn teams-page
  []
  [:div.section>div.container
   [teams-tab-bar] 
   [team-list]])

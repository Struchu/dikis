(ns app.invitations.views.invitations-page
  (:require [app.components.teams-tab-bar :refer [teams-tab-bar]]))

(defn invitations-page
  []
  [:div.section>div.container
   [teams-tab-bar]])

(ns app.invitations.views.invitations-page
  (:require [app.components.teams-tab-bar :refer [teams-tab-bar]]
            [app.invitations.views.invitation-list :refer [invitation-list]]))

(defn invitations-page
  []
  [:div.section>div.container
   [teams-tab-bar]
   [invitation-list]])

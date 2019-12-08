(ns app.users.views.users-page
  (:require [app.components.team-tab-bar :refer [team-tab-bar]]
            [app.users.views.user-list :refer [user-list]]))

(defn users-page
  []
  [:section.section>div.container
   [team-tab-bar]
   [user-list]])

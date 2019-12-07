(ns app.users.views.users-page
  (:require [app.components.team-tab-bar :refer [team-tab-bar]]))

(defn users-page
  []
  [:section.section>div.container
   [team-tab-bar]])

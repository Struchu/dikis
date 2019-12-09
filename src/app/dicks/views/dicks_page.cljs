(ns app.dicks.views.dicks-page
  (:require [app.components.team-tab-bar :refer [team-tab-bar]]
            [app.router :as router]
            [app.dicks.views.dick-list :refer [dick-list]]))

(defn dicks-page
  []
  [:section.section>div.container
   [team-tab-bar]
   [dick-list]])

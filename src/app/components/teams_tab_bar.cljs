(ns app.components.teams-tab-bar
  (:require [app.components.tab-bar :refer [tab-bar]]
            [app.router :as router]))

(defn teams-tab-bar
  []
  [tab-bar {:tabs [{:id :teams
                    :label "Teams"
                    :path (router/path-for :teams)}

                   {:id :invitations
                    :label "Invitations"
                    :path (router/path-for :invitations)}]}])

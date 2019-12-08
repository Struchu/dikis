(ns app.components.teams-tab-bar
  (:require [re-frame.core :as rf]
            [app.components.tab-bar :refer [tab-bar]]
            [app.router :as router]))

(defn teams-tab-bar
  []
  (let [invitation-count @(rf/subscribe [:invitation-count])]
    [tab-bar {:tabs [{:id :teams
                      :label "Teams"
                      :path (router/path-for :teams)}

                     {:id :invitations
                      :label [:<>
                                "Invitations "
                                (when (> invitation-count 0)
                                  [:span.tag.is-danger invitation-count])]
                      :path (router/path-for :invitations)}]}]))

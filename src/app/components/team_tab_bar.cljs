(ns app.components.team-tab-bar
  (:require [re-frame.core :as rf]
            [app.components.tab-bar :refer [tab-bar]]
            [app.router :as router]))

(defn team-tab-bar
  []
  (let [active-team @(rf/subscribe [:active-team])]
    [tab-bar {:tabs [{:id :dicks
                      :label "Dicks"
                      :path (when active-team (router/path-for :dicks :team-id active-team))}

                     {:id :users
                      :label "Users"
                      :path (when active-team (router/path-for :users :team-id active-team))}]}]))

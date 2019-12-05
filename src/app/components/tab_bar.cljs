(ns app.components.tab-bar
  (:require [re-frame.core :as rf]))

(defn tab-bar
  [{:keys [tabs]}]
  (let [active-page @(rf/subscribe [:active-page])]
    [:div.tabs.is-centered.is-large>ul
     (for [{:keys [id label path]} tabs]
       ^{:key id}
       [:li {:class (when (= id active-page) "is-active")}
        [:a {:href path}
         label]])]))

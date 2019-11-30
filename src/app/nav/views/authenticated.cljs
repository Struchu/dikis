(ns app.nav.views.authenticated
  (:require [re-frame.core :as rf]
            [app.router :as router]))

(defn authenticated
  []
  (let [{:keys [photo-url]} @(rf/subscribe [:profile])]
    [:div.navbar-end
     [:div.navbar-item.has-dropdown.is-hoverable
      [:a.navbar-link 
       [:figure.image.is-24x24
        [:img.is-rounded {:src photo-url}]]]
      [:div.navbar-dropdown
       [:a.navbar-item {:href (router/path-for :teams)}
        "Teams"]
       [:a.navbar-item {:href "#"
                        :on-click #(rf/dispatch [:sign-out])}
        "Sign out"]]]]))

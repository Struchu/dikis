(ns app.nav.views.nav
  (:require [re-frame.core :as rf]
            [app.router :as router]
            [app.nav.views.authenticated :refer [authenticated]]
            [app.nav.views.public :refer [public]]))

(defn nav
  []
  (let [logged-in? @(rf/subscribe [:logged-in?])
        navbar-open? @(rf/subscribe [:navbar-open?])]
    [:nav.navbar.is-light {:role "navigation"
                           :aria-label "main navigation"}
      [:div.navbar-brand
        [:a.navbar-item {:href (router/path-for :call-to-action)}
          "Dikis"]
        [:div.navbar-burger.burger {:class (when navbar-open? "is-active")
                                    :aria-label "menu"
                                    :aria-expanded navbar-open?
                                    :on-click #(rf/dispatch [:toggle-navbar-open])}
          (for [i (range 0 3)]
            ^{:key i}
            [:span {:aria-hidden true}])]]
      [:div.navbar-menu {:class (when navbar-open? "is-active")}
        [(if logged-in? authenticated public)]]]))

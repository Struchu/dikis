(ns app.nav.views.authenticated
  (:require [re-frame.core :as rf]))

(defn authenticated
  []
  [:div.navbar-end
   [:a.navbar-item {:href "#"
                    :on-click #(rf/dispatch [:sign-out])}
    "Sign out"]])

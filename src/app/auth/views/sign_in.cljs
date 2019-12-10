(ns app.auth.views.sign-in
  (:require [re-frame.core :as rf]))

(defn sign-in
  []
  [:section.section
    [:div.columns
      [:div.column.is-half.is-offset-one-quarter
        [:div.box
          [:h1.title.has-text-centered "Sign in"]
          [:div.level>:div.level-item
           [:a.button.is-large {:href "#"
                :on-click #(rf/dispatch [:sign-in-with-google])}
            [:span.icon
             [:i.fab.fa-google]]
            [:span "Google"]]]]]]])


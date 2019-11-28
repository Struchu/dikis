(ns app.auth.views.sign-in
  (:require [re-frame.core :as rf]))

(defn sign-in
  []
  [:section.section
    [:div.columns
      [:div.column.is-half.is-offset-one-quarter
        [:div.box
          [:h1.title.has-text-centered "Sign in"]
          [:a {:href "#"
               :on-click #(rf/dispatch [:sign-in-with-google])}
           ;; TODO: replace with icon
           "Sign in with Google"]]]]])


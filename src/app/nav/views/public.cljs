(ns app.nav.views.public
  (:require [app.router :as router]))

(defn public
  []
  [:div.navbar-end
    [:a.navbar-item {:href (router/path-for :sign-in)}
      "Sign in"]])

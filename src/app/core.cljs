(ns app.core
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [app.db]
            [app.router :as router]
            ;; -- Firebase --
            [app.firebase.auth :refer [on-auth-state-changed]]
            [app.firebase.init :refer [firebase-init]]
            [app.firebase.events]
            ;; -- auth --
            [app.auth.views.sign-in :refer [sign-in]]
            [app.auth.events]
            [app.auth.subs]
            ;; -- index --
            [app.index.views.index :refer [index]]
            ;; -- nav --
            [app.nav.views.nav :refer [nav]]
            [app.nav.events]
            [app.nav.subs]
            ;; -- teams --
            [app.teams.views.teams-page :refer [teams-page]]
            [app.teams.events]
            [app.teams.subs]))

(defn pages
  [page-name]
  (case page-name
    :index [index]
    :sign-in [sign-in]
    :teams [teams-page]
    [index]))

(defn app
  []
  (let [active-page @(rf/subscribe [:active-page])]
    [:<>
      [nav]
      (pages active-page)]))

(defn ^:dev/after-load start
  []
  (rf/dispatch-sync [:initialize-db])
  (router/start!)
  (firebase-init)
  (on-auth-state-changed)
  (r/render [app]
    (.getElementById js/document "app")))

(defn ^:export init
  []
  (start))

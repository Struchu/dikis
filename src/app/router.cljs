(ns app.router
  (:require [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [re-frame.core :as rf]))

(def routes ["/" {""              :index
                  "sign-in/"      :sign-in
                  "invitations/"  :invitations
                  "teams/"        {"" :teams
                                   [:team-id "/"] :dicks
                                   [:team-id "/users"] :users}}])

(def history
  (let [dispatch #(rf/dispatch [:route-changed %])
        match #(bidi/match-route routes %)]
    (pushy/pushy dispatch match)))

(def path-for (partial bidi/path-for routes))

(defn start!
  []
  (pushy/start! history))

(defn set-token!
  [token]
  (pushy/set-token! history token))

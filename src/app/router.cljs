(ns app.router
  (:require [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [re-frame.core :as rf]))

(def routes ["/" {""            :index
                  "sign-in/"    :sign-in
                  "teams/"      :teams}])

(def history
  (let [dispatch #(rf/dispatch [:route-changed %])
        match #(bidi/match-route routes %)]
    (pushy/pushy dispatch match)))

(defn path-for
  [route]
  (bidi/path-for routes route))

(defn start!
  []
  (pushy/start! history))

(defn set-token!
  [token]
  (pushy/set-token! history token))

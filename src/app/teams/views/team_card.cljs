(ns app.teams.views.team-card
  (:require [re-frame.core :as rf]
            [app.router :as router]))

(defn team-card
  [{:keys [team-id name picture-url]}]
  [:a {:href (router/path-for :dicks :team-id team-id)}
    [:div.card.is-hoverable 
     [:div.card-image
      [:figure.image.is-4by3
       [:img {:src picture-url
              :alt "Team image"}]]]
     [:div.card-content
      [:div.media
       [:div.media-content
        [:p.title.is-4
         name]]]]]])

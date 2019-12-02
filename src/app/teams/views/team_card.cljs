(ns app.teams.views.team-card)

(defn team-card
  [{:keys [team-id name picture-url]}]
  [:div.card
   [:div.card-image
    [:figure.image.is-4by3
     [:img {:src picture-url
            :alt "Team image"}]]]
   [:div.card-content
    [:div.media
     [:div.media-content
      [:p.title.is-4
       name]]]]])

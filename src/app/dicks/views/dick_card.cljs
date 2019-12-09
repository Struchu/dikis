(ns app.dicks.views.dick-card
  (:require [re-frame.core :as rf]))

(defn dick-card
  [{:keys [desc receiver]}]
  (let [{:keys [display-name email photo-url]} receiver]
    [:div.card.is-hoverable
     [:div.card-image
      [:figure.image.is-square
       [:img {:src "/img/dick.png"
              :alt "Dick"}]]]
     [:div.card-content
      [:div.media
       [:div.media-left
        [:figure.image.is-48x48
         [:img.is-rounded {:src photo-url
                :alt "User image"}]]]
       [:div.media-content
        [:p.title.is-4
         display-name]
        [:p.subtitle.is-6
         email]]]
      [:div.content
       desc]]]))

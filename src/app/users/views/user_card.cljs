(ns app.users.views.user-card
  (:require [re-frame.core :as rf]))

(defn user-card
  [{:keys [uid email display-name photo-url]}]
  [:a {:href "#"
       :on-click #(rf/dispatch [:give-dick uid])}
    [:div.card.is-hoverable
     [:div.card-image
      [:figure.image.is-4by3
       [:img {:src photo-url
              :alt "User image"}]]]
     [:div.card-content
      [:div.media
       [:div.media-content
        [:p.title.is-4
         display-name]
        [:p.subtitle.is-6
         email]]]]]])

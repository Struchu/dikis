(ns app.invitations.views.invitation-card
  (:require [re-frame.core :as rf]))

(defn invitation-card
  [{:keys [id team]}]
  (let [{:keys [name picture-url]} team]
    [:div.card.is-hoverable
     [:div.card-image
      [:figure.image.is-4by3
       [:img {:src picture-url
              :alt "Team image"}]]]
     [:div.card-content
      [:div.media
       [:div.media-content
        [:p.title.is-4
         name]]]]
     [:div.card-footer
      [:a.card-footer-item.has-background-success.has-text-white {:href "#"
                                                                  :on-click #(rf/dispatch [:accept-invitation id])}
       "Accept"]
      [:a.card-footer-item.has-background-danger.has-text-white {:href "#"
                                                                 :on-click #(rf/dispatch [:decline-invitation id])}
       "Decline"]]]))

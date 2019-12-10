(ns app.users.views.user-inviter
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [app.components.field :refer [field]]
            [app.components.modal :refer [modal]]))

(defn user-inviter
  []
  (let [initial-values {:email ""}
        values (r/atom initial-values)
        open-modal (fn [modal-name]
                     (reset! values initial-values)
                     (rf/dispatch [:open-modal modal-name]))
        invite-user (fn [{:keys [email]}]
                      (rf/dispatch [:invite-user {:email email}])
                      (rf/dispatch [:close-modal]))]
  (fn []
    [:<>
      [:a.button.is-light.is-fullwidth.is-fullheight {:on-click #(open-modal :invite-user)}
       [:span.icon>i.fas.fa-user-friends]
       [:span "Invite user"]]
      [modal {:modal-name :invite-user
              :title "Invite user"
              :body [:<>
                     [field {:id :email
                             :name "E-mail"
                             :type "email"
                             :placeholder "User to be invited"
                             :values values}]]
              :footer [:<>
                       [:button.button.is-success {:on-click #(invite-user @values)}
                        "Invite user"]
                       [:button.button {:on-click #(rf/dispatch [:close-modal])}
                        "Cancel"]]}]])))

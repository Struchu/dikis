(ns app.teams.views.team-creator
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [app.components.field :refer [field]]
            [app.components.modal :refer [modal]]))

(defn team-creator
  []
  (let [initial-values {:name "" :picture-url ""}
        values (r/atom initial-values)
        open-modal (fn [modal-name]
                     (reset! values initial-values)
                     (rf/dispatch [:open-modal modal-name]))
        create-team (fn [{:keys [name picture-url]}]
                      (rf/dispatch [:create-team {:name name
                                                  :picture-url picture-url}])
                      (rf/dispatch [:close-modal]))]
  (fn []
    [:<>
      [:a.button.is-success {:on-click #(open-modal :create-team)}
       "New"]
      [modal {:modal-name :create-team
              :title "Create team"
              :body [:<>
                     [field {:id :name
                             :label "Name"
                             :type "text"
                             :placeholder "Team name"
                             :values values}]
                     [field {:id :picture-url
                             :label "Team image"
                             :type "text"
                             :placeholder "Image URL"
                             :values values}]]
              :footer [:<>
                       [:button.button.is-success {:on-click #(create-team @values)}
                        "Create team"]
                       [:button.button {:on-click #(rf/dispatch [:close-modal])}
                        "Cancel"]]}]])))

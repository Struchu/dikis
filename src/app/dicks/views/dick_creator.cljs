(ns app.dicks.views.dick-creator
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [app.components.field :refer [field]]
            [app.components.modal :refer [modal]]))

(defn dick-creator
  []
  (let [initial-values {:desc ""}
        values (r/atom initial-values)
        close-modal (fn []
                      (rf/dispatch [:close-modal])
                      (reset! values initial-values))
        save-dick (fn []
                    (rf/dispatch [:save-dick @values])
                    (close-modal))]
    (fn []
      (let [dicked-user-name @(rf/subscribe [:dicked-user-name])]
        [modal {:modal-name :dick-creator
                :title "Give the dick away!"
                :body [field {:id :desc
                              :label (str "Why does " dicked-user-name " deserve a dick?")
                              :values values
                              :placeholder "Reason"
                              :type "text"}]
                :footer [:<>
                         [:button.button.is-success {:on-click #(save-dick)}
                          "Save dick!"]
                         [:button.button {:on-click #(close-modal)}
                          "Cancel"]]}]))))

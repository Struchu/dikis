(ns app.components.modal
  (:require [re-frame.core :as rf]))

(defn modal
  [{:keys [modal-name title body footer]}]
  (let [active-modal @(rf/subscribe [:active-modal])]
    [:div.modal {:class (when (= active-modal modal-name) "is-active")}
     [:div.modal-background]
     [:div.modal-card
      [:div.modal-card-head
       [:div.modal-card-title title]
       [:button.delete {:aria-label "close"
                        :on-click #(rf/dispatch [:close-modal])}]]
      [:div.modal-card-body body]
      [:div.modal-card-foot footer]]]))

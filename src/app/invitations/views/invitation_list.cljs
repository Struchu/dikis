(ns app.invitations.views.invitation-list
  (:require [re-frame.core :as rf]
            [app.invitations.views.invitation-card :refer [invitation-card]]))

(defn invitation-list
  []
  (let [invitations @(rf/subscribe [:invitations])]
    [:div.columns.is-multiline
      (for [{:keys [id] :as invitation} invitations]
        [:div.column.is-one-quarter {:key id}
         [invitation-card invitation]])]))

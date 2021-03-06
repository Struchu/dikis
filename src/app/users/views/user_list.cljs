(ns app.users.views.user-list
  (:require [re-frame.core :as rf]
            [app.dicks.views.dick-creator :refer [dick-creator]]
            [app.users.views.user-card :refer [user-card]]
            [app.users.views.user-inviter :refer [user-inviter]]))

(defn user-list
  []
  (let [users @(rf/subscribe [:users])
        admin? @(rf/subscribe [:admin?])]
    [:<>
     [:div.columns.is-multiline
       (when admin? [:div.column.is-one-quarter
        [user-inviter]])
       (for [{:keys [uid] :as user} users]
         [:div.column.is-one-quarter {:key uid}
          [user-card user]])]
     [dick-creator]]))

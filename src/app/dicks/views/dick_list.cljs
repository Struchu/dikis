(ns app.dicks.views.dick-list
  (:require [re-frame.core :as rf]
            [app.dicks.views.dick-card :refer [dick-card]]))

(defn dick-list
  []
  (let [dicks @(rf/subscribe [:dicks])]
    [:<>
     [:div.columns.is-multiline
       (for [{:keys [id] :as dick} dicks]
         [:div.column.is-one-third {:key id}
          [dick-card dick]])]]))

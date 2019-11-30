(ns app.components.field)

(defn field
  [{:keys [id label placeholder type values]}]
  [:div.field
   [:div.label {:for id}
    label]
   [:div.control
    [:input.input {:type type
                   :placeholder placeholder
                   :value (id @values)
                   :on-change #(swap! values assoc id (.. % -target -value))}]]])


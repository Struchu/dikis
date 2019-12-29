(ns app.invitations.events
  (:require [re-frame.core :refer [reg-event-db reg-event-fx reg-fx]]
            [promesa.core :as p]
            [app.firebase.db :as db]
            [app.helpers :as h]))

(defn docs->invitations
  "Extract invitations from provided snapshot" [snapshot]
  (->> snapshot
       (db/docs->clj)
       (h/index-by :id)))

(reg-event-db
  :save-invitations
  (fn [db [_ {:keys [data]}]]
    (assoc db :invitations (docs->invitations data))))

(reg-fx
  :decline-invitation
  (fn [{:keys [invitation-id]}]
    (let [ref (-> :invitations (db/collection) (db/ref invitation-id))]
      (db/delete! ref))))

(reg-event-fx
  :decline-invitation
  (fn [_ [_ invitation-id]]
    {:decline-invitation {:invitation-id invitation-id}}))

(reg-fx
  :accept-invitation
  (fn [{:keys [invitation-id profile team]}]
    (let [{:keys [name team-id picture-url]} team
          {:keys [email photo-url display-name uid]} profile
          team-member-ref (-> :team-role
                              (db/collection)
                              (db/ref team-id)
                              (db/collection :members)
                              (db/ref uid))
          invitation-ref (-> :invitations (db/collection) (db/ref invitation-id))]
      (-> (db/save! team-member-ref {:role :member
                                     :invitation-id invitation-id})
          (p/then #(db/save! (db/ref (db/collection :user-team)) {:uid uid
                                                                  :team-id team-id
                                                                  :name name
                                                                  :picture-url picture-url
                                                                  :profile {:email email
                                                                            :photo-url photo-url
                                                                            :display-name display-name}}))
          (p/then #(db/delete! invitation-ref))))))

(reg-event-fx
  :accept-invitation
  (fn [{:keys [db]} [_ invitation-id]]
    (let [{:keys [email photo-url display-name uid]} (get-in db [:auth :profile])
          {:keys [name picture-url team-id]} (get-in db [:invitations invitation-id :team])
          ]
      {:accept-invitation {:invitation-id invitation-id
                           :profile {:email email
                                     :photo-url photo-url
                                     :display-name display-name
                                     :uid uid}
                           :team {:team-id team-id
                                  :name name
                                  :picture-url picture-url}}})))

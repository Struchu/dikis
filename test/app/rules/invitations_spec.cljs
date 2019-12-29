(ns app.rules.invitations-spec
  (:require [cljs.test :refer [async deftest is use-fixtures]]
            [promesa.core :as p]
            [app.rules.helpers :as h]))

(use-fixtures
  :once {:before (fn []
                   (async done
                     (h/load-firestore-rules! done)))

         :after (fn []
                  (async done
                    (h/delete-firebase-apps! done)))})

(use-fixtures
  :each {:before (fn []
                   (async done
                     (h/clear-firestore-data! done)))})

(deftest admin-can-invite-user
  []
  (async done
    (let [admin (h/admin-app)
          team (h/doc admin ["team-role" "team-id"])
          admin-role (h/doc team ["members" "user-id"])
          app (h/authed-app #js {:uid "user-id"})
          invitation (h/doc app ["invitations" "invitation-id"])]
      (-> (.set team #js {:owner "owner-id"})
          (p/then #(.set admin-role #js {:role "admin"}))
          (p/then #(h/is-successfull (.set invitation (clj->js {:team {:team-id "team-id"}
                                                                :email "test@example.com"})) done))))))

(deftest non-admin-cant-invite-user
  []
  (async done
    (let [app (h/authed-app #js {:uid "user-id"})
          invitation (h/doc app ["invitations" "invitation-id"])]
      (h/is-failed (.set invitation (clj->js {:team {:team-id "team-id"}
                                              :email "test@example.com"})) done))))

(deftest invitee-can-read-invitation
  []
  (async done
    (let [admin (h/admin-app)
          admin-invitation (h/doc admin ["invitations" "invitation-id"])
          app (h/authed-app #js {:uid "user-id" :email "test@example.com"})
          invitation (h/doc app ["invitations" "invitation-id"])]
      (-> (.set admin-invitation (clj->js {:team {:team-id "team-id"}
                                           :email "test@example.com"}))
          (p/then #(h/is-successfull (.get invitation) done))))))

(deftest non-invitee-cant-read-invitation
  []
  (async done
    (let [admin (h/admin-app)
          admin-invitation (h/doc admin ["invitations" "invitation-id"])
          app (h/authed-app #js {:uid "user-id" :email "other@example.com"})
          invitation (h/doc app ["invitations" "invitation-id"])]
      (-> (.set admin-invitation (clj->js {:team {:team-id "team-id"}
                                           :email "test@example.com"}))
          (p/then #(h/is-failed (.get invitation) done))))))

(deftest invitee-can-delete-invitation
  []
  (async done
    (let [admin (h/admin-app)
          admin-invitation (h/doc admin ["invitations" "invitation-id"])
          app (h/authed-app #js {:uid "user-id" :email "test@example.com"})
          invitation (h/doc app ["invitations" "invitation-id"])]
      (-> (.set admin-invitation (clj->js {:team {:team-id "team-id"}
                                           :email "test@example.com"}))
          (p/then #(h/is-successfull (.delete invitation) done))))))

(deftest non-invitee-cant-delete-invitation
  []
  (async done
    (let [admin (h/admin-app)
          admin-invitation (h/doc admin ["invitations" "invitation-id"])
          app (h/authed-app #js {:uid "user-id" :email "other@example.com"})
          invitation (h/doc app ["invitations" "invitation-id"])]
      (-> (.set admin-invitation (clj->js {:team {:team-id "team-id"}
                                           :email "test@example.com"}))
          (p/then #(h/is-failed (.delete invitation) done))))))

(deftest invitation-is-immutable
  []
  (async done
    (let [admin (h/admin-app)
          admin-invitation (h/doc admin ["invitations" "invitation-id"])
          app (h/authed-app #js {:uid "user-id" :email "test@example.com"})
          invitation (h/doc app ["invitations" "invitation-id"])]
      (-> (.set admin-invitation (clj->js {:team {:team-id "team-id"}
                                           :email "test@example.com"}))
          (p/then #(h/is-failed (.set invitation (clj->js {:team {:team-id "other-team-id"}})) done))))))

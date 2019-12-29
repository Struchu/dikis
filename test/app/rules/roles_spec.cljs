(ns app.rules.roles-spec
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

(deftest can-create-own-team-role
  []
  (async done
    (let [app (h/authed-app #js {:uid "user-id"})
          team-role (h/doc app ["team-role" "team-id"])]
      (h/is-successfull (.set team-role #js {:owner "user-id"}) done))))

(deftest team-role-is-immutable
  []
  (async done
    (let [admin (h/admin-app)
          admin-team-role (h/doc admin ["team-role" "team-id"])
          app (h/authed-app #js {:uid "other-user-id"})
          team-role (h/doc app ["team-role" "team-id"])]
      (-> (.set admin-team-role #js {:owner "user-id"})
          (p/then #(h/is-failed (.set team-role #js {:owner "other-user-id"}) done))))))

(deftest team-role-cant-be-deleted
  []
  (async done
    (let [admin (h/admin-app)
          admin-team-role (h/doc admin ["team-role" "team-id"])
          app (h/authed-app #js {:uid "other-user-id"})
          team-role (h/doc app ["team-role" "team-id"])]
      (-> (.set admin-team-role #js {:owner "user-id"})
          (p/then #(h/is-failed (.delete team-role) done))))))

(deftest owner-can-become-admin
  []
  (async done
    (let [admin (h/admin-app)
          admin-team-role (h/doc admin ["team-role" "team-id"])
          app (h/authed-app #js {:uid "user-id"})
          admin-role (h/doc app ["team-role" "team-id"] ["members" "user-id"])]
      (-> (.set admin-team-role #js {:owner "user-id"})
          (p/then #(h/is-successfull (.set admin-role #js {:role "admin"}) done))))))

(deftest non-owner-cant-become-admin
  []
  (async done
    (let [admin (h/admin-app)
          team-role (h/doc admin ["team-role" "team-id"])
          app (h/authed-app #js {:uid "other-user-id"})
          admin-role (h/doc app ["team-role" "team-id"] ["members" "other-user-id"])]
      (-> (.set team-role #js {:owner "user-id"})
          (p/then #(h/is-failed (.set admin-role #js {:role "admin"}) done))))))


(deftest team-member-can-read-roles
  []
  (async done
    (let [admin (h/admin-app)
          team-role (h/doc admin ["team-role" "team-id"])
          admin-team-role (h/doc team-role ["members" "user-id"])
          app (h/authed-app #js {:uid "user-id"})
          admin-role (h/doc app ["team-role" "team-id"] ["members" "user-id"])]
      (-> (.set team-role #js {:owner "user-id"})
          (p/then #(.set admin-team-role #js {:role "admin"}))
          (p/then #(h/is-successfull (.get admin-role) done))))))


(deftest invitee-can-become-member
  []
  (async done
    (let [admin (h/admin-app)
          admin-invitation (h/doc admin ["invitations" "invitation-id"])
          admin-team-role (h/doc admin ["team-role" "team-id"])
          app (h/authed-app #js {:uid "user-id" :email "test@example.com"})
          team-role (h/doc app ["team-role" "team-id"] ["members" "user-id"])]
      (-> (.set admin-invitation (clj->js {:team {:team-id "team-id"}
                                           :email "test@example.com"}))
          (p/then #(.set admin-team-role #js {:owner "owner-id"}))
          (p/then #(h/is-successfull (.set team-role #js {:invitation-id "invitation-id"
                                                          :role "member"}) done))))))

(deftest non-invitee-cant-become-member
  []
  (async done
    (let [admin (h/admin-app)
          admin-team-role (h/doc admin ["team-role" "team-id"])
          app (h/authed-app #js {:uid "user-id" :email "test@example.com"})
          team-role (h/doc app ["team-role" "team-id"] ["members" "user-id"])]
      (-> (.set admin-team-role #js {:owner "owner-id"})
          (p/then #(h/is-failed (.set team-role #js {:invitation-id "invitation-id"
                                                     :role "member"}) done))))))

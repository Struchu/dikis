(ns app.rules.teams-spec
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

(deftest admin-can-create-team-profile
  []
  (async done
    (let [admin (h/admin-app)
          team (h/doc admin ["team-role" "team-id"])
          admin-role (h/doc team ["members" "user-id"])
          app (h/authed-app #js {:uid "user-id"})
          user-team (h/doc app ["user-team" "user-team-id"])]
      (-> (.set team #js {:owner "owner-id"})
          (p/then #(.set admin-role #js {:role "admin"}))
          (p/then #(h/is-successfull (.set user-team #js {:team-id "team-id"
                                                          :uid "user-id"}) done))))))

(deftest admin-cant-create-team-profile-for-other-user
  []
  (async done
    (let [admin (h/admin-app)
          team (h/doc admin ["team-role" "team-id"])
          admin-role (h/doc team ["members" "user-id"])
          app (h/authed-app #js {:uid "user-id"})
          user-team (h/doc app ["user-team" "user-team-id"])]
      (-> (.set team #js {:owner "owner-id"})
          (p/then #(.set admin-role #js {:role "admin"}))
          (p/then #(h/is-failed (.set user-team #js {:team-id "team-id"
                                                     :uid "other-user-id"}) done))))))

(deftest member-can-create-team-profile
  []
  (async done
    (let [admin (h/admin-app)
          team (h/doc admin ["team-role" "team-id"])
          admin-role (h/doc team ["members" "user-id"])
          app (h/authed-app #js {:uid "user-id"})
          user-team (h/doc app ["user-team" "user-team-id"])]
      (-> (.set team #js {:owner "owner-id"})
          (p/then #(.set admin-role #js {:role "member"}))
          (p/then #(h/is-successfull (.set user-team #js {:team-id "team-id"
                                                          :uid "user-id"}) done))))))

(deftest member-cant-create-team-profile-for-other-user
  []
  (async done
    (let [admin (h/admin-app)
          team (h/doc admin ["team-role" "team-id"])
          admin-role (h/doc team ["members" "user-id"])
          app (h/authed-app #js {:uid "user-id"})
          user-team (h/doc app ["user-team" "user-team-id"])]
      (-> (.set team #js {:owner "owner-id"})
          (p/then #(.set admin-role #js {:role "member"}))
          (p/then #(h/is-failed (.set user-team #js {:team-id "team-id"
                                                     :uid "other-user-id"}) done))))))

(deftest user-can-read-own-team-profile
  []
  (async done
    (let [admin (h/admin-app)
          team (h/doc admin ["team-role" "team-id"])
          admin-role (h/doc team ["members" "user-id"])
          admin-user-team (h/doc admin ["user-team" "user-team-id"])
          app (h/authed-app #js {:uid "user-id"})
          user-team (h/doc app ["user-team" "user-team-id"])]
      (-> (.set admin-user-team #js {:team-id "team-id"
                                     :uid "user-id"})
          (p/then #(.set team #js {:owner "owner-id"}))
          (p/then #(.set admin-role #js {:role "member"}))
          (p/then #(h/is-successfull (.get user-team) done))))))

(deftest user-cant-read-other-user-team-profile
  []
  (async done
    (let [admin (h/admin-app)
          admin-user-team (h/doc admin ["user-team" "user-team-id"])
          app (h/authed-app #js {:uid "user-id"})
          user-team (h/doc app ["user-team" "user-team-id"])]
      (-> (.set admin-user-team #js {:team-id "team-id"
                                     :uid "other-user-id"})
          (p/then #(h/is-failed (.get user-team) done))))))

(deftest user-can-read-other-user-team-profile-in-the-same-team
  []
  (async done
    (let [admin (h/admin-app)
          team (h/doc admin ["team-role" "team-id"])
          admin-role (h/doc team ["members" "user-id"])
          admin-other-user-team (h/doc admin ["user-team" "other-user-team-id"])
          app (h/authed-app #js {:uid "user-id"})
          user-team (h/doc app ["user-team" "other-user-team-id"])]
      (-> (.set team #js {:owner "owner-id"})
          (p/then #(.set admin-role #js {:role "member"}))
          (p/then #(.set admin-other-user-team #js {:team-id "team-id"
                                                    :uid "other-user-team-id"}))
          (p/then #(h/is-successfull (.get user-team) done))))))

(ns app.rules.dicks-spec
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

(deftest member-can-create-dick-for-other-team-member
  []
  (async done
    (let [admin (h/admin-app)
          team (h/doc admin ["team-role" "team-id"])
          admin-role (h/doc team ["members" "user-id"])
          admin-other-role (h/doc team ["members" "other-user-id"])
          app (h/authed-app #js {:uid "user-id"})
          dick (h/doc app ["dicks" "dick-id"])]
      (-> (.set team #js {:owner "owner-id"})
          (p/then #(.set admin-role #js {:role "member"}))
          (p/then #(.set admin-other-role #js {:role "member"}))
          (p/then #(h/is-successfull (.set dick (clj->js {:team-id "team-id"
                                                          :sender {:uid "user-id"}
                                                          :receiver {:uid "other-user-id"}})) done))))))

(deftest member-cant-create-dick-for-non-team-member
  []
  (async done
    (let [admin (h/admin-app)
          team (h/doc admin ["team-role" "team-id"])
          admin-role (h/doc team ["members" "user-id"])
          app (h/authed-app #js {:uid "user-id"})
          dick (h/doc app ["dicks" "dick-id"])]
      (-> (.set team #js {:owner "owner-id"})
          (p/then #(.set admin-role #js {:role "member"}))
          (p/then #(h/is-failed (.set dick (clj->js {:team-id "team-id"
                                                     :sender {:uid "user-id"}
                                                     :receiver {:uid "other-user-id"}})) done))))))

(deftest member-cant-create-dick-as-other-team-member
  []
  (async done
    (let [admin (h/admin-app)
          team (h/doc admin ["team-role" "team-id"])
          admin-role (h/doc team ["members" "user-id"])
          admin-other-role (h/doc team ["members" "other-user-id"])
          app (h/authed-app #js {:uid "user-id"})
          dick (h/doc app ["dicks" "dick-id"])]
      (-> (.set team #js {:owner "owner-id"})
          (p/then #(.set admin-role #js {:role "member"}))
          (p/then #(.set admin-other-role #js {:role "member"}))
          (p/then #(h/is-failed (.set dick (clj->js {:team-id "team-id"
                                                     :sender {:uid "other-user-id"}
                                                     :receiver {:uid "other-user-id"}})) done))))))

(deftest member-can-read-dicks-for-own-team
  []
  (async done
    (let [admin (h/admin-app)
          team (h/doc admin ["team-role" "team-id"])
          admin-role (h/doc team ["members" "user-id"])
          admin-dick (h/doc admin ["dicks" "dick-id"])
          app (h/authed-app #js {:uid "user-id"})
          dick (h/doc app ["dicks" "dick-id"])]
      (-> (.set team #js {:owner "owner-id"})
          (p/then #(.set admin-role #js {:role "member"}))
          (p/then #(.set admin-dick (clj->js {:team-id "team-id"
                                              :sender {:uid "user-id"}
                                              :receiver {:uid "other-user-id"}})))
          (p/then #(h/is-successfull (.get dick) done))))))


(ns cybernest-xd.system
  (:require [integrant.core :as ig]
            [io.pedestal.http :as server]
            ;; [reitit.pedestal :as pedestal]
            [io.pedestal.http.route :as route]
            [cybernest-xd.db :as db]
            [cybernest-xd.router :as router]))

(defn cybernest-xd-configuration [env]
  {:cybernest-xd/server {:env                     env
                         ::server/type            :jetty
                         ::server/port            8080
                         ::server/join?           false
                         ::server/routes          router/routes
                         ::server/resource-path   "public"
                         ::server/allowed-origins (constantly true)
                         ::server/secure-headers  {:content-security-policy-settings {:object-src "none"}}
                         ;; ::server/secure-headers  {:content-security-policy-settings {:default-src "'self'"
                         ;;                                                              :style-src   "'self' 'unsafe-inline'"
                         ;;                                                              :script-src  "'self' 'unsafe-inline'"}}
                         }})

(defmethod ig/init-key :cybernest-xd/db [_ db-spec]
  (println "Database Initiated...")
  (db/start-connection-pool db-spec))

(defmethod ig/halt-key! :cybernest-xd/db [_ datasource]
  (db/stop-connection-pool datasource))

;; TODO: How to pass in the
#_(defmethod ig/init-key :cybernest-xd/app [_ db]
    (println "Handlers loaded: " db)
    (router/create-app db))

(defmethod ig/init-key :cybernest-xd/server [_ config]
  (let [service-map config]
    {:server (-> service-map
                 ;; (server/default-interceptors)
                 ;; (pedestal/replace-last-interceptor router/routes)
                 ;; (server/dev-interceptors)
                 (server/create-server)
                 (server/start)
                 )}))


(defmethod ig/halt-key! :cybernest-xd/server [_ opts]
  (-> opts ;; TODO: I want to know exactly what gets passed in here. I assume it passes on
      :server
      server/stop))

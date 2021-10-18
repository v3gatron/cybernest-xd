(ns cybernest-xd.db
  (:require [next.jdbc :as jdbc]
            [next.jdbc.connection :as connection]
            [next.jdbc.sql :as sql]
            [next.jdbc.result-set :as rs]
            [honey.sql :as honeysql]
            [honey.sql.helpers :as hh]
            [io.pedestal.http :as http]
            ;; [hashp.core]
            [spyscope.core]
            [buddy.hashers :refer [encrypt check]])
  (:import (com.zaxxer.hikari HikariDataSource)))


;; -- Database Connection and Utitlies
(def db-spec {:dbtype "postgresql"
              :dbname "nest"
              :username "vega"
              :password "spiderdeus"})

(def datasource (jdbc/get-datasource db-spec))


(defn ok-response
  [context body]
  (assoc context :response {:status 200
                            :body body}))

(defn created-response
  [context body]
  (assoc context :response {:status 201
                            :body body}))

(defn deleted-response
  [context body]
  (assoc context :response {:status 204
                            :body body}))

(defn rejected-response
  [context body]
  (assoc context :response {:status 400
                            :body body}))

(defn start-connection-pool [db]
  (connection/->pool HikariDataSource db))

(defn stop-connection-pool [datasource]
  (.close datasource))

#_(jdbc/execute! datasource
                 ["create table architect(
                 id serial PRIMARY KEY,
                 handle VARCHAR(32),
                 email VARCHAR(40),
                 password VARCHAR(150))"])

(defn db-query [sql]
  (jdbc/execute! datasource sql
                 {:return-keys true
                  :builder-fn rs/as-unqualified-maps}))

(defn db-query-one [sql]
  (jdbc/execute-one! datasource sql
                   {:return-keys true
                    :builder-fn rs/as-unqualified-maps}))


(defn query-one! [query]
  (-> (honeysql/format query)
      db-query-one))

(defn query! [query]
  (-> (honeysql/format query)
      db-query))


(defn create-db-tables []
  (jdbc/execute! datasource ["CREATE TABLE IF NOT EXISTS architect(id UUID DEFAULT gen_random_uuid(),
                                                         handle VARCHAR(100) NOT NULL,
                                                         password VARCHAR(100) NOT NULL,
                                                         PRIMARY KEY (id))"])


  (jdbc/execute! datasource
                 ["create table if not exists iota(id UUID DEFAULT gen_random_uuid(),
                                                 architect_id INTEGER,
                                                 post VARCHAR(400),
                                                 created_at TIMESTAMP DEFAULT Now(),
                                                 PRIMARY KEY (id),
                                                 CONSTRAINT fk_architect
                                                     FOREIGN KEY(architect_id)
                                                        REFERENCES architect(id))"]))

(defn create-architect!
  "create-architect! - Create an architect/content creator"
  [{:keys [handle password]} ]

  (let [hashed-password     (encrypt password)
        architect           (->
                             (hh/insert-into :architect)
                             (hh/columns :handle :password)
                             (hh/values [[handle hashed-password]])
                             (honeysql/format)
                             db-query-one)
        sanitized-architect (dissoc architect :password)]
    sanitized-architect))


;; NOTE: Create iota and make sure it doesn't double ...done(what is the difference? query vs query-one?)
;; TODO: Remove iota then create one via pedestal route
;; NOTE: Do I want to do reitit or pedestal routes? Less is more...which in this case more may be less because I'll want to use reitit for front end routes?
;; TODO: Create Iota from cljs/ajax

;; -- 01-10-21 Scratch
#_(create-architect! {:handle "v3ga"
                      :password "hocus"
                      })                ; NOTE: This works, i think your issue deals with (query!/db-query-one)


#_(query-one! (create-iota {:architect_id 1 :post "lets see"} ))





;; NOTE: So here I have to decide if I should go interceptors or a regular handler. I don't think interceptors are necessary
(defn create-iota [{:keys [architect_id post]} ]
  (-> (hh/insert-into :iota)
      (hh/columns :architect_id :post)
      (hh/values [[architect_id post]])))


(def insert-iota
  {:name  ::insert-iota
   :enter (fn [ctx]
            (let [id 1
                  post (-> ctx :request :json-params :post)]
              ;; (#spy/p post)
              (query-one! (create-iota {:architect_id id :post post}))
              (println "THIS WENT THROUGH! # " post )
              ;; (#spy/p ctx)
              ))})

(defn get-all-iotas []
  (-> (hh/select :*)
      (hh/from :iota)))

(def find-all-iotas
  {:name ::find-all-iotas
   :enter
   (fn [context]
     (let [iota-return (sql/find-by-keys datasource :iota :all)]
       (ok-response context iota-return)))})

(def get-iotas
  {:name ::get-iotas
   :enter (fn [ctx]
            (query! (get-all-iotas)))})





(defn get-iota-by-id
  "get-book-by-id - Get book by ID"
  [{:keys [id]}]
  (-> (hh/select :*)
      (hh/from :book)
      (hh/where := :id id)))

#_(create-db-tables)

(defn drop-db-tables []
  (jdbc/execute! datasource ["drop table if exists iota"])
  (jdbc/execute! datasource ["drop table if exists architect cascade"]))
#_(drop-db-tables)




#_(defn create-db-tables []
  (jdbc/execute! db/datasource ["create table if not exists architect(id SERIAL NOT NULL PRIMARY KEY,
                                                         handle VARCHAR(100) NOT NULL,
                                                         password VARCHAR(100)  NOT NULL)"])


  (jdbc/execute! db/datasource
                 ["create table if not exists book(id SERIAL NOT NULL PRIMARY KEY,
                                                    title VARCHAR(1000)  NOT NULL,
                                                    author VARCHAR(100),
                                                    genre VARCHAR(50),
                                                    own BOOLEAN,
                                                    reading VARCHAR(10),
                                                    description TEXT,
                                                    pages INTEGER)"])


  (jdbc/execute! db/datasource
                 ["create table if not exists chamber(id SERIAL NOT NULL PRIMARY KEY,
                                                  architect_id INTEGER,
                                                  name VARCHAR(100) NOT NULL,
                                                  created_at TIMESTAMP DEFAULT Now(),
                                                  CONSTRAINT fk_architect
                                                     FOREIGN KEY(architect_id)
                                                        REFERENCES architect(id))"])

  (jdbc/execute! db/datasource
                 ["create table if not exists cube(id SERIAL NOT NULL PRIMARY KEY,
                                                  architect_id INTEGER,
                                                  chamber_id INTEGER,
                                                  name VARCHAR(100) NOT NULL,
                                                  created_at TIMESTAMP DEFAULT Now(),
                                                  CONSTRAINT fk_architect
                                                     FOREIGN KEY(architect_id)
                                                        REFERENCES architect(id),
                                                  CONSTRAINT fk_chamber
                                                       FOREIGN KEY(chamber_id)
                                                          REFERENCES chamber(id))"])
  (jdbc/execute! db/datasource
                 ["create table if not exists iota(id SERIAL NOT NULL PRIMARY KEY,
                                                 architect_id INTEGER,
                                                 chamber_id INTEGER,
                                                 cube_id INTEGER,
                                                 post VARCHAR(400),
                                                 created_at TIMESTAMP DEFAULT Now(),
                                                 CONSTRAINT fk_architect
                                                     FOREIGN KEY(architect_id)
                                                        REFERENCES architect(id),
                                                 CONSTRAINT fk_chamber
                                                       FOREIGN KEY(chamber_id)
                                                          REFERENCES chamber(id),
                                                 CONSTRAINT fk_cube
                                                       FOREIGN KEY(cube_id)
                                                          REFERENCES cube(id))"]))

#_(create-db-tables)

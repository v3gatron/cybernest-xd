(ns cybernest-xd.journal
  (:require [cybernest-xd.db :as db]
            [next.jdbc :as jdbc]
            [next.jdbc.sql :as sql]
            [next.jdbc.result-set :as rs]
            [honey.sql :as honeysql]
            [honey.sql.helpers :as hh]
            [jsonista.core :as jsonista]
            [ring.util.http-response :as response]
            ))
#_(jdbc/execute! db/datasource ["create table if not exists architect(id SERIAL NOT NULL PRIMARY KEY,
                                                         handle VARCHAR(100) NOT NULL,
                                                         password VARCHAR(100)  NOT NULL,
                                                         profile jsonb)"])
(defn create-db-tables []
  (jdbc/execute! db/datasource ["create table if not exists architect(id SERIAL NOT NULL PRIMARY KEY,
                                                         handle VARCHAR(100) NOT NULL,
                                                         password VARCHAR(100)  NOT NULL,
                                                         profile jsonb)"]
               )


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


(defn post-iota [{:keys [architect_id post]}]
  (-> (hh/insert-into :iota)
      (hh/columns :architect_id :post)
      (hh/values [[architect_id post]])))


(defn db-query [sql]
  (jdbc/execute! db/datasource sql
               {:return-keys true
                :builder-fn rs/as-unqualified-maps}))

(defn db-query-one [sql]
  (jdbc/execute-one! db/datasource sql
                   {:return-keys true
                    :builder-fn rs/as-unqualified-maps}))

(defn query-one! [query]
  (-> (honeysql/format query)
      db-query-one))

(defn query! [query]
  (-> (honeysql/format query)
      db-query))


(defn create-iota! [{:keys [params]}]
  (query! (post-iota params))
  (response/found "/")
  )

;; NOTE: My first post...
#_(query! (post-iota {:architect_id 1 :post "Is it working? I'm not sure what I'm feeling. Am I fighting loneliness, anger or frustration? I definitely don't fit in in this world... To be honest, that may not be a bad thing"}))

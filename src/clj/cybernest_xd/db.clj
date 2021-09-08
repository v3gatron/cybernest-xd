(ns cybernest-xd.db
  (:require [next.jdbc :as jdbc]
            [next.jdbc.connection :as connection])
  (:import (com.zaxxer.hikari HikariDataSource)))


(def db-spec {:dbtype "postgresql"
               :dbname "nest"
               :username "vega"
              :password "spiderdeus"})

;; (defn start-connection-pool [db-spec]
;;   (connection/->pool HikariDataSource db-spec))

(defn start-connection-pool [db]
  (connection/->pool HikariDataSource db))



(def datasource (jdbc/get-datasource db-spec))
(defn stop-connection-pool [datasource]
  (.close datasource))

#_(jdbc/execute! datasource
                 ["create table architect(
                 id serial PRIMARY KEY,
                 handle VARCHAR(32),
                 email VARCHAR(40),
                 password VARCHAR(150))"])

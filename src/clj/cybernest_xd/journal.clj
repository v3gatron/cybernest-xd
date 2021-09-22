(ns cybernest-xd.journal
  (:require [cybernest-xd.db :as db]
            [next.jdbc :as jdbc]
            [next.jdbc.sql :as sql]
            [next.jdbc.result-set :as rs]
            [honey.sql :as honeysql]
            [honey.sql.helpers :as hh]
            [jsonista.core :as jsonista]

            [ring.adapter.jetty :as jetty]
            [ring.util.http-response :as response]
            [ring.middleware.reload :refer [wrap-reload]]
            [buddy.hashers :refer [encrypt check]]
            ;; [hashp.core]
            [portal.api :as p]

            [reitit.ring :as ring]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [reitit.ring.coercion :as coercion]
            [reitit.coercion.spec]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.ring.middleware.exception :as exception]
            [reitit.ring.middleware.multipart :as multipart]
            [reitit.ring.middleware.parameters :as parameters]
            [muuntaja.core :as m]
            [hiccup.core :refer [html]]
            [hiccup.page :refer [html5 include-js include-css]]
            [hiccup.def]
            [hiccup.form]
            [hiccup.element :refer [link-to]])
   (:import [org.postgresql.util PGobject]
           [java.sql PreparedStatement]))


(defn head [title]
  [:head
   [:meta {:http-equiv "Content-Type" :content "text/html; charset=UTF-8"}]
   [:title title]
   (include-css "/css/style.css")])



(defn page [title & content]
  (html5 {:lang "en"}
         (head title)
         [:body
          [:div#app
           (include-js "/out/main.js")
           [:script "cybernest_xd.core.run()"]]]))


#_(p/open)

#_(p/close)
#_(p/tap)


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


(defn create-db-tables []
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



#_(create-architect! {:handle "v3ga"
                    :password "hocus"
                     })



#_(defn create-contact
  [{:keys [parameters]}]
  (let [data (:body parameters)
        created-id (db/insert-contact db/config data)]
    {:status 201
     :body (db/get-contact-by-id db/config created-id)}))


;; NOTE: So here I have to decide if I should go interceptors or a regular handler. I don't think interceptors are necessary
(defn create-iota! [{:keys [architect_id post]}]
  (-> (hh/insert-into :iota)
      (hh/columns :architect_id :post)
      (hh/values [[architect_id post]])))


(defn post-iota [{:keys [params]}]
  (query! (create-iota! params))
  {:status 201
   :body "post created"}
  ;; (response/found "/")
  )


#_(def insert-iota
    {:name ::insert-iota
     :enter
     (fn [context]
       (let [architect-id (-> context :request :json-params :architect_id)
             post         (-> context :request :json-params :post)
             ]
         (query! (post-iota {:architect_id architect-id :post post}))
         ))})


;; lets start a server here
(defn handler [_]
  {:status 200
   :body   (page "Cybernest&#8734xD" "ok 22 testing again")
   :headers {"Content-Type" "text/html"}})

(defn wrap [handler id]
  (fn [request]
    (update (handler request) :wrap (fnil conj '()) id)))

(def app
  (ring/ring-handler
   (ring/router
    [["/swagger.json"
      {:get {:no-doc                  true
             :swagger                 {:info     {:title "cybernest"}
                                       :basePath "/api"} ;; prefix for all paths
             :handler                 (swagger/create-swagger-handler)}}]
     ["/"
      {:get handler}]
     "/api"
     {:middleware [[wrap :api]]}
     ["/iota"
      {:swagger    {:tags ["iotas"]}
       :parameters {:body {:architect_id int?, :post string?}}
       ;; :get {:interceptors journal/post-iota}
       :post       {:handler create-iota!
                    ;; (fn [context]
                    ;;   (let [architect-id (-> context :request :json-params :architect_id)
                    ;;         post         (-> context :request :json-params :post)
                    ;;         ]
                    ;;     (create-iota! {:architect_id architect-id :post post})
                    ;;     ))
                    }}]
     ;; ["/iota"
     ;;  {:parameters {:architect_id int? :post string?}
     ;;   :post       {:handler create-iota!}}]
     ["/ping"
      {:get  handler
       :name ::ping}]
     ["/admin" {:middleware [[wrap :admin]]}
      ["/users" {:get  handler
                 :post handler}]]]

    {:data {:coercion   reitit.coercion.spec/coercion
            :muuntaja   m/instance
            :middleware [;; query-params & form-params
                         parameters/parameters-middleware
                         ;; content-negotiation
                         muuntaja/format-negotiate-middleware
                         ;; encoding response body
                         muuntaja/format-response-middleware
                         ;; exception handling
                         exception/exception-middleware
                         ;; decoding request body
                         muuntaja/format-request-middleware
                         ;; coercing response bodys
                         coercion/coerce-response-middleware
                         ;; coercing request parameters
                         coercion/coerce-request-middleware
                         ;; multipart
                         multipart/multipart-middleware]}})
   (ring/routes
    (swagger-ui/create-swagger-ui-handler {:path "/swagger"})
    (ring/create-resource-handler {:path "/" })
    (ring/create-default-handler))))





(defonce ^:private web-server (atom nil))

(defn create-server [port]
  (reset! web-server
          (jetty/run-jetty (wrap-reload #'app)  {:port port :join? false})))

(defn stop-server []
  (when @web-server
    (.stop @web-server))
  (reset! web-server nil))

(defn restart-server []
  (stop-server)
  (create-server 8888))

;; -- Interactive Play
#_(create-server 8888)
#_(stop-server)
#_(restart-server)

(add-tap #'p/submit)
(tap> (app {:request-method :get :uri "/swagger.json"}))




;; -- Scratch
#_(create-iota! [{:architect_id 1 :post "lets see"}] )

;; NOTE: My first post...
#_(query! (post-iota {:architect_id 1 :post "Is it working? I'm not sure what I'm feeling. Am I fighting loneliness, anger or frustration? I definitely don't fit in in this world... To be honest, that may not be a bad thing"}))

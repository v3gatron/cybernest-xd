(ns cybernest-xd.journal
  (:require [cybernest-xd.db :as db]
            [jsonista.core :as jsonista]
            [ring.adapter.jetty :as jetty]
            [ring.util.http-response :as response]
            [ring.middleware.reload :refer [wrap-reload]]

            [hashp.core]
            [portal.api :as p]
            [spyscope.core]

            [reitit.ring :as ring]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [reitit.ring.coercion :as ring-coercion]
            [reitit.coercion.malli]
            [reitit.coercion :as coercion]
            [reitit.coercion.spec]

            [reitit.ring.middleware.exception :as exception]
            [reitit.ring.middleware.multipart :as multipart]
            [reitit.ring.middleware.parameters :refer [parameters-middleware]]
            [reitit.ring.middleware.muuntaja :as muuntaja]

            [muuntaja.core :as m]
            [hiccup.core :refer [html]]
            [hiccup.page :refer [html5 include-js include-css]]
            [hiccup.def]
            [hiccup.form]
            [hiccup.element :refer [link-to]]
            [clj-http.client :as client])
  (:import [org.postgresql.util PGobject]
           [java.sql PreparedStatement]))

;; TODO: At some point you need to switch to http/2 Do it early so you don't get hit with issues
#_(#spy/p (client/get "http://localhost:8080"))
;; -- Web Interface --------------------------------------------

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
           ;; [:script "cybernest_xd.core.run()"]
           ]])) ;; NOTE: Firefox allows this even when run isn't called. chrome doesnt


(comment                              ; TODO: Make post work correctly
  ;; NOTE: INFO  io.pedestal.http.cors  - {:msg "cors request processing", :origin nil, :allowed true, :line 84} Execution error (ExceptionInfo) at slingshot.support/stack-trace (support.clj:201).
  (client/post "http://localhost:8080/iota" {:form-params  {:architect_id 1 :post "hello from clj-http, once more"}
                                             :content-type :json})
  (client/get "http://localhost:8080/iotas")
  )


;; lets start a server here
#_(defn handler [_]
  {:status 200
   :body   (page "Cybernest&#8734xD" "ok 22 testing again")
   :headers {"Content-Type" "text/html"}})

#_(defn wrap [handler id]
  (fn [request]
    (update (handler request) :wrap (fnil conj '()) id)))

#_(p/open)

#_(p/close)
#_(p/tap)

#_(add-tap #'p/submit)
#_(tap> (app {:request-method :get :uri "/swagger.json"}))

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
           [:script "cybernest_xd.core.run()"]]]))


(comment                                ; TODO: Make post work correctly
  ;; NOTE: INFO  io.pedestal.http.cors  - {:msg "cors request processing", :origin nil, :allowed true, :line 84} Execution error (ExceptionInfo) at slingshot.support/stack-trace (support.clj:201).
  (client/post "http://localhost:8080/iota" {:form-params  {:architect_id 1 :post "hello from clj-http"}
                                             :content-type :json})
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

#_(comment
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
     ;; ["/iota"
     ;;  {:swagger    {:tags ["iotas"]}
     ;;   :coercion reitit.coercion.malli/coercion
     ;;   :parameters {:path [:map
     ;;                       [:architect_id int]
     ;;                       [:post string?]]}

     ;;   :post       {:handler post-iota}}]

     ["/ping"
      {:get  handler
       :name ::ping}]
     ["/admin" {:middleware [[wrap :admin]]}
      ["/users" {:get  handler
                 :post handler}]]]

    {:compile coercion/compile-request-coercers
     :data {:coercion   reitit.coercion.spec/coercion
            :muuntaja   m/instance
            :middleware [ ;; query-params & form-params
                         parameters-middleware
                         ;; content-negotiation
                         muuntaja/format-negotiate-middleware
                         ;; encoding response body
                         muuntaja/format-response-middleware
                         ;; exception handling
                         exception/exception-middleware
                         ;; decoding request body
                         muuntaja/format-request-middleware
                         ;; coercing response bodys
                         ring-coercion/coerce-response-middleware
                         ;; coercing request parameters
                         ring-coercion/coerce-request-middleware
                         ;; multipart
                         multipart/multipart-middleware]}})
   (ring/routes
    (swagger-ui/create-swagger-ui-handler {:path "/swagger"})
    (ring/create-resource-handler {:path "/" })
    (ring/create-default-handler))))




(defonce ^:private web-server (atom nil))

(defn create-server [port]
  (reset! web-server
          (jetty/run-jetty (wrap-reload #'app)  {:port port :join? false}))) ;; NOTE: You want wrap-reload middleware with pedestal

(defn stop-server []
  (when @web-server
    (.stop @web-server))
  (reset! web-server nil))

(defn restart-server []
  (stop-server)
  (create-server 8888)))
;; -- Interactive Play
#_(create-server 8888)
#_(stop-server)
#_(restart-server)

#_(p/open)

#_(p/close)
#_(p/tap)

#_(add-tap #'p/submit)
#_(tap> (app {:request-method :get :uri "/swagger.json"}))

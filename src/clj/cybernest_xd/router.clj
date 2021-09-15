(ns cybernest-xd.router
  (:require [reitit.ring :as ring]
            [reitit.http :as http]
            [reitit.coercion.spec]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [reitit.http.coercion :as coercion]
            [reitit.dev.pretty :as pretty]
            [reitit.http.interceptors.parameters :as parameters]
            [reitit.http.interceptors.muuntaja :as muuntaja]
            [reitit.http.interceptors.exception :as exception]
            [reitit.http.interceptors.multipart :as multipart]
            [reitit.pedestal :as pedestal]

            [muuntaja.core :as m]
            [clojure.core.async :as a]
            [clojure.java.io :as io]
            [cybernest-xd.journal :as journal]
            ))

(defn interceptor [number]
  {:enter (fn [ctx] (a/go (update-in ctx [:request :number] (fnil + 0) number)))})

(defn hello-interceptor [request]
  {:status 200 :body "Hello, chrysalis"})

(def router
  (pedestal/routing-interceptor
   (http/router
    [["/swagger.json"
      {:get {:no-doc  true
             :swagger {:info {:title       "cybernest-xd: per aspera ad astra"
                              :description "my paracosmic playground"}}
             :handler (swagger/create-swagger-handler)}}]

     ["/"
      {:swagger {:tags ["basic"]}
       :get     {:interceptors [hello-interceptor]} }]

     ;; TODO: now how to make this work...
     ;; TODO: check what you're doing here... your swagger tags should be seperate and above allroutes under topic

     ["/iota"
      {:swagger {:tags ["iotas"]}
       :parameters {:body {:architect_id int?, :post string?}}
       ;; :get {:interceptors journal/post-iota}
       :post {:interceptors [journal/create-iota!]}
       }]]

    { ;:reitit.interceptor/transform dev/print-context-diffs ;; pretty context diffs
     ;;:validate spec/validate ;; enable spec validation for route data
     ;;:reitit.spec/wrap spell/closed ;; strict top-level validation
     :exception pretty/exception
     :data      {:coercion     reitit.coercion.spec/coercion
                 :muuntaja     m/instance
                 :interceptors [ ;; swagger feature
                                swagger/swagger-feature
                                ;; query-params & form-params
                                (parameters/parameters-interceptor)
                                ;; content-negotiation
                                (muuntaja/format-negotiate-interceptor)
                                ;; encoding response body
                                (muuntaja/format-response-interceptor)
                                ;; exception handling
                                (exception/exception-interceptor)
                                ;; decoding request body
                                (muuntaja/format-request-interceptor)
                                ;; coercing response bodys
                                (coercion/coerce-response-interceptor)
                                ;; coercing request parameters
                                (coercion/coerce-request-interceptor)
                                ;; multipart
                                (multipart/multipart-interceptor)]}})

   ;; optional default ring handler (if no routes have matched)
   (ring/routes
    (swagger-ui/create-swagger-ui-handler
     {:path   "/swagger"
      :config {:validatorUrl     nil
               :operationsSorter "alpha"}})
    (ring/create-resource-handler)
    (ring/create-default-handler))))

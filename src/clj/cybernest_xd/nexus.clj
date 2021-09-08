(ns cybernest-xd.nexus
  (:require [reitit.pedestal :as pedestal]
            [reitit.ring :as ring]
            [reitit.coercion.spec]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [reitit.http.coercion :as coercion]
            [reitit.dev.pretty :as pretty]
            [reitit.http.interceptors.parameters :as parameters]
            [reitit.http.interceptors.muuntaja :as muuntaja]
            [reitit.http.interceptors.exception :as exception]
            [reitit.http.interceptors.multipart :as multipart]
            [muuntaja.core :as m]
            [cybernest-xd.router :as router]))

#_(defn create-app [{:keys [db]}]
  (pedestal/routing-interceptor
   router/router
   (ring/routes
    (swagger-ui/create-swagger-ui-handler
     {:path "/"
      :config {:validatorUrl nil
               :operationSorter "alpha"}})
    (ring/create-resource-handler)
    (ring/create-default-handler))))

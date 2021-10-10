(ns cybernest-xd.core
  (:require [reagent.core :as r]
            [reagent.dom :as rdom]
            [portal.web :as p]
            [clojure.string :as str]
            [goog.string :as gstring]
            [cybernest-xd.util :refer [api-host]]
            ["react-dom" :as dom]
            [ajax.core :refer [GET POST]]
            [devtools.core :as devtools]
            ))
#_(devtools/install!)

(defn hello-component []
  [:div "Hello from Cybernest, ok cool"])

(defn app []
  (hello-component)
  )

(defn ^:export ^:dev/after-load render
  "Render the toplevel component for this app."
  []
  (rdom/render [app] (.getElementById js/document "app")))










;; (devtools.core/set-pref! :dont-detect-custom-formatters true)

#_(.log js/console (range 100))
;; (derive :counter :keechma/controller)



;; TODO: Create an input for iota post
;; TODO: call /iota/ POST handler



;;
;; (defn ^:export run []
;;    (init))

#_(GET "http://localhost:8080/iotas"
     {:handler (fn [response]
                 (.log js/console response))})

(ns cybernest-xd.core
  (:require [reagent.core :as r]
            [reagent.dom :as rdom]
            [portal.web :as p]
            [clojure.string :as str]
            [goog.string :as gstring]
            [cybernest-xd.util :refer [api-host]]
            [keechma.next.core :as k-core]
            [keechma.next.controller :as k-ctrl]
            [helix.core :refer [defnc $ <> provider]]
            [helix.dom :as d]
            [helix.hooks :as hooks]
            ["react-dom" :as dom]
            [ajax.core :refer [GET]]
            [devtools.core :as devtools]
            ))
#_(devtools/install!)
;; (devtools.core/set-pref! :dont-detect-custom-formatters true)

#_(.log js/console (range 100))
(derive :counter :keechma/controller)



;; TODO: Create an input for iota post
;; TODO: call /iota/ POST handler



(defnc app []
  (d/div (d/img {:src "images/chrysalisxd-grn.png"})))

;; (defn ^:dev/after-load render
;;   "Render the toplevel component for this app."
;;   []
;;   (rdom/render [content] (.getElementById js/document "app")))

(defn ^:dev/after-load init []
  (dom/render ($ app) (js/document.getElementById "app")))
;;
(defn ^:export run []
   (init))

#_(GET "http://localhost:8080/iotas"
     {:handler (fn [response]
                 (.log js/console response))})

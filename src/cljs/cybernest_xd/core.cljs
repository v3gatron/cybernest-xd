(ns cybernest-xd.core
  (:require [reagent.core :as r]
            [reagent.dom :as rdom]
            [portal.web :as p]
            [clojure.string :as str]
            [goog.string :as gstring]
            [cybernest-xd.utils :refer [api-host]]
            [keechma.next.core :as k-core]
            [keechma.next.controller :as k-ctrl]
            [helix.core :refer [defnc $ <> provider]]
            [helix.dom :as d]
            [helix.hooks :as hooks]
            ["react-dom" :as dom]
            ;; [ajax.core :refer [GET POST]]
            ))

(derive :counter :keechma/controller)



;; TODO: Create an input for iota post
;; TODO: call /iota/ POST handler


(defn hello []
  [:div (str "hello cybernest xd")])


(defn ok [])

(defn header []
  [:header
   [:div#logo (str "Cybernest -" (gstring/unescapeEntities "&#8734") "xD: Per Aspera Ad Astra")]])

(defn content []
  [:div
   (header)
   (hello)])

(defnc app []
  (d/div "hello cybernest from helix"))

;; (defn ^:dev/after-load render
;;   "Render the toplevel component for this app."
;;   []
;;   (rdom/render [content] (.getElementById js/document "app")))

(defn ^:dev/after-load init []
  (dom/render
   ($ app) (js/document.getElementById "app")))
;; (defn ^:export run []
;;   (render))

(ns cybernest-xd.core
  (:require [reagent.core :as r]
            [reagent.dom :as rdom]
            [portal.web :as p]
            [clojure.string :as str]
            [goog.string :as gstring]))



;; TODO: Create an input for iota post
;; TODO: call /iota/ POST handler


(defn hello []
  [:div (str "hello cybernest2")])

(defn header []
  [:header
   [:div#logo (str "Cybernest -" (gstring/unescapeEntities "&#8734") "xD: Per Aspera Ad Astra")]])

(defn content []
  [:div
   (header)
   (hello)])

(defn ^:dev/after-load render
  "Render the toplevel component for this app."
  []
  (rdom/render [content] (.getElementById js/document "app")))

(defn ^:export run []
  (render))

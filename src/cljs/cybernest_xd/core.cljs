(ns cybernest-xd.core
  (:require [reagent.core :as r]
            [reagent.dom :as rdom]
            [clojure.string :as str]))

(defn hello-cybernest [message]
  [:h4 message])

(defn content []
  [:div
   (hello-cybernest "this works, jose")])

(defn ^:dev/after-load render
  "Render the toplevel component for this app."
  []
  (rdom/render [content] (.getElementById js/document "app")))

(defn ^:export run []
  (render))

(ns cybernest-xd.view.style
    (:require [garden.core :refer [css]]
              [garden.def :as gdef]
              [garden.units :as gunit]
              [garden.stylesheet :as gstyle]
              [garden-watcher.def :as gwd]
              [garden-watcher.core :as gw]
              [clojure.string :as str]))

;; TODO: Stylesheet could not be loaded, firefox. Explained https://stackoverflow.com/questions/36989220/how-do-i-investigate-a-style-sheet-could-not-be-loaded-message-in-firefox

(gdef/defcssfn url)



;; Information Layer
(def bg "#000E0F")
(def bg1 "#0C1314")
(def bg2 "#090E0F")
(def bg3 "#0A0F1C")
(def fg1 "#eeeeee")

(def green1 "#4D764F")
(def green2 "#232f24")

(def green-dark1 "#39524A")
(def green-dark2 "#5E887A")
(def green-light1 "#6AAE95")
(def green-light2 "#64A68D")
(def green-light3 "#45CCB5")

;; -- Comlog Layer

;; -- Notification Layer
(def success-notification-bg green-light3)
(def error-notification-bg "#cf6679")
(def error-fg bg1)

(gwd/defstyles style

  (gstyle/at-import (url "https://fonts.googleapis.com/css2?family=Karla:wght@400;600&family=Roboto:wght@400;700&family=VT323&display=swap"))
  [:body {:background-color bg
          :color            fg1
          :font-family      "Futura"
          :line-height      "1.2"
          }]

  [:a {:text-decoration "none"
       :color           green-light1
       :font-weight     "bold"}]

  )

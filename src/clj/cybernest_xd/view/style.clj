(ns cybernest-xd.view.style
    (:require [garden.core :refer [css]]
              [garden.def :as gdef]
              [garden.units :as gunit]
              [garden.stylesheet :as gstyle]
              [garden.selectors :as $]
              [garden-watcher.def :as gwd]
              [garden-watcher.core :as gw]
              [clojure.string :as str]))

;; TODO: Stylesheet could not be loaded, firefox. Explained https://stackoverflow.com/questions/36989220/how-do-i-investigate-a-style-sheet-could-not-be-loaded-message-in-firefox

(gdef/defcssfn url)



(def chrysalis-theme {:bg "#000f10"
                      :bg-2 "#000B0C"
                      :electric-blue "#442de2"
                      :error-notification-bg "#cf6679"})

;; Information Layer
(def bg "#000f10")
(def chrysalis-bg1 "#0b0b12")

(def bg1 "#0C1314")
(def bg2 "#090E0F")
(def bg3 "#0A0F1C")
(def fg1 "#eeeeee")


(def green "#2e8b57")
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
(def error-fg bg)

;; (def cyber-bg2 "#0A0613")
;; (def cyber-bg "#0D0C11")
;; (def bgc "#0E0E12")

;; (def cyberscheme
;;   {:bg "#2B233E"
;;    :twilight-bg "#514371"})

(def cyber-bg1 "#0E0912")                     ; darkest
(def cyber-bg1 "#17171E")
(def cyber-bg2 "#0F0F14")
(def cyber-bg3 "#070709")               ; second darkest

(def chrysalis-bg3 "#060913")
(def chrysalis-bg4 "#0A0F1C")
(def cyber-orange "#992636" )
(def cyber-orange2 "#B83F4B")
(def cyber-orange3 "#B82D4C")
(def cyber-orange4 "#B84260")

(gwd/defstyles style

  (gstyle/at-import (url "https://fonts.googleapis.com/css2?family=Karla:ital,wght@1,700&family=Oxygen:wght@300&family=Roboto:ital,wght@0,400;0,500;1,400&display=swap"))
  [:body {:background-color bg
          :color            fg1
          :font-family      "Futura"    ;NOTE: Add roboto and see if there are some other replacements
          :line-height      "1.2"}]


  [:#ui {:display               "grid"
         :grid-gap              "15px"
         :max-width             "2400px"
         :grid-template-columns "1r 1fr 1fr"
         :grid-template-areas   [["'main-pane content-pane content-pane'"]]}]


  [:#main-pane {:grid-area "main-pane"}]


  [:#comlog-panel {:background-color "#000B0C"
                   :clear            "both"
                   :width            "100%"
                   :padding "8px;"
                   ;; :max-width        "700px"
                   }]

  [:#comlog-io-panel {:clear "both"}]


  [:.comlog-menu
   [:ul {:list-style-type  "inside"
         :padding          "0px"
         :background-color "#1a1a1a"}

    [:li {:display          "inline"
          :float            "left"
          :padding          "5px 10px 5px 10px"
          :background-color "#1a1a1a"
          :border           "3px outset #1c1c1c"}

     [:&:hover {:border "2px inset #1c1c1c"
                :color  "#2e8b57"}]

     [:&:last-child {:background-color "#442de2"}]

     [:&:last-child:hover {:background-color "#1a1a1a"
                           :color            "#442de2"}]

     [:a {:display "block"}]]]]


  [:#content-pane {:grid-area "content-pane"}]

  [:#header
   [:#logo
    [:h2 {:padding-top "0px"
          :margin-top "0px"
          :color "#3279FB"
          :font-family "Karla"
          :font-style "italic"}]]])

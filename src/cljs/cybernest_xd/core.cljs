(ns cybernest-xd.core
  (:require [reagent.core :as r]
            [reagent.dom :as rdom]
            [re-frame.core :as rf]
            [goog.dom :as gdom]
            [day8.re-frame.http-fx]
            [portal.web :as p]
            [clojure.string :as str]
            [goog.string :as gstring]
            [cybernest-xd.util :refer [api-host]]
            ["react-dom" :as dom]
            [ajax.core :as ajax]
            [devtools.core :as devtools]))


#_(devtools/install!)

;; (defonce app-state (atom {:books
;;                           [{:id 1 :title "Reamde" :author "Neal Stephenson"}
;;                            {:id 2 :title "Dune" :author "Frank Herbert"}
;;                            {:id 3 :title "Colorless Tsukuru Tazaki and His Years of Pilgrimage" :author "Haruki Murakami"}
;;                            {:id 4 :title "1Q84" :author "Haruki Murakami"}
;;                            {:id 5 :title "Rust for Rustaceans"}] }
;;                          {:iotas
;;                           [{:id 1 :post "First post, I should say something interesting here"}
;;                            {:id 2 :post "So I decided to make chrysalis as a thought dump and a remedy to saying too much on facbook. Call it a control thing, curiosity...the want to create. Privacy while wanting to be open? Who knows"}]}))


;; (@app-state)
;; (def x (get-in @app-state [2 :books]))


(defn iota-success [response]
  ;; NOTE: Hmmm a success response probably with
  (js/console.log response))


(defn sendith [fields]
  (let [{:keys [post]} @fields]
    (ajax/POST "/iota"
               {
                :params          {:post post}
                :handler         iota-success
                :format          (ajax/json-request-format)
                :response-format (ajax/json-response-format {:keywords? true})
                :error-handler   iota-success
                })))


(defn message-form []
  (let [initial-state {:post ""}
        fields        (r/atom initial-state)]
    (fn []
      [:div.block
       ;; [:p (:post @fields)]
       [:div.field
        [:div {:style {:border-bottom "1px solid #442de2"}}
         [:textarea
         {:type      :text
          :style {:width "100%"
                  :border "none"
                  :color "#fff"
                  :background-color "#000f10"} ; NOTE this shouldn't push the width of the entire grid out.
          :placeholder "What's on your mind..."
          :name      :post
          :value     (:post @fields)
          :on-change #(swap! fields
                             assoc :post (-> % .-target .-value))}]]

        [:br]
        [:input.button {:type     :submit
                        :on-click #(sendith fields)
                        :value    "Post"}]]])))




(defn comlog-section-panel-label [])

(defn session-management [])

;; -- Main Pane
;; This pane will hold all of the components on the left side of the interface, comlog, and things pertaining to books, etc
(defn header []
  [:div#header
   [:div#logo [:h3 "chrysalisXD"] ]])   ; NOTE: aura spectrum letters for logo and make a X out of like bands/rings

(defn menu-panel []
  [:div.comlog-menu
   [:ul
    [:li [:div "Articles"]]
    [:li [:div "Books"]]
    [:li [:div "Chambers"]]
    [:li [:div "Cubes"]]
    [:li [:div "Robots"]]]])

(defn comlog-io-panel []
  [:div
   [:div#comlog-io-panel "Comlog breadreadcrumbs placeholder"
    [message-form]]
   ]
  )

(defn comlog-panel []
  [:div#comlog-panel
   [:div (header)]
   [:div (menu-panel)]
   [:div (comlog-io-panel) ; NOTE: you only want this to show up if signed in. Otherwise you get navigation and search
    ] ])


;; -- Showcase Panel
(defn currently-reading-pane []
  [:div.currently-reading
   [:h4 "currently reading"]])

(defn reading-list-panel []
  [:div [:h4 "reading list"]])

(defn book-journal-posts-pane [])

(defn showcase-panel [])

(defn main-pane []
  [:div#main-pane
   [comlog-panel]
   [currently-reading-pane]
   [reading-list-panel]])


;; -- content-pane
;; This pane will hold the header/logo/breadcrumbs and the main timeline.  Also content, so individual articles/ chamber etc?


(defn disclaimer-pane [])

(defn content-pane []
  [:div#content-pane
   "timeline"])



(defn get-app-element []
  (gdom/getElement "app"))


(defn app []
  [:div#ui
   (main-pane)
   (content-pane)])

(defn mount [el]
  (rdom/render [app] el))

(defn mount-app-element []
  (when-let [el (.getElementById js/document "app")]
    (mount el)))

(mount-app-element)

(defn ^:after-load on-reload []
  (mount-app-element))


;; (defn ^:export ^:dev/after-load mount-root
;;   "Render the toplevel component for this app."
;;   []
;;   (when-let [el (rdom/render [app] (.getElementById js/document "app"))]
;;     (mount el)))













;; (devtools.core/set-pref! :dont-detect-custom-formatters true)

#_(.log js/console (range 100))
;; (derive :counter :keechma/controller)



;; TODO: Create an input for iota post
;; TODO: call /iota/ POST handler



;;
;; (defn ^:export run []
;;    (init))

#_(ajax/GET "http://localhost:8080/iotas"
     {:handler (fn [response]
                 (.log js/console response))})

(ns cybernest-xd.core
  (:require [reagent.core :as r]
            [reagent.dom :as rdom]
            [re-frame.core :as rf]
            [day8.re-frame.http-fx]
            [portal.web :as p]
            [clojure.string :as str]
            [goog.string :as gstring]
            [cybernest-xd.util :refer [api-host]]
            ["react-dom" :as dom]
            [ajax.core :as ajax]
            [devtools.core :as devtools]))


#_(devtools/install!)


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
       [:p (:post @fields)]
       [:div.field
        [:label.label {:for :post} "Post"]
        [:input.input
         {:type      :text
          :name      :post
          :value     (:post @fields)
          :on-change #(swap! fields
                             assoc :post (-> % .-target .-value))}]
        [:br]
        [:input.button {:type     :submit
                        :on-click #(sendith fields)
                        :value    "Submit"}]]])))




(defn comlog-section-panel-label [])
(defn logo [])
(defn session-management [])

;; -- Main Pane
;; This pane will hold all of the components on the left side of the interface, comlog, and things pertaining to books, etc
(defn menu-panel []
  [:div.comlog-menu
   [:ul
    [:li [:div "Articles"]]
    [:li [:div "Books"]]
    [:li [:div "Chambers"]]
    [:li [:div "Cubes"]]
    [:li [:div "Robots"]]]])

(defn comlog-panel []
  [:div#comlog-panel
   (menu-panel)])

;; -- Showcase Panel
(defn currently-reading-pane [])
(defn reading-list-pane [])
(defn book-journal-posts-pane [])

(defn showcase-panel [])

(defn main-pane []
  [:div#main-pane
   (comlog-panel)])


;; -- content-pane
;; This pane will hold the header/logo/breadcrumbs and the main timeline.  Also content, so individual articles/ chamber etc?
(defn header []
  [:div#header ])

(defn disclaimer-pane [])
(defn content-pane []
  [:div#content-pane "ha"])



;; (defn iota-post []
;;   [:div#post
;;    [:form {:method post :action "/iota"}
;;     []]])


(defn app []
  [:div#ui
   (main-pane)
   (content-pane)])

(defn ^:export ^:dev/after-load mount-root
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

#_(ajax/GET "http://localhost:8080/iotas"
     {:handler (fn [response]
                 (.log js/console response))})

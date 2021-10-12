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
            [devtools.core :as devtools]
            ))
#_(devtools/install!)
#_(rf/reg-event-fx
 ::iota-post
 (fn [_ [_ ctx]]
   {:http-xhrio {:method :post
                 :uri "http://localhost:8080/iota"
                 :params data
                 :timeout 5000
                 :format (ajax/json-response-format {:keywords? true})
                 :on-success [:good-http-result]
                 :on-failure [:bad-http-result]}}))

(defn send-message! [fields]
  (ajax/POST "/iota"
             {:format :json
              :params @fields
              :handler #(.log js/console (str "response: " %))
              :error-handler #(.error js/console (str "error: " %))})) ; NOTE: :format :json is what I wanted. I do see the id isn't passing through

(defn message-form []
  (let [fields (r/atom {})]
    (fn []
      [:div
       [:p (:id @fields)]
       [:p (:post @fields)]
       [:div.field
        [:label.label {:for :id} "ID"]
        [:input.input
         {:type :number
          :name :architect_id
          :on-change #(swap! fields assoc :id (-> % .-target .-value))
          :value (:id @fields)}]]

       [:div.field
        [:label.label {:for :post} "Post"]
        [:input.input
         {:name :post
          :value (:post @fields)
          :on-change #(swap! fields
                             assoc :post (-> % .-target .-value))}]]
       [:input.button {:type :submit
                       :on-click #(send-message! fields)
                       :value "iota"}]])))

  (defn hello-component []
    [:div "Hello from Cybernest, ok cool"])


(defn comlog-section-panel-label [])
(defn logo [])
(defn session-management [])

(defn header []
  [:div#header ])

(defn content [])

;; -- Showcase Panel
(defn menu-pane [])
(defn comlog-pane [])

(defn currently-reading-pane [])
(defn reading-list-pane [])
(defn book-journal-posts-pane [])

(defn showcase-panel [])



;; -- Grand Station Panel
(defn disclaimer-pane [])
(defn grandstation-panel [])

;; (defn iota-post []
;;   [:div#post
;;    [:form {:method post :action "/iota"}
;;     []]])


(defn app []
  (hello-component)
  (message-form)
  )

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

#_(GET "http://localhost:8080/iotas"
     {:handler (fn [response]
                 (.log js/console response))})

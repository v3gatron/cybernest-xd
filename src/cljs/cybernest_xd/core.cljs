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





(defn send-message2! [fields]
  (ajax/POST "/iota"
             (.log js/console (str @fields))
             {:format        :json
              :params        @fields
              :handler       #(.log js/console (str "response: " %))
              :error-handler #(.error js/console (str "error: " %))}))

(defn send-message! [fields]
;  (.log js/console @fields)
  (-> (js/fetch "/iota"
                #js{:method  "POST"
                    :headers #js {"content-type" "application/json"}
                    :body    (js/JSON.stringify @fields) })
                (.then (fn [res] (.json res)))
                (.then #(.log js/console (str "response: " %)))
                (.catch #(.error js/console (str "error: " %)))))




(defn iota-success [response]
  (js/console.log response)
  )


(defn sendith [fields]
  (let [{:keys [post]} @fields]
    (ajax/POST "/iota"
               {
                :params          {:post post}
                :handler         iota-success
                :format          (ajax/json-request-format)
                :response-format (ajax/json-response-format {:keywords? true})
                :error-handler   iota-success
                })
    )
  )
                                        ; NOTE: it sent this as raw data ["^ ","~:post","ok now"]
;; NOTE: I can;t believe it fucking worked and I don't know which one....


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
  (message-form))

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

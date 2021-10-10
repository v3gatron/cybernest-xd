(ns user
  (:require [integrant.repl :as ig-repl]
            [cemerick.pomegranate :only (add-dependencies)]
            [garden-watcher.core :as garden-watch]
            [cybernest-xd.system :as system]))

(ig-repl/set-prep! (constantly (system/cybernest-xd-configuration :dev)))

(def go ig-repl/go)
(def halt ig-repl/halt)
(def reset ig-repl/reset)

#_(def watcher ^{:garden {:output-to "resources/public/assets/style.css"}} (garden-watch/start-garden-watcher! '[cybernest-xd.view.style]))
#_(garden-watch/stop-garden-watcher! watcher)
#_(garden-watch/compile-garden-namespaces ^{:garden {:output-to "resources/public/assets/style.css"}} '[cybernest-xd.view.style])

(defn add-dep
  [coords]
  (cemerick.pomegranate/add-dependencies
   :coordinates coords
   :repositories (merge cemerick.pomegranate.aether/maven-central
                        {"clojars" "https://clojars.org/repo"})))

(comment
  (add-dep '[[kee-frame "1.1.2"]]))

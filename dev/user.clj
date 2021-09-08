(ns user
  (:require [integrant.repl :as ig-repl]

            [cybernest-xd.system :as system]
            ))

(ig-repl/set-prep! (constantly (system/cybernest-xd-configuration :dev)))

(def go ig-repl/go)
(def halt ig-repl/halt)
(def reset ig-repl/reset)

(ns caves.core
  (:use [caves.ui.core :only [->UI]]
        [caves.ui.drawing :only [draw-game]]
        [caves.ui.input :only [get-input process-input]]
        [caves.entities.core :only [tick]])
  (:require [lanterna.screen :as s]))

(defrecord Game [world uis input])

(defn tick-entity [world entity]
  (tick entity world))

(defn tick-all [world]
  (reduce tick-entity world (vals (:entities world))))

(defn run-game [game screen]
  (loop [{:keys [input uis] :as game} game]
    (when (seq uis)
      (draw-game game screen)
      (if (nil? input)
        (let [game (update-in game [:world] tick-all)]
          (draw-game game screen)
          (recur (get-input game screen)))
        (recur (process-input (dissoc game :input) input))))))

(defn new-game []
  (->Game nil [(->UI :start)] nil))

(defn main
  "Main entry point which handles different screen types" 
  ([] (main :swing false))
  ([screen-type] (main screen-type false))
  ([screen-type block?]
   (letfn [(go []
             (let [screen (s/get-screen screen-type)]
               (s/in-screen screen
                            (run-game (new-game) screen))))]
     (if block?
       (go)
       (future (go))))))

(defn -main
  "Bootstrap main"
  [& args]
  (let [args (set args)
        screen-type (cond
                      (args ":swing") :swing
                      (args ":text")  :text
                      :else           :auto)]
    (main screen-type true)))

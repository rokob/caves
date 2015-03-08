(ns caves.world)

(def world-size [160 50])

(defrecord World [tiles])
(defrecord Tile [kind glyph color])

(def tiles
  {:floor (->Tile :floor "." :white)
   :wall  (->Tile :wall  "#" :white)
   :bound (->Tile :bound "X" :black)})

(defn get-tile [tiles x y]
  (get-in tiles [y x] (:bound tiles)))

(defn random-tiles []
  (let [[cols rows] world-size]
    (letfn [(random-tile []
              (tiles (rand-nth [:floor :wall])))
            (random-row []
              (vec (repeatedly cols random-tile)))]
      (vec (repeatedly rows random-row)))))

(defn print-row [row]
  (println (apply str (map :glyph row))))

(defn print-world [world]
  (dorun (map print-row (:tiles world))))

(defn block-coords [x y]
  (for [dx [-1 0 1]
        dy [-1 0 1]]
    [(+ x dx) (+ y dy)]))

(defn get-block [tiles x y]
  (map (fn [[x y]]
         (get-tile tiles x y))
       (block-coords x y)))

(defn get-smoothed-tile [block]
  (let [tile-counts (frequencies (map :kind block))
        floor-threshold 5
        floor-count (get tile-counts :floor 0)
        result (if (>= floor-count floor-threshold)
                 :floor
                 :wall)]
    (tiles result)))

(defn get-smoothed-row [tiles y]
  (mapv #(get-smoothed-tile (get-block tiles % y))
        (range (count (first tiles)))))

(defn get-smoothed-tiles [tiles]
  (mapv #(get-smoothed-row tiles %)
        (range (count tiles))))

(defn smooth-world [{:keys [tiles] :as world}]
  (assoc world :tiles (get-smoothed-tiles tiles)))

(defn random-world []
  (let [world (->World (random-tiles))
        world (nth (iterate smooth-world world) 0)]
    world))

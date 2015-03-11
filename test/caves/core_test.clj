(ns caves.core-test
  (:require [clojure.test :refer :all]
            [caves.core :refer :all])
  (:use [caves.ui.core :only [->UI]]
        [caves.ui.input :only [process-input]]))

(defn current-ui [game]
  (:kind (last (:uis game))))

(deftest test-start
  (let [game (->Game nil [(->UI :start)] nil)]

    (testing "Enter takes you to play at the starting screen."
      (let [result (process-input game :enter)]
        (is (= (current-ui result) :play))))

    (testing "Other keys takes you play at the starting screen."
      (let [results (map (partial process-input game)
                         [\space \a \A :escape :up :backspace])]
        (doseq [result results]
          (is (= (current-ui result) :play)))))))


(defproject caves "0.1.0-SNAPSHOT"
  :description "Caves: Keep looking"
  :url "https://www.github.com/rokob/caves"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [clojure-lanterna "0.9.0"]]
  :main caves.core
  :profiles {:uberjar {:aot :all}})
  ;:main ^:skip-aot caves.core

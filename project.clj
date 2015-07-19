(defproject cambrian "0.1.0"
  :description "cambrian"
  :url "http://openlab.caiyunapp.com/cambrian"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :global-vars  {*warn-on-reflection* true}

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [selmer "0.8.5"]
                 [com.taoensso/timbre "4.0.2"]
                 [com.taoensso/tower "3.0.2"]
                 [markdown-clj "0.9.67"]
                 [environ "1.0.0"]
                 [compojure "1.4.0"]
                 [ring/ring-defaults "0.1.5"]
                 [ring/ring-session-timeout "0.1.0"]
                 [ring "1.4.0" :exclusions [ring/ring-jetty-adapter]]
                 [metosin/ring-middleware-format "0.6.0"]
                 [metosin/ring-http-response "0.6.3"]
                 [bouncer "0.3.3"]
                 [prone "0.8.2"]
                 [org.clojure/tools.nrepl "0.2.10"]
                 [migratus "0.8.2"]
                 [org.clojure/java.jdbc "0.3.7"]
                 [instaparse "1.4.1"]
                 [yesql "0.5.0-rc3"]
                 [to-jdbc-uri "0.1.0"]
                 [com.h2database/h2 "1.4.187"]
                 [org.clojure/clojurescript "0.0-3308" :scope "provided"]
                 [org.clojure/tools.reader "0.9.2"]
                 [reagent "0.5.0"]
                 [cljsjs/react "0.13.3-1"]
                 [reagent-forms "0.5.4"]
                 [reagent-utils "0.1.5"]
                 [secretary "1.2.3"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [cljs-ajax "0.3.13"]
                 [metosin/compojure-api "0.22.0"]
                 [metosin/ring-swagger-ui "2.1.1-M2"]
                 [http-kit "2.1.19"]
                 [clojurewerkz/elastisch "2.1.0"]
                 [org.elasticsearch/elasticsearch "1.5.1"]
                 [com.vividsolutions/jts "1.13"]
                 [net.mikera/vectorz-clj "0.29.0"]
                 [net.mikera/vectorz "0.46.0"]
                 [net.mikera/core.matrix "0.34.0"]
                 [net.mikera/imagez "0.5.0"]
                 [com.climate/claypoole "1.0.0"]
                 [org.clojure/data.codec "0.1.0"]
                 [org.apache.commons/commons-math3 "3.4.1"]
                 [org.apache.commons/commons-pool2 "2.3"]
                 [org.clojure/tools.logging "0.3.1"]
                 [net.java.dev.jna/jna "4.1.0"]
                 [cheshire "5.4.0"]
                 [org.slf4j/slf4j-api "1.7.12"]
                 [org.slf4j/slf4j-log4j12 "1.7.12"]
                 [log4j/log4j "1.2.17"]]

  :min-lein-version "2.0.0"
  :source-paths ["src/clj"]
  :java-source-paths ["src/java"]  ; Java source is stored separately.
  :test-paths ["test/clj"]
  :uberjar-name "cambrian.jar"
  :jvm-opts ["-server"]

  :main cambrian.core
  :migratus {:store :database}

  :plugins [[lein-environ "1.0.0"]
            [lein-ancient "0.6.5"]
            [migratus-lein "0.1.5"]
            [lein-cljsbuild "1.0.6"]]
  :clean-targets ^{:protect false} ["resources/public/js"]
  :cljsbuild
  {:builds
   {:app
    {:source-paths ["src/cljs"]
     :compiler
                   {:output-dir "resources/public/js/out"
                    :externs ["react/externs/react.js"]
                    :optimizations :none
                    :output-to "resources/public/js/app.js"
                    :pretty-print true}}}}

  :profiles
  {:uberjar {:omit-source true
             :env {:production true}
             :hooks [leiningen.cljsbuild]
             :cljsbuild
                          {:jar true
                           :builds
                                {:app
                                 {:source-paths ["env/prod/cljs"]
                                  :compiler {:optimizations :advanced :pretty-print false}}}}

             :aot :all}
   :dev           [:project/dev :profiles/dev]
   :test          [:project/test :profiles/test]
   :project/dev  {:dependencies [[ring/ring-mock "0.2.0"]
                                 [ring/ring-devel "1.4.0"]
                                 [pjstadig/humane-test-output "0.7.0"]
                                 [org.clojure/tools.nrepl "0.2.10"]
                                 [lein-figwheel "0.3.7"]
                                 [mvxcvi/puget "0.8.1"]
                                 [junit/junit "4.12"]]

                  :junit   ["test/java"]
                  :plugins [[lein-figwheel "0.3.7"]
                            [lein-junit "1.1.8"]]
                  :cljsbuild
                                {:builds
                                 {:app
                                  {:source-paths ["env/dev/cljs"] :compiler {:source-map true}}}}

                  :figwheel
                                {:http-server-root "public"
                                 :server-port 3449
                                 :nrepl-port 7002
                                 :css-dirs ["resources/public/css"]
                                 :ring-handler cambrian.handler/app}

                  :repl-options {:init-ns cambrian.core}
                  :injections [(require 'pjstadig.humane-test-output)
                               (pjstadig.humane-test-output/activate!)]
                  ;;when :nrepl-port is set the application starts the nREPL server on load
                  :env {:dev        true
                        :port       3000
                        :nrepl-port 7000}}
   :project/test {:env {:test       true
                        :port       3001
                        :nrepl-port 7001}}})

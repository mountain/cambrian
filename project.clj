(defproject cambrian "0.1.0"
  :description "cambrian"
  :url "http://openlab.caiyunapp.com/cambrian"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :global-vars  {*warn-on-reflection* true}
  :plugins      [[lein-expectations "0.0.7"]
                 [lein-junit "1.1.8"]]

  :dependencies [[org.clojure/clojure "1.7.0-RC1"]
                 [org.codehaus.groovy/groovy-all "2.4.3"]
                 [http-kit "2.1.19"]
                 [ring "1.3.2"]
                 [compojure "1.3.3"]
                 [ring/ring-defaults "0.1.4"]
                 [ring/ring-headers "0.1.2"]
                 [ring/ring-ssl "0.2.1"]
                 [ring/ring-codec "1.0.0"]
                 [ring/ring-json "0.3.1"]
                 [ring.middleware.jsonp "0.1.6"]
                 [org.clojure/core.cache "0.6.4"]
                 [org.clojure/core.memoize "0.5.7"]
                 [clojurewerkz/elastisch "2.1.0"]
                 [org.elasticsearch/elasticsearch "1.5.1"]
                 [com.vividsolutions/jts "1.13"]
                 [org.osgeo/proj4j "0.1.0"]
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
                 [log4j/log4j "1.2.17"]
                 [junit/junit "4.12"]
                 [expectations "2.0.9"]]

  :uberjar-merge-with {"META-INF/services/org.apache.lucene.codecs.PostingsFormat" [slurp str spit]
                       "META-INF/services/org.apache.lucene.codecs.DocValuesFormat" [slurp str spit]
                       "META-INF/services/org.apache.lucene.codecs.Codec" [slurp str spit]
                       "META-INF/services/org.apache.lucene.analysis.util.CharFilterFactory" [slurp str spit]
                       "META-INF/services/org.apache.lucene.analysis.util.TokenFilterFactory" [slurp str spit]
                       "META-INF/services/org.apache.lucene.analysis.util.TokenizerFactory" [slurp str spit]}

  :jvm-opts ["-Xms1g" "-Xmx2g" "-server"]
  :javac-options ["-target" "1.7" "-source" "1.7" "-Xlint:-options"]
  :omit-source true

  :test-paths ["tests/clojure"]
  :source-paths ["src/clojure"]
  :java-source-paths ["src/java"]
  :junit ["tests/java"]

  :jar-name "cambrian.jar"
  :uberjar-name "cambrian-standalone.jar"

  :aot :all
  :main cambrian.main)

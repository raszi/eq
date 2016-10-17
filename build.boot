(def project 'eq)

(set-env! :resource-paths #{"src"}
          :source-paths   #{"test"}
          :dependencies   '[[org.clojure/clojure "1.8.0"]
                            [org.clojure/tools.cli "0.3.5"]
                            [mvxcvi/puget "1.0.1"]
                            [degree9/boot-semver "1.3.6" :scope "test"]
                            [adzerk/boot-test "1.1.2" :scope "test"]
                            [tolitius/boot-check "0.1.3" :scope "test"]])

(require '[adzerk.boot-test :refer [test]]
         '[degree9.boot-semver :refer :all]
         '[tolitius.boot-check :as check])

(task-options!
 aot {:namespace #{'eq.cli}}
 pom {:project     project
      :version     (get-version)
      :description "edn query"
      :url         "https://github.com/raszi/eq"
      :scm         {:url "https://github.com/raszi/eq"}
      :license     {"Eclipse Public License"
                    "http://www.eclipse.org/legal/epl-v10.html"}}
 jar {:main 'eq.cli
      :file (str "eq-" (get-version) ".jar")})

(deftask check-sources []
  ;; (set-env! :source-paths #{"src" "test"})
  (comp
   (check/with-yagni :options {:entry-points ["eq.cli/-main"]})
   (check/with-eastwood)
   (check/with-kibit)
   (check/with-bikeshed)))

(deftask build
  "Build the project locally as a JAR."
  [d dir PATH #{str} "the set of directories to write to (target)."]
  (let [dir (if (seq dir) dir #{"target"})]
    (comp
     (aot)
     (version :include true)
     (pom)
     (uber)
     (jar)
     (target :dir dir))))

(deftask run
  "Run the project."
  [a args ARG [str] "the arguments for the application."]
  (require '[eq.cli :as app])
  ((resolve 'app/run) args println)
  identity)

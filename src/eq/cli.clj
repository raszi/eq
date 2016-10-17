(ns eq.cli
  (:gen-class)
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.tools.cli :as cli]
            [eq.core :as core])
  (:import java.util.Properties))

(def cli-options
  [["-C" "--color-output" "Print the output colorized"
    :default true]
   ["-M" "--monochrome-output" "Print the output in monochrome mode"
    :default false]
   ["-V" "--version" "Print out the version"]
   ["-h" "--help"]])

(defn- usage [options-summary]
  (str/join
   \newline
   [""
    "Usage: eq [options...] filter [files...]"
    ""
    "Options:"
    options-summary
    ""
    "Filter:"
    "  :key     Fetch value from a map"
    ""]))

(defn- error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (str/join \newline errors)))

(defn- exit
  ([status] (exit status nil))
  ([status msg]
   (when msg (println msg))
   (System/exit status)))

(defn- version
  ([] (version "version.properties"))
  ([file]
   (get (doto (java.util.Properties.)
          (.load ^java.io.Reader (io/reader file)))
        "VERSION")))

(defn- filter-fn [options filter]
  (let [filter-fn (core/filter-fn filter)]
    (fn [data]
      (core/pretty-print (filter-fn data) options))))

(defn- execute [filter-fn files]
  (if (seq files)
    (doall (map (comp filter-fn core/read-file) files))
    (filter-fn (core/read-stdin)))
  nil)

(defn- run [args exit-fn]
  (let [{:keys [options errors summary] [filter & files] :arguments}
        (cli/parse-opts args cli-options)]
    (cond
      (:help options)    (exit-fn 0 (usage summary))
      (:version options) (exit-fn 0 (str "eq-" (version)))
      errors             (exit-fn 1 (error-msg errors))
      (nil? filter)      (exit-fn 1 (usage summary))
      :else              (exit-fn 0 (execute (filter-fn options filter) files)))))

(defn -main [& args]
  (run args exit))

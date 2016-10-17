(ns eq.core
  (:refer-clojure :exclude [filter])
  (:require [clojure.edn :as edn]
            [puget.printer :as puget]
            [clojure.java.io :as io]))

(defn read-stdin []
  (edn/read))

(defn read-file [file]
  (with-open [in (java.io.PushbackReader. (io/reader file))]
    (edn/read in)))

(defn filter-fn [filter]
  (-> (format "(fn [data] (-> data %s))" filter)
      (read-string)
      (eval)))

(defn filter [data [filter]]
  (if filter
    ((filter-fn filter) data)
    data))

(defn- colored? [options]
  ;; http://bugs.java.com/bugdatabase/view_bug.do?bug_id=4099017
  ;; (System/console)
  (if (options :monochrome-output)
    false
    (options :color-output)))

(defn pretty-print [data options]
  (puget/pprint data {:print-color (colored? options)}))

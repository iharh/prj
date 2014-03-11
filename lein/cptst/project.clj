(defproject cptst "1.0.0-SNAPSHOT"

  :description "Concept stress test"

  ; TODO: need to avoid caching at "C:\Documents and Settings\Ihar_Hancharenka\.m2"

  :repositories
  {
;    "terracotta-release"  "http://www.terracotta.org/download/reflector/releases"
;    "sonatype-sf-release" "http://oss.sonatype.org/content/repositories/sourceforge-releases"
;    "jboss-release"       "http://repository.jboss.org/maven2"
  }


  :dependencies
  [
    [org.clojure/clojure         "1.3.0"]
    [clj-webdriver/clj-webdriver "0.5.0-alpha5"]
;    [org.clojure/clojure-contrib "1.2.0"]
  ]

  :dev-dependencies
  [
;    [org.clojars.scott/lein-nailgun "1.1.0"]
;    [org.clojars.ibdknox/lein-nailgun "1.1.1"]
;    [org.clojars.gfodor/lein-nailgun "1.1.0"]
;    [vimclojure/server ""]
  ]

  :main cptst.core

  ;:run-aliases {
  ;  :s cptst.core/-main ; "http://www.slideshare.net"
  ;}
)

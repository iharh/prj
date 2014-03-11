(ns cptst.core
  (:gen-class)

  (:import
   (java.io File))

  (:require [clj-webdriver.cache :as cache])

  (use clj-webdriver.core)
  (use clj-webdriver.form-helpers))


(defn- print-page-src
  [page-url]
  (let [b (start {:browser :firefox } page-url)] ; :cache-spec {:strategy :basic}
    (println "1 - " (title b))

    ;(-> b
    ;  (find-it {:class "text" :name "q"})
    ;  (input-text "Consona"))
    ;(-> b
    ;  (find-it {:value "Search"}) ;:class "ctaNormal" -- use regular expressions
    ;  click)

    ; (cache/seed b)
    ; (quick-fill b [])


    ; org.openqa.selenium.StaleElementReferenceException:
    ; Element not found in the cache - perharps the page has changed since it was looked up
    ; For documentation on this error, please visit: http://seleniumhq.org/exceptions/stale_element_reference.html
    (quick-fill-submit b [{{:value "Search" :class "ctaNormal"} click}
                          {{:class "text" :name "q"} "Consona"}])

    (println "3 - hello")

    (println "4 - " (current-url b))

    ; (close b) ; use quit for close all the windows and quit
    ))

(defn -main
  [& args]
  (. System exit

  (if (nil? args)
      (do (println "starting URL not specified!!!") 1)
      (do (print-page-src (first args)) 0))))


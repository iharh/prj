(ns myprj.test.core
	(:use [myprj.core] :reload)
	(:use [clojure.test])
)

;; (deftest replace-me ;; FIXME: write
;;  (is false "No tests have been written."))

(deftest simple-test
	(is (= (hello) "Hello world!"))
	(is (= (hello "test") "Hello test!"))
)

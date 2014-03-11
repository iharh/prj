(ns myprj.core)

(defn hello
	([] "Hello world!")
	([n1] (str "Hello " n1 "!"))
	([n1 n2] (str "Hello " n1 "-" n2 "!"))
)

(defn add2 [p1]
  (+ p1 2)
)

(defn add3 [p1]
  (+ p1 3)
)

(defn add5 [p1]
  (add3 (add2 p1))
)

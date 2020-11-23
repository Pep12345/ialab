;  ---------------------------------------------
;  --- Definizione del modulo e dei template ---
;  ---------------------------------------------
(defmodule AGENT (import MAIN ?ALL) (import ENV ?ALL) (export ?ALL))

- definire 7 regole per k-cell (usare anche i k-row, k-col)[water,middle.....]
- definire template b-cell
- scegliere se dividere in moduli declaration - executation
- scegliere euristiche da usare quando non abbiamo più k-cell

//prova regola
(defrule k-cell-water
		(status (step ?s)(currently running))  // forse bisogna incrementare step
		(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c water)))
	=>
		// non dovrebbe fare nulla
	     (pop-focus)
)


(defrule k-cell-left
		(status (step ?s)(currently running))  // forse bisogna incrementare step
		(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c left)))
		//controllare che la casella a fianco non sia k-cell altrimenti non deve fare nulla
	=>
		// creare b-cell water in [x+1,y] // [x-1,y] // [x,y-1] // [x-1,y-1] // [x+1,y-1]
		// decrementare k-row/k-col di 1 (forse conviene usare b-row/b-col) - controllare step precedende se fire per assicurarsi di non decrementare due volte
		(assert (exec (step ?s) (action guess) (x ?x) (y (+ ?y 1))) // andiamo a mettere bandierina nella casella a fianco
			 (pop-focus)
)
(defrule k-cell-middle
		(status (step ?s)(currently running))  // forse bisogna incrementare step
		(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c middle)))
	=>
	 (pop-focus)
)



(defrule inerzia0 (declare (salience 10))
	(status (step ?s)(currently running))
	(moves (fires 0) (guesses ?ng&:(> ?ng 0)))
=>
	(assert (exec (step ?s) (action guess) (x 0) (y 0)))
     (pop-focus)

)

(defrule inerzia0-bis (declare (salience 10))
	(status (step ?s)(currently running))
	(moves (guesses 0))
=>
	(assert (exec (step ?s) (action unguess) (x 0) (y 0)))
     (pop-focus)

)



(defrule inerzia
	(status (step ?s)(currently running))
	(not (exec  (action fire) (x 2) (y 4)) )
=>
	(assert (exec (step ?s) (action fire) (x 2) (y 4)))
     (pop-focus)

)

(defrule inerzia1
	(status (step ?s)(currently running))
	(not (exec  (action fire) (x 2) (y 5)))
=>


	(assert (exec (step ?s) (action fire) (x 2) (y 5)))
     (pop-focus)

)

(defrule inerzia2
	(status (step ?s)(currently running))
	(not (exec  (action fire) (x 2) (y 6)))

=>

	(assert (exec (step ?s) (action fire) (x 2) (y 6)))
     (pop-focus)

)

(defrule inerzia3
	(status (step ?s)(currently running))
	(not (exec  (action fire) (x 1) (y 2)))

=>
	(assert (exec (step ?s) (action fire) (x 1) (y 2)))
     (pop-focus)
)


(defrule inerzia4
	(status (step ?s)(currently running))
	(not (exec (action fire) (x 7) (y 5)))
=>

	(assert (exec (step ?s) (action fire) (x 7) (y 5)))
     (pop-focus)



)

(defrule inerzia5
	(status (step ?s)(currently running))

	(not (exec (action fire) (x 8) (y 3)))
=>



	(assert (exec (step ?s) (action fire) (x 8) (y 3)))
     (pop-focus)


)


(defrule inerzia6
	(status (step ?s)(currently running))
		(not (exec  (action fire) (x 8) (y 4)))
=>


	(assert (exec (step ?s) (action fire) (x 8) (y 4)))
     (pop-focus)

	)





(defrule inerzia7
	(status (step ?s)(currently running))
		(not (exec  (action fire) (x 8) (y 5)))
=>


	(assert (exec (step ?s) (action fire) (x 8) (y 5)))
     (pop-focus)

)


(defrule inerzia8
	(status (step ?s)(currently running))
		(not (exec  (action fire) (x 6) (y 9)))
=>


	(assert (exec (step ?s) (action fire) (x 6) (y 9)))
     (pop-focus)
)


(defrule inerzia9
	(status (step ?s)(currently running))
		(not (exec  (action fire) (x 7) (y 9)))
=>


	(assert (exec (step ?s) (action fire) (x 7) (y 9)))
     (pop-focus)
)

(defrule inerzia10 (declare (salience 30))
	(status (step ?s)(currently running))
		(not (exec  (action fire) (x 6) (y 4)))
=>


	(assert (exec (step ?s) (action fire) (x 6) (y 4)))
     (pop-focus)
)

(defrule inerzia11 (declare (salience 30))
	(status (step ?s)(currently running))
		(not (exec  (action guess) (x 7) (y 7)))
=>


	(assert (exec (step ?s) (action guess) (x 7) (y 7)))
     (pop-focus)
)


(defrule inerzia20 (declare (salience 30))
	(status (step ?s)(currently running))
	(not (exec  (action guess) (x 1) (y 3)))
=>


	(assert (exec (step ?s) (action guess) (x 1) (y 3)))
     (pop-focus)

)

(defrule inerzia21  (declare (salience 30))
	(status (step ?s)(currently running))
	(not (exec  (action guess) (x 1) (y 4)))
=>


	(assert (exec (step ?s) (action guess) (x 1) (y 4)))
     (pop-focus)

)





(defrule print-what-i-know-since-the-beginning
	(k-cell (x ?x) (y ?y) (content ?t) )
=>
	(printout t "I know that cell [" ?x ", " ?y "] contains " ?t "." crlf)
)

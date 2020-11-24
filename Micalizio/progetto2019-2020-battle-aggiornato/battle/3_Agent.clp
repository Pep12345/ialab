;  ---------------------------------------------
;  --- Definizione del modulo e dei template ---
;  ---------------------------------------------
(defmodule AGENT (import MAIN ?ALL) (import ENV ?ALL) (export ?ALL))

;template
(deftemplate visto
	(slot x)
	(slot y)
)

;- definire 7 regole per k-cell (usare anche i k-row, k-col)[water,middle.....]
;- definire template b-cell
;- scegliere se dividere in moduli declaration - executation
;- scegliere euristiche da usare quando non abbiamo più k-cell

;k-cell: cose che sappiamo per certo
;b-cell: cose che supponiamo: le usiamo quando troviamo un middle che non sappiamo se andare vertiale o orizzontale
;f-cell: caselle che sono barche ma non sappiamo che parte di barca è e quindi non possiamo metterle come k-cell
			;	[TEMPLATE: rapprensentate come x,y,contenutoPadre]

; nelle regole di f-cell controllo colonne/righe a fianco per vedere se siamo middle o estremo
; regola estrema (da usare quando non so che fare) fire su incrocio con riga e colonna con numero più alto
(defrule k-cell-water
		(status (step ?s)(currently running))  // forse bisogna incrementare step
		(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c water)))
	=>
		; non dovrebbe fare nulla
	     (pop-focus)
)

(defrule k-cell-genenral (declare (salience 10))
		(k-cell (x ?x) (y ?y) (content ?c&:(neq ?c water)))
		?kpr <- (k-per-row (row ?y) (num ?num-row))
  	?kpc <-(k-per-col (col ?x) (num ?num-col))
		(not (visto(x ?x)(y ?y)))
	=>
		(modify ?kpr (num (- ?num-row 1)))
		(modify ?kpc (num (- ?num-col 1)))
		(printout t "x: " ?x " y: " ?y  crlf)
		(assert (visto (x ?x) (y ?y)))
)

;estremi
;regola per quando non conosciamo cosa c'è a destra
(defrule k-cell-left
		(status (step ?s)(currently running))
		(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c left)))
		; k-cell x,y+1 || f-cell x,y+1
	=>
		; creare k-cell water in [x+1,y] // [x-1,y] // [x,y-1] // [x-1,y-1] // [x+1,y-1]
			 (pop-focus)
)
(defrule k-cell-left
		(status (step ?s)(currently running))
		(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c left)))
		; not k-cell x,y+1
		; not f-cell x,y+1
	=>
		; creare k-cell water in [x+1,y] // [x-1,y] // [x,y-1] // [x-1,y-1] // [x+1,y-1]
		; segnare la casella x,y+1 come f-cell (perchè non sappiamo se middle/right)
		(assert (exec (step ?s) (action guess) (x ?x) (y (+ ?y 1)))) // andiamo a mettere bandierina nella casella a fianco
			 (pop-focus)
)


(defrule k-cell-sub
		;(status (step ?s)(currently running))  // forse bisogna incrementare step
		(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c sub)))
	=>
		; creare k-cell water in [x+1,y] // [x-1,y] // [x,y-1] // [x-1,y-1] // [x+1,y-1] // [x+1,y+1] // [x-1,y+1]
			 (pop-focus) ;non dovrebbe servire
)



;(defrule k-cell-middle
;		(status (step ?s)(currently running))  // forse bisogna incrementare step
;		(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c middle)))
;	=>
;	 (pop-focus)
;)



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

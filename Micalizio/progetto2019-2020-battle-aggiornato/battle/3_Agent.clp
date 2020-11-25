;  ---------------------------------------------
;  --- Definizione del modulo e dei template ---
;  ---------------------------------------------
(defmodule AGENT (import MAIN ?ALL) (import ENV ?ALL) (export ?ALL))

;template
(deftemplate visto
	(slot x)
	(slot y)
)

(deftemplate crea-k-cell
	(slot x)
	(slot y)
	(slot c)
)
(deftemplate f-cell
	(slot x)
	(slot y)
	(slot padre) ;da rivedere perchè nel caso di middle come padre si perde l'informazione di orizzontale/verticale
)

;- definire 7 regole per k-cell (usare anche i k-row, k-col)[water,middle.....]
;- definire template b-cell
;- scegliere se dividere in moduli declaration - executation
;- scegliere euristiche da usare quando non abbiamo più k-cell

;k-cell: cose che sappiamo per certo
;b-cell: cose che supponiamo: le usiamo quando troviamo un middle che non sappiamo se andare vertiale o orizzontale
;f-cell: caselle che sono barche ma non sappiamo che parte di barca è e quindi non possiamo metterle come k-cell
			;	[TEMPLATE: rapprensentate come x,y,contenutoPadre?]

; nelle regole di f-cell controllo colonne/righe a fianco per vedere se siamo middle o estremo
; regola estrema (da usare quando non so che fare) fire su incrocio con riga e colonna con numero più alto

(defrule k-cell-genenral (declare (salience 10)) ; PROBABILMENTE X/Y SONO INVERTITE
		(k-cell (x ?x) (y ?y) (content ?c&:(neq ?c water)))
		?kpr <- (k-per-row (row ?y) (num ?num-row))
  	?kpc <-(k-per-col (col ?x) (num ?num-col))
		(not (visto(x ?x)(y ?y)))
	=>
		(modify ?kpr (num (- ?num-row 1)))
		(modify ?kpc (num (- ?num-col 1)))
		(printout t " HO VISTO x: " ?x " y: " ?y  crlf)
		(assert (visto (x ?x) (y ?y)))
)

;sub
(defrule k-cell-sub (declare (salience 20))
		(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c sub)))
	=>
; creare k-cell water in [x+1,y] // [x-1,y] // [x,y-1] // [x,y+1] //[x+1,y-1] // [x-1,y-1] // [x+1,y+1] // [x-1,y+1]
		(assert (crea-k-cell (x (+ ?x 1)) (y ?y) (c water)))
		(assert (crea-k-cell (x (- ?x 1)) (y ?y) (c water)))
		(assert (crea-k-cell (x ?x) (y (- ?y 1)) (c water)))
		(assert (crea-k-cell (x ?x) (y (+ ?y 1)) (c water)))
		(assert (crea-k-cell (x (+ ?x 1)) (y (- ?y 1)) (c water)))
		(assert (crea-k-cell (x (- ?x 1)) (y (- ?y 1)) (c water)))
		(assert (crea-k-cell (x (+ ?x 1)) (y (+ ?y 1)) (c water)))
		(assert (crea-k-cell (x (- ?x 1)) (y (+ ?y 1)) (c water)))
		;(printout t "TROVATO K-CELL SUB IN x: " ?x " y: " ?y  crlf)
)

;creatore per k-cell che controlla di non superare i limiti della mappa
(defrule k-cell-cretor-water (declare (salience 20))
		(crea-k-cell (x ?x) (y ?y) (c ?c&:(eq ?c water)))
		(test(>= ?x 0))
		(test(< ?x 10))
		(test(>= ?y 0))
		(test(< ?y 10))
		(not (k-cell (x ?x) (y ?y) (content ?c)))
	=>
		(assert (k-cell (x ?x) (y ?y) (content ?c)))
		;(printout t "CREATO K CELL IN x: " ?x " y: " ?y  crlf)
)


;estremi
;regola per quando non conosciamo cosa c'è a destra
(defrule k-cell-left-know-neighbor (declare (salience 10))
		(status (step ?s)(currently running))
		(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c left)))
		?z <- (+ ?y 1)
		(or (k-cell (x ?x)(y ?z)) (f-cell (x ?x)(y ?z))) ;caso in cui conosciamo cosa c'è a fianco
	=>
		; creare k-cell water in [x+1,y] // [x-1,y] // [x,y-1] // [x-1,y-1] // [x+1,y-1]
		(assert (crea-k-cell (x (+ ?x 1)) (y ?y) (c water)))
		(assert (crea-k-cell (x (- ?x 1)) (y ?y) (c water)))
		(assert (crea-k-cell (x ?x) (y (- ?y 1)) (c water)))
		(assert (crea-k-cell (x (+ ?x 1)) (y (- ?y 1)) (c water)))
		(assert (crea-k-cell (x (- ?x 1)) (y (- ?y 1)) (c water)))
		(assert (crea-k-cell (x (+ ?x 1)) (y (+ ?y 1)) (c water)))
		(assert (crea-k-cell (x (- ?x 1)) (y (+ ?y 1)) (c water)))
		(printout t "LEFT K CELL- REGOLA KNOW IN x: " ?x " y: " ?y  crlf)
)
(defrule k-cell-left-unknown-neighbor (declare (salience 10))
		(status (step ?s)(currently running))
		(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c left)))
		?z <- (+ ?y 1)
		(not (k-cell (x ?x)(y ?z)(content ?)))
		(not (f-cell (x ?x)(y ?z)(padre ?)))
	=>
		; creare k-cell water in [x+1,y] // [x-1,y] // [x,y-1] // [x-1,y-1] // [x+1,y-1]
		(assert (crea-k-cell (x (+ ?x 1)) (y ?y) (c water)))
		(assert (crea-k-cell (x (- ?x 1)) (y ?y) (c water)))
		(assert (crea-k-cell (x ?x) (y (- ?y 1)) (c water)))
		(assert (crea-k-cell (x (+ ?x 1)) (y (- ?y 1)) (c water)))
		(assert (crea-k-cell (x (- ?x 1)) (y (- ?y 1)) (c water)))
		(assert (crea-k-cell (x (+ ?x 1)) (y (+ ?y 1)) (c water)))
		(assert (crea-k-cell (x (- ?x 1)) (y (+ ?y 1)) (c water)))
		; segnare la casella x,y+1 come f-cell (perchè non sappiamo se middle/right)
		(assert (f-cell (x ?x) (y (+ ?y 1)) (padre ?c)))
		(printout t "LEFT K CELL- REGOLA UNKNOW IN x: " ?x " y: " ?y  crlf)
		(assert (exec (step ?s) (action guess) (x ?x) (y (+ ?y 1)))) ;andiamo a mettere bandierina nella casella a fianco
			 (pop-focus)
)



;(defrule k-cell-middle
;		(status (step ?s)(currently running))  // forse bisogna incrementare step
;		(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c middle)))
;	=>
;	 (pop-focus)
;)




(defrule print-what-i-know-since-the-beginning (declare (salience 50))
	(k-cell (x ?x) (y ?y) (content ?t) )
=>
	(printout t "I know that cell [" ?x ", " ?y "] contains " ?t "." crlf)
)

;  ---------------------------------------------
;  --- Definizione del modulo e dei template ---
;  ---------------------------------------------
(defmodule AGENT (import MAIN ?ALL) (import ENV ?ALL) (export ?ALL))

; // TEMPLATE //
(deftemplate visto
	(slot x)
	(slot y)
)
(deftemplate crea-k-cell-water
	(slot x)
	(slot y)
	(slot c)
)
(deftemplate f-cell
	(slot x)
	(slot y)
	(slot direzione)
)
(deftemplate crea-f-cell
	(slot x)
	(slot y)
	(slot direzione)
)
(deftemplate b-cell
	(slot x)
	(slot y)
)
(deftemplate crea-b-cell
	(slot x)
	(slot y)
)
(deftemplate barca
	(slot tipo)
	(slot num)
)

; INIZIALIZZAZIONE NUMERO BARCHE
(deffacts numerobarche
	(barca (tipo 4)(num 1))
	(barca (tipo 3)(num 2))
	(barca (tipo 2)(num 3))
	(barca (tipo 1)(num 4))
)

; // FUNCTIONS
; (num barche da trovare riga / num caselle sconosciute riga ) * (num barche da trovare col / num caselle sconosciute col )
(deffunction get-known-cell-for-row (?row)
	(+
		(length$ (find-all-facts ((?f k-cell)) (eq ?f:x ?row)))
		(length$ (find-all-facts ((?f1 f-cell)) (eq ?f1:x ?row)))
	)
)
(deffunction get-known-cell-for-col (?col)
	(+
		(length$ (find-all-facts ((?f k-cell)) (eq ?f:y ?col)))
		(length$ (find-all-facts ((?f1 f-cell)) (eq ?f1:y ?col)))
	)
)
(deffunction calculate-prob-x-y (?num-row ?num-col ?x ?y)
	;(printout t "x: " ?x " y: " ?y " ^num-row: " ?num-row " num-col: " ?num-col crlf)
	(*
		(/ ?num-row (- 10 (get-known-cell-for-row ?x)))
		(/ ?num-col (- 10 (get-known-cell-for-col ?y)))
	)
)
; TODO => - scegliere se dividere in moduli declaration - executation
; TODO => - scegliere euristiche da usare quando non abbiamo più k-cell
; TODO => mettere guess sulle b-cell con valori di k-row e k-col più alti
; // DESCRIZIONE CELLE USATE //
;k-cell: cose che sappiamo per certo
;b-cell: cose che supponiamo: le usiamo quando troviamo un middle che non sappiamo se andare vertiale o orizzontale
;f-cell: caselle che sono barche ma non sappiamo che parte di barca è e quindi non possiamo metterle come k-cell


; // REGOLA PER DECREMENTARE I VALORI IN K-ROW K-COL //
(defrule decrement-k-row-col-battleship (declare (salience 50))
		(or (k-cell (x ?x) (y ?y) (content ?c&:(neq ?c water)))
		    (f-cell (x ?x)(y ?y)))
		?kpr <- (k-per-row (row ?x) (num ?num-row))
  	?kpc <-(k-per-col (col ?y) (num ?num-col))
		(not (visto(x ?x)(y ?y)))
	=>
		(modify ?kpr (num (- ?num-row 1)))
		(modify ?kpc (num (- ?num-col 1)))
		(printout t " HO VISTO x: " ?x " y: " ?y  crlf)
		(assert (visto (x ?x) (y ?y)))
)

; // REGOLE PER CREARE CELL //
(defrule crea-f-cell (declare (salience 20))
		(status (step ?s)(currently running))
		(crea-f-cell  (x ?x)(y ?y)(direzione ?c))
		(not (k-cell (x ?x)(y ?y)))
		(not (f-cell (x ?x)(y ?y)))
		(not (exec (step ?s) (action guess) (x ?x)(y ?y))) ; non dovrebbe servire ma l'ha messa il prof nelle fire
	=>
		(assert (f-cell (x ?x)(y ?y)(direzione ?c)))
		(assert (exec (step ?s) (action guess) (x ?x)(y ?y)))
		 	(pop-focus)
)
(defrule crea-b-cell (declare (salience 20))
		(status (step ?s)(currently running))
		(crea-b-cell  (x ?x)(y ?y))
		(k-per-row (row ?x) (num ?num-row))
		(test(>= ?num-row 0))
		(k-per-col (col ?y) (num ?num-col))
		(test(>= ?num-col 0))
		(not (k-cell (x ?x)(y ?y)))
		(not (f-cell (x ?x)(y ?y)))
		(test(>= ?x 0))
		(test(< ?x 10))
		(test(>= ?y 0))
		(test(< ?y 10))
	=>
		(assert (b-cell (x ?x)(y ?y)))
)
(defrule k-cell-cretor-water (declare (salience 20))
		(crea-k-cell-water (x ?x) (y ?y) (c ?c&:(eq ?c water)))
		(test(>= ?x 0))
		(test(< ?x 10))
		(test(>= ?y 0))
		(test(< ?y 10))
		(not (k-cell (x ?x) (y ?y)))
		(not (f-cell (x ?x) (y ?y)))
	=>
		(assert (k-cell (x ?x) (y ?y) (content ?c)))
		;(printout t "CREATO K CELL water IN x: " ?x " y: " ?y  crlf)
)
(defrule create-k-cell-water-in-diagonal (declare (salience 20))
		(or (k-cell (x ?x) (y ?y) (content ?c&:(neq ?c water)))
				(f-cell (x ?x) (y ?y)))
	=>
		(assert (crea-k-cell-water (x (- ?x 1)) (y (+ ?y 1)) (c water)))
		(assert (crea-k-cell-water (x (- ?x 1)) (y (- ?y 1)) (c water)))
		(assert (crea-k-cell-water (x (+ ?x 1)) (y (+ ?y 1)) (c water)))
		(assert (crea-k-cell-water (x (+ ?x 1)) (y (- ?y 1)) (c water)))
)

; // REGOLE K CELL  //

; REGOLA SUB
(defrule k-cell-sub (declare (salience 20))
		(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c sub)))
	=>
		; creare k-cell water in [x+1,y] // [x-1,y] // [x,y-1] // [x,y+1] //[x+1,y-1] // [x-1,y-1] // [x+1,y+1] // [x-1,y+1]
		(assert (decrement-sub-counter))
		(assert (crea-k-cell-water (x (+ ?x 1)) (y ?y) (c water)))
		(assert (crea-k-cell-water (x (- ?x 1)) (y ?y) (c water)))
		(assert (crea-k-cell-water (x ?x) (y (- ?y 1)) (c water)))
		(assert (crea-k-cell-water (x ?x) (y (+ ?y 1)) (c water)))
		(printout t "Ho trovato una K-cell di tipo sub in x " ?x " y: " ?y  crlf)
)

; REGOLE ESTREMI
(defrule k-cell-left (declare (salience 10))
		(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c left)))
	=>
		; creo k-cell water sopra,sotto e a sinistra
		(assert (crea-k-cell-water (x (+ ?x 1)) (y ?y) (c water)))
		(assert (crea-k-cell-water (x (- ?x 1)) (y ?y) (c water)))
		(assert (crea-k-cell-water (x ?x) (y (- ?y 1)) (c water)))

		(assert (crea-f-cell (x ?x) (y (+ ?y 1)) (direzione right)))
		(assert (crea-b-cell (x ?x)(y (+ ?y 2))))
		(printout t "Ho trovato una K-cell di tipo left in x: " ?x " y: " ?y  crlf)
)
(defrule k-cell-right (declare (salience 10))
		(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c right)))
	=>
		(assert (crea-k-cell-water (x (+ ?x 1)) (y ?y) (c water)))
		(assert (crea-k-cell-water (x (- ?x 1)) (y ?y) (c water)))
		(assert (crea-k-cell-water (x ?x) (y (+ ?y 1)) (c water)))

		(assert (crea-f-cell (x ?x) (y (- ?y 1)) (direzione left)))
		(assert (crea-b-cell (x ?x)(y (- ?y 2))))
		(printout t "Ho trovato una K-cell di tipo right in x: " ?x " y: " ?y  crlf)
)
(defrule k-cell-top (declare (salience 10))
		(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c top)))
	=>

		(assert (crea-k-cell-water (x (- ?x 1)) (y ?y) (c water)))
		(assert (crea-k-cell-water (x (- ?x 1)) (y (+ ?y 1)) (c water)))
		(assert (crea-k-cell-water (x (- ?x 1)) (y (- ?y 1)) (c water)))
		(assert (crea-k-cell-water (x ?x) (y (- ?y 1)) (c water)))
		(assert (crea-k-cell-water (x ?x) (y (+ ?y 1)) (c water)))
		(assert (crea-k-cell-water (x (+ ?x 1)) (y (+ ?y 1)) (c water)))
		(assert (crea-k-cell-water (x (+ ?x 1)) (y (- ?y 1)) (c water)))

		(assert (crea-f-cell (x =(+ 1 ?x))(y ?y)(direzione bot)))
		(assert (crea-b-cell (x =(+ 2 ?x))(y ?y)))
		(printout t "Ho trovato una K-cell di tipo top in x: " ?x " y: " ?y  crlf)
)
(defrule k-cell-bot (declare (salience 10))
		(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c bot)))
	=>
		(assert (crea-k-cell-water (x (+ ?x 1)) (y ?y) (c water)))
		(assert (crea-k-cell-water (x ?x) (y (- ?y 1)) (c water)))
		(assert (crea-k-cell-water (x ?x) (y (+ ?y 1)) (c water)))

		(assert (crea-f-cell  (x (- ?x 1))(y ?y)(direzione top)))
		(assert (crea-b-cell (x (- ?x 2))(y ?y)))
		(printout t "Ho trovato una K-cell di tipo bot in x: " ?x " y: " ?y  crlf)
)

; REGOLE MIDDLE
; vicino bordi mappa
(defrule k-cell-middle-near-vertical-border (declare (salience 30))
		(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c middle)))
		(or (test(eq ?y 0)) (test(eq ?y 9)))
	=>
		(assert (crea-f-cell  (x (- ?x 1))(y ?y)(direzione top)))
		(assert (crea-b-cell (x (- ?x 2))(y ?y)))
		(assert (crea-f-cell  (x (+ ?x 1))(y ?y)(direzione bot)))
		(assert (crea-b-cell (x (+ ?x 2))(y ?y)))
		(printout t "Ho trovato una K-cell di tipo middle vicino al bordo in x: " ?x " y: " ?y  crlf)
)
(defrule k-cell-middle-near-horizontal-border (declare (salience 30))
		(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c middle)))
		(or (test(eq ?x 0)) (test(eq ?x 9)))
	=>
		(assert (crea-f-cell  (x ?x)(y (+ ?y 1))(direzione right)))
		(assert (crea-b-cell (x ?x)(y (+ ?y 2))))
		(assert (crea-f-cell  (x ?x)(y (- ?y 1))(direzione left)))
		(assert (crea-b-cell (x ?x)(y (- ?y 2))))
		(printout t "Ho trovato una K-cell di tipo middle vicino al bordo in x: " ?x " y: " ?y  crlf)
)
; quando middle è vicina ad un'altra cella barca (k o f)
(defrule k-cell-middle-near-left-or-middle (declare (salience 10))
		(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c middle)))
		(or (k-cell (x ?x) (y =(- ?y 1)) (content ?c1&:(neq ?c1 water)))
				(f-cell (x ?x) (y =(- ?y 1))))
	=>
		(assert (crea-f-cell  (x ?x)(y (+ ?y 1))(direzione right)))
		(assert (crea-b-cell (x ?x)(y (+ ?y 2))))
		(printout t "Ho trovato una K-cell di tipo middle vicino ad una left in x: " ?x " y: " ?y  crlf)
)
(defrule k-cell-middle-near-right-or-middle (declare (salience 10))
		(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c middle)))
		(or (k-cell (x ?x) (y =(+ 1 ?y)) (content ?c1&:(neq ?c1 water)))
				(f-cell (x ?x) (y =(+ 1 ?y))))
	=>
		(assert (crea-f-cell  (x ?x)(y (- ?y 1))(direzione  left)))
		(assert (crea-b-cell (x ?x)(y (- ?y 2))))
)
(defrule k-cell-middle-near-top-or-middle (declare (salience 10))
		(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c middle)))
		(or (k-cell (x =(- ?x 1)) (y ?y) (content ?c1&:(neq ?c1 water)))
				(f-cell (x =(- ?x 1)) (y ?y)))
	=>
		(assert (crea-f-cell  (x (+ ?x 1))(y ?y)(direzione bot)))
		(assert (crea-b-cell (x (+ ?x 2))(y ?y)))
		(printout t "Ho trovato una K-cell di tipo middle vicino ad una top in x: " ?x " y: " ?y  crlf)
)
(defrule k-cell-middle-near-bot-or-middle (declare (salience 10))
		(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c middle)))
		(or (k-cell (x =(+ 1 ?x)) (y ?y) (content ?c1&:(neq ?c1 water)))
				(f-cell (x =(+ 1 ?x)) (y ?y)))
	=>
		(assert (crea-f-cell  (x (- ?x 1))(y ?y)(direzione top)))
		(assert (crea-b-cell (x (- ?x 2))(y ?y)))
)
; quando middle non è vicina a nulla
(defrule k-cell-middle-ultimaspiaggia (declare (salience 5))
		(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c middle)))
		(not (k-cell (x =(- ?x 1))(y ?y)))	(not (f-cell (x =(- ?x 1))(y ?y)))
		(not (k-cell (x =(+ 1 ?x))(y ?y)))	(not (f-cell (x =(+ 1 ?x))(y ?y)))
		(not (k-cell (x ?x)(y =(- ?y 1))))	(not (f-cell (x ?x)(y =(- ?y 1))))
		(not (k-cell (x ?x)(y =(+ 1 ?y))))	(not (f-cell (x ?x)(y =(+ 1 ?y))))
	=>
		;creao b cell sopra-sotto-destra-sinistra
		(assert (crea-b-cell  (x (- ?x 1))(y ?y)))
		(assert (crea-b-cell  (x (+ ?x 1))(y ?y)))
		(assert (crea-b-cell  (x ?x)(y (- ?y 1))))
		(assert (crea-b-cell  (x ?x)(y (+ ?y 1))))
)
; due middle vicine
(defrule kmid-near-kmid-ver (declare (salience 20))
		(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c middle)))
		(k-cell (x =(+ ?x 1)) (y ?y) (content ?c&:(eq ?c middle)))
	=>
		(assert (crea-k-cell-water (x (+ ?x 3)) (y ?y) (c water)))
		(assert (crea-k-cell-water (x (- ?x 2)) (y ?y) (c water)))
		(printout t "Middle Near Middle in Vertical in x: " ?x " y: " ?y  crlf)
)
(defrule kmid-near-kmid-hor (declare (salience 20))
		(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c middle)))
		(k-cell (x ?x) (y =(+ ?y 1)) (content ?c&:(eq ?c middle)))
	=>
		(assert (crea-k-cell-water (x ?x) (y (+ ?y 3)) (c water)))
		(assert (crea-k-cell-water (x ?x) (y (- ?y 2)) (c water)))
		(printout t "Middle Near Middle in Horizzontal in x: " ?x " y: " ?y  crlf)
)

; // REGOLE K-ROW/K-COL IS ZERO  //
; metto tutte le caselle sconosciute come water
(defrule create-k-cell-water-when-row-value-is-zero (declare (salience 30))
		(k-per-row (row ?row) (num ?num-row&:(eq ?num-row 0)))
	=>
		(loop-for-count (?i 0 9) do
			(assert (crea-k-cell-water (x ?row) (y ?i) (c water))))
)
(defrule create-k-cell-water-when-col-value-is-zero (declare (salience 30))
		(k-per-col (col ?col) (num ?num-col&:(eq ?num-col 0)))
	=>
		(loop-for-count (?i 0 9) do
			(assert (crea-k-cell-water (x ?i) (y ?col) (c water))))
)

; // REGOLE F-CELL //
; // regole per convertire f cell
;(defrule convert-f-to-k-cell-when-is-extreme (declare (salience 20))
;		?fcell <- (f-cell (x ?x)(y ?y)(direzione ?d))
;		(or
;			(and (test(eq ?d right)) (or (k-cell (x ?x)(y =(+ 1 ?y))(content ?c&:(eq ?c water))) (test(>= ?y 9))))
;			(and (test(eq ?d left)) (or (k-cell (x ?x)(y =(- ?y 1))(content ?c&:(eq ?c water))) (test(<= ?y 0))))
;			(and (test(eq ?d top)) (or (k-cell (x =(- ?x 1))(y ?y)(content ?c&:(eq ?c water))) (test(<= ?x 0))))
;			(and (test(eq ?d bot)) (or (k-cell (x =(+ 1 ?x))(y ?y)(content ?c&:(eq ?c water))) (test(>= ?x 9))))
;		)
;	=>
;		(assert (k-cell (x ?x) (y ?y) (content ?d)))
;		(retract ?fcell)
;		(printout t "trasformo la fcell in kcell: " ?x " " ?y crlf)
;)
(defrule convert-f-to-k-cell-if-right (declare (salience 20))
		?fcell <- (f-cell (x ?x)(y ?y))
		(or (f-cell (x ?x) (y =(- ?y 1)))
			(k-cell (x ?x) (y =(- ?y 1)) (content ?c1&:(neq ?c1 water))))
		(or (k-cell (x ?x)(y =(+ 1 ?y))(content ?c&:(eq ?c water)))
				(test(>= ?y 9)))
	=>
		(assert (k-cell (x ?x) (y ?y) (content right)))
		(retract ?fcell)
		(printout t "trasformo la fcell in kcell right: " ?x " " ?y crlf)
)
(defrule convert-f-to-k-cell-if-left (declare (salience 20))
		?fcell <- (f-cell (x ?x)(y ?y))
		(or (f-cell (x ?x) (y =(+ 1 ?y)))
				(k-cell (x ?x) (y =(+ 1 ?y)) (content ?c&:(neq ?c water))))
		(or (k-cell (x ?x)(y =(- ?y 1))(content ?c1&:(eq ?c1 water)))
				(test(<= ?y 0)))
	=>
		(assert (k-cell (x ?x) (y ?y) (content left)))
		(retract ?fcell)
		(printout t "trasformo la fcell in kcell left: " ?x " " ?y crlf)
)
(defrule convert-f-to-k-cell-if-top (declare (salience 20))
		?fcell <- (f-cell (x ?x)(y ?y))
		(or (f-cell (x =(+ 1 ?x)) (y ?y))
				(k-cell (x =(+ 1 ?x)) (y ?y) (content ?c&:(neq ?c water))))
		(or (k-cell (x =(- ?x 1))(y ?y)(content ?c1&:(eq ?c1 water)))
				(test(<= ?x 0)))
	=>
		(assert (k-cell (x ?x) (y ?y) (content top)))
		(retract ?fcell)
		(printout t "trasformo la fcell in kcell top: " ?x " " ?y crlf)
)
(defrule convert-f-to-k-cell-if-bot (declare (salience 20))
		?fcell <- (f-cell (x ?x)(y ?y))
		(or (f-cell (x =(- ?x 1)) (y ?y))
				(k-cell (x =(- ?x 1)) (y ?y) (content ?c1&:(neq ?c1 water))))
		(or (k-cell (x =(+ 1 ?x))(y ?y)(content ?c&:(eq ?c water)))
				(test(>= ?x 9)))
	=>
		(assert (k-cell (x ?x) (y ?y) (content bot)))
		(retract ?fcell)
		(printout t "trasformo la fcell in kcell bot: " ?x " " ?y crlf)
)

; converto f in middle se compresa tra due k/f cell
(defrule convert-f-to-k-cell-middle-if-between-ship-horizontal (declare (salience 20))
		?fcell <- (f-cell (x ?x) (y ?y))
		(or (f-cell (x ?x) (y =(- ?y 1)))
			(k-cell (x ?x) (y =(- ?y 1)) (content ?c1&:(neq ?c1 water))))
		(or (f-cell (x ?x) (y =(+ 1 ?y)))
			(k-cell (x ?x) (y =(+ 1 ?y)) (content ?c&:(neq ?c water))))
	=>
		(assert (k-cell (x ?x) (y ?y) (content middle)))
		(retract ?fcell)
		(printout t "Ho trovato una parte middle di una barca orizzontale x: " ?x " y: " ?y  crlf)
)
(defrule convert-f-to-k-cell-middle-if-between-ship-vertical (declare (salience 20))
		?fcell <- (f-cell (x ?x) (y ?y))
		(or (f-cell (x =(- ?x 1)) (y ?y))
			(k-cell (x =(- ?x 1)) (y ?y) (content ?c1&:(neq ?c1 water))))
		(or (f-cell (x =(+ 1 ?x)) (y ?y))
			(k-cell (x =(+ 1 ?x)) (y ?y) (content ?c&:(neq ?c water))))
	=>
		(assert (k-cell (x ?x) (y ?y) (content middle)))
		(retract ?fcell)
		(printout t "Ho trovato una parte middle di una barca verticale x: " ?x " y: " ?y  crlf)
)
;Regola che non penso riuscirà mai ad attivarsi
(defrule convert-f-to-k-cell-sub-with-waterframe (declare (salience 20))
		?fcell <- (f-cell (x ?x)(y ?y))
		(or (k-cell (x =(- ?x 1)) (y ?y) (content water)) (test(<= ?x 0))) ;sopra
		(or (k-cell (x =(+ ?x 1)) (y ?y) (content water)) (test(>= ?x 9))) ;sotto
		(or (k-cell (x ?x) (y =(+ ?y 1)) (content water)) (test(>= ?y 9))) ;destra
		(or (k-cell (x ?x) (y =(- ?y 1)) (content water)) (test(<= ?y 0)));sinistra
	=>
		(retract ?fcell) 
		(assert (k-cell (x ?x) (y ?y) (content sub)))
)
(defrule convert-f-to-k-middle-when-battleship-type-two-already-been-found (declare (salience 20))
		(barca (tipo 2)(num ?t&:(eq ?t 0)))
		?fcell <-(f-cell (x ?x) (y ?y))
		(or
			(k-cell (x =(+ ?x 1)) (y ?y) (content ?c&:(neq ?c water)))
			(k-cell (x =(- ?x 1)) (y ?y) (content ?c1&:(neq ?c1 water)))
			(k-cell (x ?x) (y  =(+ ?y 1)) (content ?c2&:(neq ?c2 water)))
			(k-cell (x ?x) (y  =(- ?y 1)) (content ?c3&:(neq ?c3 water)))
		)
	=>
		(retract ?fcell)
		(assert (k-cell (x ?x) (y ?y) (content middle)))
)
; limito le barche con cell water in base alle barche che conosco già
(defrule add-water-if-battleship-type-three-horizontal (declare (salience 20))
			(barca (tipo 4)(num ?t&:(eq ?t 0)))
			(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c middle)))
			(or (k-cell (x ?x) (y =(- ?y 1)) (content ?c&:(neq ?c water)))
					(f-cell (x ?x) (y =(- ?y 1))))
			(or (k-cell (x ?x) (y =(+ ?y 1)) (content ?c1&:(neq ?c1 water)))
					(f-cell (x ?x) (y =(+ ?y 1))))
		=>
			(assert (crea-k-cell-water (x ?x) (y (- ?y 2)) (c water)))
			(assert (crea-k-cell-water (x ?x) (y (+ ?y 2)) (c water)))
)
(defrule add-water-if-battleship-type-three-vertical (declare (salience 20))
			(barca (tipo 4)(num ?t&:(eq ?t 0)))
			(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c middle)))
			(or (k-cell (x =(- ?x 1)) (y ?y) (content ?c&:(neq ?c water)))
					(f-cell (x =(- ?x 1)) (y ?y)))
			(or (k-cell (x =(+ ?x 1)) (y ?y) (content ?c1&:(neq ?c1 water)))
					(f-cell (x =(+ ?x 1)) (y ?y)))
		=>
			(assert (crea-k-cell-water (x (- ?x 2)) (y ?y) (c water)))
			(assert (crea-k-cell-water (x (+ ?x 2)) (y ?y) (c water)))
)
(defrule add-water-if-battleship-type-two-horizontal (declare (salience 20))
			(barca (tipo 4)(num ?t&:(eq ?t 0)))
			(barca (tipo 3)(num ?t1&:(eq ?t1 0)))
			(or (k-cell (x ?x) (y ?y) (content ?c&:(neq ?c water)))
					(f-cell (x ?x) (y ?y)))
			(or (k-cell (x ?x) (y =(+ ?y 1)) (content ?c1&:(neq ?c1 water)))
					(f-cell (x ?x) (y =(+ ?y 1))))
		=>
			(assert (crea-k-cell-water (x ?x) (y (- ?y 1)) (c water)))
			(assert (crea-k-cell-water (x ?x) (y (+ ?y 2)) (c water)))
)
(defrule add-water-if-battleship-type-two-vertical (declare (salience 20))
			(barca (tipo 4)(num ?t&:(eq ?t 0)))
			(barca (tipo 3)(num ?t1&:(eq ?t1 0)))
			(or (k-cell (x ?x) (y ?y) (content ?c&:(neq ?c water)))
					(f-cell (x ?x) (y ?y)))
			(or (k-cell (x =(+ ?x 1)) (y ?y) (content ?c1&:(neq ?c1 water)))
					(f-cell (x =(+ ?x 1)) (y ?y)))
		=>
			(assert (crea-k-cell-water (x (- ?x 1)) (y ?y) (c water)))
			(assert (crea-k-cell-water (x (+ ?x 2)) (y ?y) (c water)))
)
(defrule add-water-if-battleship-type-one (declare (salience 20))
			(barca (tipo 4)(num ?t&:(eq ?t 0)))
			(barca (tipo 3)(num ?t1&:(eq ?t1 0)))
			(barca (tipo 2)(num ?t2&:(eq ?t2 0)))
			(f-cell (x ?x) (y ?y))
		=>
			(assert (crea-k-cell-water (x (+ ?x 1)) (y ?y) (c water)))
			(assert (crea-k-cell-water (x (- ?x 1)) (y ?y) (c water)))
			(assert (crea-k-cell-water (x ?x) (y (- ?y 1)) (c water)))
			(assert (crea-k-cell-water (x ?x) (y (+ ?y 1)) (c water)))
)

; // regole per creare f cell
; se le restanti celle da scoprire lungo la riga = numer barche di quella riga => creo f cell
(defrule create-f-cell-in-row-where-i-know-all-water-cells (declare (salience 20))
		(k-per-row (row ?x) (num ?num-row&:(> ?num-row 0)))
		(status (step ?s)(currently running))
		(test (eq (+ 	(+ (length$ (find-all-facts ((?f k-cell)) (eq ?f:x ?x))) (length$ (find-all-facts ((?f1 f-cell)) (eq ?f1:x ?x)))) ?num-row) 10))
		(k-per-col (col ?y)(num ?num-col&:(> ?num-col 0))) ; (num...) velocizza perchè salta subito tutta la colonna ma funziona anche senza
		(not (k-cell (x ?x) (y ?y)))
		(not (f-cell (x ?x) (y ?y)))
	=>
		;(printout t "Creo f-cell usando nonsocomechiamarla x: " ?x " y: " ?y  crlf)
		(assert (crea-f-cell (x ?x)(y ?y)))
)
(defrule create-f-cell-in-col-where-i-know-all-water-cells (declare (salience 20))
		(k-per-col (col ?y) (num ?num-col&:(> ?num-col 0)))
		(status (step ?s)(currently running))
		(test (eq (+ (+ (length$ (find-all-facts ((?f k-cell)) (eq ?f:y ?y))) (length$ (find-all-facts ((?f1 f-cell)) (eq ?f1:y ?y)))) ?num-col) 10))
		(k-per-row (row ?x)(num ?num-row&:(> ?num-row 0)))
		(not (k-cell (x ?x) (y ?y)))
		(not (f-cell (x ?x) (y ?y)))
	=>
		;(printout t "Creo f-cell usando nonsocomechiamarla x: " ?x " y: " ?y  crlf)
		(assert (crea-f-cell (x ?x)(y ?y)))
)


; // REGOLE B-CELL //
(defrule delete-b-cell-where-row-is-zero (declare (salience 20))
		(k-per-row (row ?row) (num ?num-row&:(eq ?num-row 0)))
		?bcell <- (b-cell (x ?x&:(eq ?x ?row))(y ?y))
	=>
		(retract ?bcell)
)
(defrule delete-b-cell-where-col-is-zero (declare (salience 20))
		(k-per-col (col ?col) (num ?num-col&:(eq ?num-col 0)))
		?bcell <- (b-cell (x ?x)(y ?y&:(eq ?y ?col)))
	=>
		(retract ?bcell)
)
(defrule delete-b-cell-if-k-cell-water (declare (salience 20))
		(k-cell (x ?x) (y ?y))
		?bcell <- (b-cell (x ?x)(y ?y))
	=>
		(retract ?bcell)
)

; // REGOLE BARCHE TROVATE //
(defrule found-battleship-type-two(declare (salience 40))
		(or
			(and 	(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c left)))
						(k-cell (x ?x) (y =(+ 1 ?y)) (content ?c1&:(eq ?c1 right))))
			(and 	(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c top)))
						(k-cell (x =(+ 1 ?x)) (y ?y) (content ?c1&:(eq ?c1 bot))))
		)
	=>
		(assert (decrement-double-counter))
)
(defrule found-battleship-type-three(declare (salience 40))
		(or
			(and 	(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c left)))
						(k-cell (x ?x) (y =(+ 1 ?y)) (content ?c1&:(eq ?c1 middle)))
						(k-cell (x ?x) (y =(+ 2 ?y)) (content ?c2&:(eq ?c2 right))))
			(and 	(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c top)))
						(k-cell (x =(+ 1 ?x)) (y ?y) (content ?c1&:(eq ?c1 middle)))
						(k-cell (x =(+ 2 ?x)) (y ?y) (content ?c2&:(eq ?c2 bot))))
		)
	=>
		(assert (decrement-triple-counter))
)
(defrule found-battleship-type-four(declare (salience 40))
	(or
		(and 	(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c left)))
					(k-cell (x ?x) (y =(+ 1 ?y)) (content ?c1&:(eq ?c1 middle)))
					(k-cell (x ?x) (y =(+ 2 ?y)) (content ?c2&:(eq ?c2 middle)))
					(k-cell (x ?x) (y =(+ 3 ?y)) (content ?c3&:(eq ?c3 right))))
		(and 	(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c top)))
					(k-cell (x =(+ 1 ?x)) (y ?y) (content ?c1&:(eq ?c1 middle)))
					(k-cell (x =(+ 2 ?x)) (y ?y) (content ?c2&:(eq ?c2 middle)))
					(k-cell (x =(+ 3 ?x)) (y ?y) (content ?c3&:(eq ?c3 bot))))
	)
	=>
		(assert (decrement-fourth-counter))
)
; regole per decrementare contatori
(defrule decrement-battleship-sub-counter(declare (salience 40))
		?dsc <- (decrement-sub-counter)
		?nb <- (barca (tipo 1)(num ?t))
	=>
		(modify ?nb (num (- ?t 1)))
		(retract ?dsc)
		(printout t "-Trovata barca sub-"  crlf)
)
(defrule decrement-battleship-type-two-counter(declare (salience 40))
		?dsc <- (decrement-double-counter)
		?nb <- (barca (tipo 2)(num ?t))
	=>
		(modify ?nb (num (- ?t 1)))
		(retract ?dsc)
		(printout t "-Trovata barca da due-"  crlf)
)
(defrule decrement-battleship-type-three-counter(declare (salience 40))
		?dsc <- (decrement-triple-counter)
		?nb <- (barca (tipo 3)(num ?t))
	=>
		(modify ?nb (num (- ?t 1)))
		(retract ?dsc)
		(printout t "-Trovata barca da tre-"  crlf)
)
(defrule decrement-battleship-type-four-counter(declare (salience 40))
		?dsc <- (decrement-fourth-counter)
		?nb <- (barca (tipo 4)(num ?t))
	=>
		(modify ?nb (num (- ?t 1)))
		(retract ?dsc)
		(printout t "-Trovata barca da quattro-"  crlf)
)

; // REGOLE FIRE
;FIRE 2: sparo sulla f-cell che si trova dopo una kcell estrema e una kcell middle
(defrule fire-two (declare (salience -45))
		(moves (fires ?fires&:(> ?fires 0)))
		?fcell <- (f-cell (x ?x)(y ?y))
		(barca (tipo 4)(num ?value&:(> ?value 0)))
		(or
		(and (k-cell (x =(+ ?x 1)) (y ?y) (content ?c&:(eq ?c middle))) (k-cell (x =(+ ?x 2)) (y ?y) (content ?c1&:(eq ?c1 bot))))
		(and (k-cell (x =(- 1 ?x)) (y ?y) (content ?c&:(eq ?c middle))) (k-cell (x =(- 2 ?x)) (y ?y) (content ?c2&:(eq ?c2 top))))
		(and (k-cell (x ?x) (y =(+ ?y 1)) (content ?c&:(eq ?c middle))) (k-cell (x ?x) (y =(+ ?y 2)) (content ?c3&:(eq ?c3 right))))
		(and (k-cell (x ?x) (y =(- 1 ?y)) (content ?c&:(eq ?c middle))) (k-cell (x ?x) (y =(- 2 ?y)) (content ?c4&:(eq ?c4 left))))
		)
		(status (step ?s)(currently running))
		(not (exec  (action fire) (x ?x) (y ?y)))
	=>
		(printout t "Sto per eseguire Fire-two in x: " ?x " y: " ?y  crlf)
		(retract ?fcell)
		(assert (exec (step ?s) (action fire) (x ?x) (y ?y)))
	  	(pop-focus)
)
; FIRE 3: sparo sulla riga e colonna con la maggiore probabilità di avere una barca
; nota: nel caso in cui fa fire su water, k row max e k col max restano gli stessi e quindi sta regola si blocca e non fa altre fire
(defrule fire-where-krow-kcol-have-max-value (declare (salience -100))
		(moves (fires ?fires&:(> ?fires 0)))
		(k-per-row (row ?x) (num ?num-row))
		(not (k-per-row (num ?num-row2&:(> ?num-row2 ?num-row))))
		(k-per-col (col ?y) (num ?num-col))
		(not (k-per-col (num ?num-col2&:(> ?num-col2 ?num-col))))
		(status (step ?s)(currently running))
		(not (exec  (action fire) (x ?x) (y ?y))) ; non dovrebbe servire ma l'ha messa il prof
		(not (k-cell (x ?x) (y ?y)))
	=>
		(printout t " FIRE di tipo 3 in x: " ?x " y: " ?y  crlf)
		(assert (exec (step ?s) (action fire) (x ?x) (y ?y)))
	  (pop-focus)
)
; FIRE 1: sparo su b/f cell vicina ad una f cell
(defrule fire1-bcell (declare (salience -55))
	(moves (fires ?fires&:(> ?fires 0)))
  (status (step ?s)(currently running))
  ?b<-(b-cell(x ?x) (y ?y))
  (or
     (f-cell (x ?x)(y =(- ?y 1)))
     (f-cell(x ?x)(y =(+ 1 ?y)))
     (f-cell(x =(+ 1 ?x))(y ?y))
     (f-cell(x =(- ?x 1))(y ?y))
   )
 =>
 (retract ?b)
 (printout t " FIRE di tipo 1 - b in x: " ?x " y: " ?y  crlf)
 (assert (exec (step ?s) (action fire) (x ?x) (y ?y)))
     (pop-focus)
)
(defrule fire1-fcell (declare (salience -55))
	(moves (fires ?fires&:(> ?fires 0)))
  (status (step ?s)(currently running))
  ?f<-(f-cell(x ?x) (y ?y))
  (or
     (b-cell (x ?x)(y =(- ?y 1)))
     (b-cell(x ?x)(y =(+ 1 ?y)))
     (b-cell(x =(+ 1 ?x))(y ?y))
     (b-cell(x =(- ?x 1))(y ?y))
   )
 =>
 (retract ?f)
 (printout t " FIRE di tipo 1 - f in x: " ?x " y: " ?y  crlf)
 (assert (exec (step ?s) (action fire) (x ?x) (y ?y)))
      (pop-focus)
)
;FIRE PROB
; probabilità calcolata come: prodotto tra
;																	num-row diviso caselle sconosciute su quella riga
;																	num-col diviso caselle sconosciute su quella colonna
; alternativa - fare max nella funzione e trovare il candidato migliore direttamente nella funzione
(defrule fire-probability (declare (salience -65))
		(k-per-row (row ?x) (num ?num-row&:(> ?num-row 0)))
		(k-per-col (col ?y) (num ?num-col&:(> ?num-col 0)))
		(not (k-cell (x ?x) (y ?y)))
		(not (exec  (action fire) (x ?x) (y ?y)))
		(not (and
						(k-per-row (row ?x1)(num ?num-row2&:(> ?num-row2 0)))
						(k-per-col (col ?y1)(num ?num-col2&:(> ?num-col2 0)))
						(not (k-cell (x ?x1) (y ?y1))) ; non serve ma magari riduce il numero di confronti
						(test(> (calculate-prob-x-y ?num-row2 ?num-col2 ?x1 ?y1) (calculate-prob-x-y ?num-row ?num-col ?x ?y)))
					)
		)
		(moves (fires ?fires&:(> ?fires 0)))	; non capisco perchè se ste due righe le sposto sopra non mi funziona calculate-prob-x-y
		(status (step ?s)(currently running))
	=>
		(printout t " FIRE sulla probabilità in x: " ?x " y: " ?y  crlf)
		(assert (exec (step ?s) (action fire) (x ?x) (y ?y)))
			(pop-focus)
)


(defrule add-k-cell-water-if-fire-fail (declare (salience 20))
		(exec (step ?s) (action fire) (x ?x) (y ?y))
		(status (step ?s1&:(> ?s1 ?s))(currently running))
		(not (k-cell (x ?x) (y ?y)))
	=>
		(assert(k-cell(x ?x) (y ?y)(content water)))
)

; // REGOLE PULIZIA FATTI //
(defrule remove-crea-f-cell
		?c <- (crea-f-cell (x ?x)(y ?y))
		(or (k-cell (x ?x) (y ?y))
				(f-cell (x ?x) (y ?y))
				(or (test(< ?x 0)) (test(>= ?x 10)) (test(< ?y 0)) (test(>= ?y 10)))
		)
		=>
			(retract ?c)
)
(defrule remove-crea-b-cell
		?c <- (crea-b-cell (x ?x)(y ?y))
		(or (k-cell (x ?x) (y ?y))
				(f-cell (x ?x) (y ?y))
				(b-cell (x ?x) (y ?y))
				(or (test(< ?x 0)) (test(>= ?x 10)) (test(< ?y 0)) (test(>= ?y 10)))
		)
	=>
		(retract ?c)
)
(defrule remove-crea-k-cell-water
		?c <- (crea-k-cell-water (x ?x)(y ?y))
		(or (k-cell (x ?x) (y ?y))
				(f-cell (x ?x) (y ?y))
				(or (test(< ?x 0)) (test(>= ?x 10)) (test(< ?y 0)) (test(>= ?y 10)))
		)
				 ; controllo anche f-cell perchè c'è una regola che quando il contatore della riga si azzera
				 ; fa crea-k-cell water su tutta la riga
	=>
		(retract ?c)
)

; // Termina programma
;Bisogna asserire (exec (step ?s) (action solve)) quando vogliamo concludere
(defrule solve (declare (salience -999))
		(moves (fires ?fires&:(<= ?fires 0)))	; provvisoria, bisogna decidere quando risolvere il problema
		(status (step ?s)(currently running))
	=>
		(assert (exec (step ?s) (action solve)))
)

; // STAMPE PRIMA DI FARE FIRE //
(defrule print-k-col (declare (salience 1))
		(status (step ?s)(currently running))
		(k-per-col (col ?y)(num ?n))
	=>
		(printout t "K-col: " ?y " num: " ?n crlf)
)
(defrule print-k-row (declare (salience 1))
		(status (step ?s)(currently running))
		(k-per-row (row ?x)(num ?n))
	=>
		(printout t "K-row: " ?x " num: " ?n crlf)
)
(defrule print-what-i-know-first-to-fire-k (declare (salience 0))
		(status (step ?s)(currently running))
		(k-cell (x ?x) (y ?y)(content ?t) )
	=>
		(printout t "K cell  [" ?x ", " ?y "] type: " ?t crlf)
)
(defrule print-what-i-know-first-to-fire-f (declare (salience 0))
		(status (step ?s)(currently running))
		(f-cell (x ?x) (y ?y)(direzione ?t))
	=>
		(printout t "F cell [" ?x ", " ?y "] direzione " ?t  crlf)
)
(defrule print-what-i-know-first-to-fire-b (declare (salience 0))
			(status (step ?s)(currently running))
			(b-cell (x ?x)(y ?y))
	=>
		(printout t "B cell [" ?x ", " ?y "] " crlf)
)

; stampa generale
(defrule print-what-i-know-since-the-beginning (declare (salience 50))
		(k-cell (x ?x) (y ?y) (content ?t) )
	=>
		(printout t "I know that cell [" ?x ", " ?y "] contains " ?t "." crlf)
)


;se f-cell è compresa tra acqua e f cell(okcell middle) allora è estremo
; regole superflue
; (defrule f-cell-is-k-cell-bot
;         ?f<-(f-cell(x ?x) (y ?y))
;         (or (f-cell(x =(- ?x 1))(y ?y))(k-cell(x =(- ?x 1))(y ?y)))
;         (k-cell (x =(+ 1 ?x))(y ?y)(content ?c&:(eq ?c water)))
;         =>
;         (retract ?f)
;         (assert(k-cell(x ?x) (y ?y)(content bot)))
; )

; (defrule f-cell-is-k-cell-top
;         ?f<-(f-cell(x ?x) (y ?y))
;         (or (f-cell(x =(+ 1 ?x))(y ?y))(k-cell(x =(+ 1 ?x))(y ?y)))
;         (k-cell (x =(- ?x 1))(y ?y)(content ?c&:(eq ?c water)))
;         =>
;         (retract ?f)
;         (assert(k-cell(x ?x) (y ?y)(content top)))
; )

; (defrule f-cell-is-k-cell-left
;         ?f<-(f-cell(x ?x) (y ?y))
;         (or (f-cell(x ?x)(y =(+ 1 ?y)))(k-cell(x ?x)(y =(+ 1 ?y))))
;         (k-cell (x ?x)(y =(- ?y 1))(content ?c&:(eq ?c water)))
;         =>
;         (retract ?f)
;         (assert(k-cell(x ?x) (y ?y)(content left)))
; )

; (defrule f-cell-is-k-cell-right
;         ?f<-(f-cell(x ?x) (y ?y))
;         (or (f-cell(x ?x)(y =(- ?y 1)))(k-cell(x ?x)(y =(- ?y 1))))
;         (k-cell (x ?x)(y =(+ 1 ?y))(content ?c&:(eq ?c water)))
;         =>
;         (retract ?f)
;         (assert(k-cell(x ?x) (y ?y)(content right)))
; )



;DOMANDE:
; se facciamo la fire su una casella su cui abbiamo fatto la guess dobbiamo fare unguess?
; dovremmo fare guess sulle k-cell iniziali?
; l'app per i risultati, conviene salvare i fatti su file e leggere per creare immagine o usare librerie e avviare clips da java?

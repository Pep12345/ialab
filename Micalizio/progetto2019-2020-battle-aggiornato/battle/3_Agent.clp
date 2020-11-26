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
(deftemplate crea-f-cell
	(slot x)
	(slot y)
	(slot padre)
)
(deftemplate b-cell
	(slot x)
	(slot y)
)
(deftemplate crea-b-cell
	(slot x)
	(slot y)
)

;- scegliere se dividere in moduli declaration - executation
;- scegliere euristiche da usare quando non abbiamo più k-cell

;k-cell: cose che sappiamo per certo
;b-cell: cose che supponiamo: le usiamo quando troviamo un middle che non sappiamo se andare vertiale o orizzontale
;f-cell: caselle che sono barche ma non sappiamo che parte di barca è e quindi non possiamo metterle come k-cell
			;	[TEMPLATE: rapprensentate come x,y,contenutoPadre?]

(defrule rule-for-decrement-k-row-col (declare (salience 50))
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


; K CELL RULES
; REGOLE ESTREMI
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
		(printout t "Ho trovato una K-cell di tipo sub in x " ?x " y: " ?y  crlf)
)

(defrule k-cell-left (declare (salience 10))
		(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c left)))
	=>
		; creare k-cell water in [x+1,y] // [x-1,y] // [x,y-1] // [x-1,y-1] // [x+1,y-1]
		(assert (crea-k-cell (x (+ ?x 1)) (y ?y) (c water)))
		(assert (crea-k-cell (x (- ?x 1)) (y ?y) (c water)))
		(assert (crea-k-cell (x ?x) (y (- ?y 1)) (c water)))
		(assert (crea-k-cell (x (+ ?x 1)) (y (- ?y 1)) (c water)))
		(assert (crea-k-cell (x (- ?x 1)) (y (- ?y 1)) (c water)))
		(assert (crea-k-cell (x (+ ?x 1)) (y (+ ?y 1)) (c water)))
		(assert (crea-k-cell (x (- ?x 1)) (y (+ ?y 1)) (c water)))

		(assert (crea-f-cell (x ?x) (y (+ ?y 1)) (padre ?c)))
		(printout t "Ho trovato una K-cell di tipo left in x: " ?x " y: " ?y  crlf)
)

(defrule k-cell-right (declare (salience 10))
		(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c right)))
	=>
		(assert (crea-k-cell (x (+ ?x 1)) (y ?y) (c water)))
		(assert (crea-k-cell (x (- ?x 1)) (y ?y) (c water)))
		(assert (crea-k-cell (x ?x) (y (+ ?y 1)) (c water)))
		(assert (crea-k-cell (x (+ ?x 1)) (y (- ?y 1)) (c water)))
		(assert (crea-k-cell (x (- ?x 1)) (y (- ?y 1)) (c water)))
		(assert (crea-k-cell (x (+ ?x 1)) (y (+ ?y 1)) (c water)))
		(assert (crea-k-cell (x (- ?x 1)) (y (+ ?y 1)) (c water)))

		(assert (crea-f-cell (x ?x) (y (- ?y 1)) (padre ?c)))
		(printout t "Ho trovato una K-cell di tipo right in x: " ?x " y: " ?y  crlf)
)

(defrule k-cell-top (declare (salience 10))
		(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c top)))
	=>

		(assert (crea-k-cell (x (- ?x 1)) (y ?y) (c water)))
		(assert (crea-k-cell (x (- ?x 1)) (y (+ ?y 1)) (c water)))
		(assert (crea-k-cell (x (- ?x 1)) (y (- ?y 1)) (c water)))
		(assert (crea-k-cell (x ?x) (y (- ?y 1)) (c water)))
		(assert (crea-k-cell (x ?x) (y (+ ?y 1)) (c water)))
		(assert (crea-k-cell (x (+ ?x 1)) (y (+ ?y 1)) (c water)))
		(assert (crea-k-cell (x (+ ?x 1)) (y (- ?y 1)) (c water)))

		(assert (crea-f-cell (x =(+ 1 ?x))(y ?y)) (padre ?c))
		(printout t "Ho trovato una K-cell di tipo top in x: " ?x " y: " ?y  crlf)
)

(defrule k-cell-bot (declare (salience 10))
		(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c bot)))
	=>
		(assert (crea-k-cell (x (+ ?x 1)) (y ?y) (c water)))
		(assert (crea-k-cell (x ?x) (y (- ?y 1)) (c water)))
		(assert (crea-k-cell (x ?x) (y (+ ?y 1)) (c water)))
		(assert (crea-k-cell (x (- ?x 1)) (y (+ ?y 1)) (c water)))
		(assert (crea-k-cell (x (- ?x 1)) (y (- ?y 1)) (c water)))
		(assert (crea-k-cell (x (+ ?x 1)) (y (+ ?y 1)) (c water)))
		(assert (crea-k-cell (x (+ ?x 1)) (y (- ?y 1)) (c water)))

		(assert (crea-f-cell  (x (- ?x 1))(y ?y)(padre ?c)))
		(printout t "Ho trovato una K-cell di tipo bot in x: " ?x " y: " ?y  crlf)
)

; MIDDLE RULES
(defrule k-cell-middle-vertical-border (declare (salience 30))
		(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c middle)))
		(or (test(eq ?y 0)) (test(eq ?y 9)))
	=>
		(assert (crea-f-cell  (x (+ ?x 1))(y ?y)(padre ?c)))
		(assert (crea-f-cell  (x (- ?x 1))(y ?y)(padre ?c)))
		(printout t "Ho trovato una K-cell di tipo middle vicino al bordo in x: " ?x " y: " ?y  crlf)
)

(defrule k-cell-middle-horizontal-border (declare (salience 30))
		(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c middle)))
		(or (test(eq ?x 0)) (test(eq ?x 9)))
	=>
		(assert (crea-f-cell  (x ?x)(y (+ ?y 1))(padre ?c)))
		(assert (crea-f-cell  (x ?x)(y (- ?y 1))(padre ?c)))
		(printout t "Ho trovato una K-cell di tipo middle vicino al bordo in x: " ?x " y: " ?y  crlf)
)

(defrule k-cell-middle-general (declare (salience 30))
		(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c middle)))
	=>
		(assert (crea-k-cell (x (- ?x 1)) (y (+ ?y 1)) (c water)))
		(assert (crea-k-cell (x (- ?x 1)) (y (- ?y 1)) (c water)))
		(assert (crea-k-cell (x (+ ?x 1)) (y (+ ?y 1)) (c water)))
		(assert (crea-k-cell (x (+ ?x 1)) (y (- ?y 1)) (c water)))
		(printout t "Creo water vicino middle: " ?x " y: " ?y  crlf)
)

; serie di regole in cui middle è vicina a k cell
(defrule k-cell-middle-near-left (declare (salience 10))
		(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c middle)))
		(or (k-cell (x ?x) (y =(- ?y 1)) (content ?c1&:(neq ?c1 water)))
				(f-cell (x ?x) (y =(- ?y 1))))
	=>
		(assert (crea-f-cell  (x ?x)(y (+ ?y 1))(padre ?c)))
		(printout t "Ho trovato una K-cell di tipo middle vicino ad una left in x: " ?x " y: " ?y  crlf)
)
(defrule k-cell-middle-near-right (declare (salience 10))
		(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c middle)))
		(or (k-cell (x ?x) (y =(+ 1 ?y)) (content ?c1&:(neq ?c1 water)))
				(f-cell (x ?x) (y =(+ 1 ?y))))
	=>
		(assert (crea-f-cell  (x ?x)(y (- ?y 1))(padre ?c)))
)
(defrule k-cell-middle-near-top (declare (salience 10))
		(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c middle)))
		(or (k-cell (x =(- ?x 1)) (y ?y) (content ?c1&:(neq ?c1 water)))
				(f-cell (x =(- ?x 1)) (y ?y)))
	=>
		(assert (crea-f-cell  (x (+ ?x 1))(y ?y)(padre ?c)))
		(printout t "Ho trovato una K-cell di tipo middle vicino ad una top in x: " ?x " y: " ?y  crlf)
)
(defrule k-cell-middle-near-bot (declare (salience 10))
		(k-cell (x ?x) (y ?y) (content ?c&:(eq ?c middle)))
		(or (k-cell (x =(+ 1 ?x)) (y ?y) (content ?c1&:(neq ?c1 water)))
				(f-cell (x =(+ 1 ?x)) (y ?y)))
	=>
		(assert (crea-f-cell  (x (- ?x 1))(y ?y)(padre ?c)))
)

; caso finale in cui conosco solo k-cell water
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



;B-CELL rules
(defrule delete-b-cell-where-row-is-0 (declare (salience 20))
		(k-per-row (row ?row) (num ?num-row&:(eq ?num-row 0)))
		?bcell <- (b-cell (x ?x&:(eq ?x ?row))(y ?y))
	=>
		(retract ?bcell)
)
(defrule delete-b-cell-where-col-is-0 (declare (salience 20))
		(k-per-col (col ?col) (num ?num-col&:(eq ?num-col 0)))
		?bcell <- (b-cell (x ?x)(y ?y&:(eq ?y ?col)))
	=>
		(retract ?bcell)
)


; CREA CELLE
(defrule crea-f-cell (declare (salience 10))
		(status (step ?s)(currently running))
		(crea-f-cell  (x ?x)(y ?y)(padre ?c))
		(not (k-cell (x ?x)(y ?y)))
		(not (f-cell (x ?x)(y ?y)))
	=>
		(assert (crea-k-cell (x (+ ?x 1)) (y (- ?y 1)) (c water)))
		(assert (crea-k-cell (x (- ?x 1)) (y (- ?y 1)) (c water)))
		(assert (crea-k-cell (x (+ ?x 1)) (y (+ ?y 1)) (c water)))
		(assert (crea-k-cell (x (- ?x 1)) (y (+ ?y 1)) (c water)))
		(assert (f-cell (x ?x)(y ?y)) (padre ?c))
		(assert (exec (step ?s) (action guess) (x ?x)(y ?y)))
		 	(pop-focus)
)

(defrule crea-b-cell (declare (salience 10))
		(status (step ?s)(currently running))
		(crea-b-cell  (x ?x)(y ?y))
		(k-per-row (row ?x) (num ?num-row))
		(test(>= ?num-row 0))
		(k-per-col (col ?y) (num ?num-col))
		(test(>= ?num-col 0))
		(not (k-cell (x ?x)(y ?y)))
		(not (f-cell (x ?x)(y ?y)))
	=>
		(assert (b-cell (x ?x)(y ?y)))
)


; f-cell:
	; regola priorità alta: per ogni f cell in base ai valori che può assumere, guardo valore riga/col adiacente, se 0 -> questa f cell diventa k cell estremo
	; op1: multislot antenati
	; op2: due slot: direzione e lunghezza antenati

	; se ho una f-cell vicina a una b cell è molto probabile quella bcell sia barca
	; se ho f cell di una barca lunga 4 allora siamo ad un estremo
;regole generali:
	; se la somma di k-cell water e il contatore della riga/colonna = max allora le restanti sono barche
	; 8kcell water e 2 sconosciute allora dato che la riga = 10 le ultime 2 sono barche
	; mettere guess sulle b-cell con valori di k-row e k-col più alti
;regola ultima spiaggia:
	; sparo fire a caso nell'incrocio tra riga e col con valori più alti ( da qusare quando non so che pesci pijare)

(defrule print-what-i-know-since-the-beginning (declare (salience 50))
	(k-cell (x ?x) (y ?y) (content ?t) )
=>
	(printout t "I know that cell [" ?x ", " ?y "] contains " ?t "." crlf)
)

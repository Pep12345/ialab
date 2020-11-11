
/* in questa soluzione rappresento ogni nodo come:
nodo(posizione, percorso per raggiungerlo)

asserta server per aggiungere clausole dinamicamente in cima (assertz uguale ma in fondo)
retract al contrario, rimuove la clausola
*/

/* inizio da Start con soglia 0 */
ida_star(Soluzione):-
	iniziale(Start),

	retractall(prossimoThreshold(_)),	% serve perchè in caso di chiamate ripetute rimangono clausole dinamiche
	retractall(thresholdCheck(_)),		% ed è anche più veloce da leggere
	
	asserta(prossimoThreshold(0)), % setto il threshold per il prossimo giro(prima iterazione) a 0
	asserta(thresholdCheck(0)),
	ida_aux(nodo(Start,[]),Soluzione).

/* 
	setto i parametri iniziali
 */
ida_aux(Nodo,Res):-
	generaFigli(Nodo,[],Figli),
	retractall(thresholdCheck(_)), 
	asserta(thresholdCheck(Threshold)),	% aggiorno il nuovo thresholdCheck per controllare se si blocca
	ida_aux2(Nodo,Figli,Threshold,[Nodo],Res),!.

/* 
	ricerco in profondita, se fallisce:
		controllo di non essermi bloccato e riparto da sopra
	DOMANDA: i cut son da rivedere, non so perchè funzionano
 */
ida_aux2(_,Figli,Threshold,Espansi,Res):-  
	ricerca_in_profondita(Figli,Threshold,Espansi,Res),!.
ida_aux2(Nodo,_,_,_,Res):-
	thresholdCheck(Vs),
	prossimoThreshold(Ps),
	write("vecchio: "),write(Vs),		
	write("   nuovo: "),write_ln(Ps),
	write_ln(Vs =\= Ps),
	(Vs =\= Ps ; Vs =:= 0),!,
	ida_aux(Nodo,Res),!.		%DOMANDA: perchèquesta riga causa casi di backtracking con altre soluzioni senza cut?

/* 
	ricerca in profondità
*/
%ricerca_in_profondita([],_,_,_):- !,fail.   % DOMANDA: perchè questa riga causa casi di backtracking con altre soluzioni?
ricerca_in_profondita([nodo(N,Az)|_],_,_,Az):-finale(N),!.		%se è nodo finale
ricerca_in_profondita([nodo(N,Az)|Figli],Threshold,Espansi,Res):-	%se il nodo espanso ha costo F <Threshold allora espando figli
	calcola_F(nodo(N,Az),F),
	F =< Threshold,!,
	generaFigli(nodo(N,Az),Espansi,FigliDiN),%write("genero i figli di "+ N + " "),write_ln(FigliDiN),
	append(FigliDiN,Figli,NuoviFigli),
	append(FigliDiN,Espansi,NuoviEspansi),
	ricerca_in_profondita(NuoviFigli,Threshold,NuoviEspansi,Res).
ricerca_in_profondita([nodo(N,Az)|Figli],Threshold,Espansi,Res):-	%se il nodo espanso ha costo > Threshold, non lo espando e aggiorno soglia
	prossimoThreshold(ProssimoThreshold),
	calcola_F(nodo(N,Az),F),
	(F < ProssimoThreshold ; ProssimoThreshold == Threshold),!,	 % se il consto F è minore dell'attuale soglia salvo F come soglia
	retract(prossimoThreshold(ProssimoThreshold)),
	asserta(prossimoThreshold(F)),%write("nuovo prossimoThreshold: "),write(F + " "),writeln(N),
	ricerca_in_profondita(Figli,Threshold,Espansi,Res).		% invece di usare una lista di valori tengo traccia solo del Threshold
ricerca_in_profondita([_|Tail],Threshold,Espansi,Res):-
	ricerca_in_profondita(Tail,Threshold,Espansi,Res),!.
	
/*
	Funzioni di supporto
*/
	
/* espando i figli del nodo N */
generaFigli(nodo(N,Azioni),Espansi,Result):- 
	findall(Azione,applicabile(Azione,N),ListaApplicabili), % cerco le azioni applicabili al nodo N
	generaFigli_aux(nodo(N,Azioni),ListaApplicabili,Espansi,Result).
	
generaFigli_aux(_,[],_,[]).
generaFigli_aux(nodo(N,Azioni),[Azione|AltreAzioni],Espansi,[nodo(Figlio,AzioniFiglio)|ListaFigli]):-
	trasforma(Azione,N,Figlio), % operazione per generare il figlio dato un nodo e un'azione
	\+isMember(Figlio,Azioni,Espansi),!,
	append(Azioni,[Azione],AzioniFiglio),
	generaFigli_aux(nodo(N,Azioni),AltreAzioni,Espansi,ListaFigli).
generaFigli_aux(nodo(S,Azioni),[_|AltreAzioni],Espansi,FigliTail):- 
	generaFigli_aux(nodo(S,Azioni),AltreAzioni,Espansi,FigliTail).
		

/* controlla se il nodo è contenuto con funzione costo maggiore*/
isMember(N,Az,[nodo(N1,Az2)|_]):-
	N == N1,
	length(Az,A1),
	length(Az2,A2),
	A2 =< A1 + 1,!.	
isMember(N,Az,[_|Tail]):-
	isMember(N,Az,Tail).
		
/* funzione F*/
calcola_F(nodo(pos(X,Y),Az),F):-
	calcola_G(Az,G),
	calcola_H(pos(X,Y),H),
	F is G + H.
	
/* funzione costo */
calcola_G(Az,G):-length(Az,G).
	
/* funzione euristica */
calcola_H(pos(X,Y),H):-
	finale(pos(Xf,Yf)),
	DistX is Xf - X,
	DistY is Yf - Y,
	H is (abs(DistX)+abs(DistY)).
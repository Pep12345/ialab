/*
Sia s il nodo iniziale
● Sia T l’insieme dei nodi obiettivo (target)
● segna s come APERTO e calcola f (s);
● seleziona il nodo APERTO n avente valutazione minima (i pari merito dovrebbero
	essere risolti a vantaggio degli eventuali t ∈ T );
● se n appartiene a T, marca n CHIUSO e temina;
● altrimenti marca n CHIUSO e applica l’operatore successore Γ a n; poi calcola il
	valore di f per tutti i successori n’e :
		– marca come APERTI quei successori n’che non risultano già CHIUSI:
		– rimarca come APERTI quei successori n’che erano CHIUSI ma per cui è stato
		calcolato un valore f più basso di quello calcolato in precedenza (cioè sono stati
		raggiunti tramite un percorso migliore)
*/


/* in questa soluzione rappresento ogni nodo come:
nodo(posizione, costo G, costo H, percorso per raggiungerlo)
G = costo funzione, quanto costa arivare a quel nodo, ovvero la lunghezza del percoso
H = euristica, quanto si suppone costi arrivare da li alla fine
*/

a_star(Result):-
	iniziale(Start),
	calcola_H(Start,H),
	a_star_aux([nodo(Start,0,H,[])],[],Result).
	
a_star_aux(Opens,Closed,Result):-
	min(Opens,CurrentMin), % mi salva in CurrentMin il nodo con costo (g+h) minore da cui parte la ricerca
	search(Opens,Closed,CurrentMin,Result).
	
/* tolgo currentMin dai nodi aperti, lo sposto in chiusi, aggiungo figli e cerco */
search(_,_,nodo(CurrentMin,_,_,Actions),Actions):-finale(CurrentMin),!.   
search(Opens,Closed,CurrentMin,Result):-
	subtract(Opens,[CurrentMin],NewOpens), % cancella CurrentMin da opens
	generateChild(CurrentMin,Figli),
	search_aux(NewOpens,[CurrentMin|Closed],Figli,ResOpens,ResClosed), % aggiunge figli in open/closed 	
	a_star_aux(ResOpens,ResClosed,Result).
	
/* marca i figli come aperti se non son già in lista chiusi altrimenti ricontrolla la funzione costo
 	se il nuovo pecorso per tale nodo ha un costo minore viene riaperto
*/
search_aux(O,C,[],O,C).	
search_aux(Open,Closed,[F|Figli],ResOpens,ResClosed):- 
	\+isMember(F,Closed),!,
	search_aux([F|Open],Closed,Figli,ResOpens,ResClosed).
search_aux(Open,Closed,[nodo(Nodo,G,H,Actions)|Figli],ResOpens,ResClosed):- % se Nodo è già closed ma troviamo una F migliore
	F is G + H,
	getNodeFromList(Nodo,Closed,nodo(Nodo,GNodo,HNodo,AzioniNodo)),
	FNodo is GNodo + HNodo,
	F < FNodo,!,
	subtract(nodo(Nodo,GNodo,HNodo,AzioniNodo),Closed,NewClosed),
	search_aux([nodo(Nodo,G,H,Actions)|Open],NewClosed,Figli,ResOpens,ResClosed).
search_aux(Open,Closed,[_|Figli],ResOpens,ResClosed):-
	search_aux(Open,Closed,Figli,ResOpens,ResClosed).

/*
	controlla se un nodo è parte di una lista
*/
isMember(nodo(N,_,_,_),[nodo(N1,_,_,_)|_]):-
	N == N1,!.
isMember(nodo(N,G,H,Actions),[_|Tail]):-
	isMember(nodo(N,G,H,Actions),Tail).
	
/*
	estrae un nodo da una lista
	getNodeFromList(NodeToSearch,ListWithNode,Result)	  
*/ 
getNodeFromList(Node,[nodo(N1,G1,H1,_)|_],nodo(N1,G1,H1,_)):-
	Node == N1,!.
getNodeFromList(Node,[_|Tail],Res):-
	getNodeFromList(Node,Tail,Res).

/*
	resituisco tutti i nodi raggiungibili dato un nodo
*/
generateChild(nodo(N,G,H,Actions),Result):-  
	findall(Action,applicabile(Action,N),ListaApplicabili),  % cerco quali azioni sono applicabili al nodo
	generateChild_aux(nodo(N,G,H,Actions),ListaApplicabili,Result).
	
generateChild_aux(_,[],[]).
generateChild_aux(nodo(N,G,H,ActSequence),[Action|ActionResult],[nodo(Figlio,GFiglio,HFiglio,SonActions)|ListaFigli]):-
	trasforma(Action,N,Figlio),
	GFiglio is G+1,
	calcola_H(Figlio,HFiglio),
	append(ActSequence,[Action],SonActions), % action+actsequence
	generateChild_aux(nodo(N,G,H,ActSequence),ActionResult,ListaFigli).
	
/* funzione euristica, manhattan */
calcola_H(pos(X,Y),H):-
	finale(pos(Xf,Yf)),
	DistX is Xf - X,
	DistY is Yf - Y,
	H is (abs(DistX)+abs(DistY)).
	
/* cerco il nodo con funzione costo minore data una lista */
min([Head|Tail],MinNode):-
	min_aux(Tail,Head,MinNode).
	
min_aux([],CurrentMin,CurrentMin).
min_aux([Head|Tail],CurrentMin,Result):-
	lessthen(Head,CurrentMin),!,
	min_aux(Tail,Head,Result).
min_aux([_|Tail],CurrentMin,Result):-
	min_aux(Tail,CurrentMin,Result).

	
lessthen(nodo(_,G1,H1,_),nodo(_,G2,H2,_)):-
	(G1 + H1) < (G2 + H2).

	
	












% nodo(pos(x,y),g,h,ListaAzioni) f = g+h
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

a_star(Result):-
	iniziale(Start),
	calcola_H(Start,H),
	search([nodo(Start,0,H,[])],[],Result).
	
% search( lista-aperti, lista-chiusi, Risultato)
search(Opens,_,Azioni):-min(Opens,nodo(FNodeMin,_,_,Azioni)),finale(FNodeMin),!.   % non so se è corretto finire subito, forse deve guardare altri percorsi
search(Opens,Closed,Result):-
	min(Opens,FNodeMin),
	subtract(Opens,[FNodeMin],NewOpens), % cancella fnodemin da opens
	generaFigli(FNodeMin,Figli),
	boh(NewOpens,[FNodeMin|Closed],Figli,ResOpens,ResClosed), % aggiunge figli in open/closed 	
	search(ResOpens,ResClosed,Result).
	
boh(O,C,[],O,C).	
boh(Open,Closed,[F|Figli],ResOpens,ResClosed):-  % marco come aperti i figli che non sono nella lista chiusi
	\+isMember(F,Closed),!,
	boh([F|Open],Closed,Figli,ResOpens,ResClosed).
boh(Open,Closed,[nodo(Nodo,G,H,Azioni)|Figli],ResOpens,ResClosed):- % se Nodo è già closed ma troviamo una F migliore
	F is G + H,
	getNode(Nodo,Closed,nodo(Nodo,GNodo,HNodo,AzioniNodo)),
	FNodo is GNodo + HNodo,
	F<FNodo,!,
	subtract(nodo(Nodo,GNodo,HNodo,AzioniNodo),Closed,NewClosed),
	boh([nodo(Nodo,G,H,Azioni)|Open],NewClosed,Figli,ResOpens,ResClosed).
boh(Open,Closed,[_|Figli],ResOpens,ResClosed):-
	boh(Open,Closed,Figli,ResOpens,ResClosed).


isMember(nodo(N,G,H,Azioni),[nodo(N1,_,_,_)|Tail]):-
	N == N1;
	isMember(nodo(N,G,H,Azioni),Tail).
	
getNode(Node,[nodo(N1,G1,H1,_)|_],nodo(N1,G1,H1,_)):-
	Node == N1,!.
getNode(Node,[_|Tail],Res):-
	getNode(Node,Tail,Res).

generaFigli(nodo(N,G,H,Azioni),Result):-
	findall(Azione,applicabile(Azione,N),ListaApplicabili),
	generaFigli_aux(nodo(N,G,H,Azioni),ListaApplicabili,Result).
	
generaFigli_aux(_,[],[]).
generaFigli_aux(nodo(N,G,H,Azioni),[Azione|AltreAzioni],[nodo(Figlio,GFiglio,HFiglio,AzioniFiglio)|ListaFigli]):-
	trasforma(Azione,N,Figlio),
	GFiglio is G+1,
	calcola_H(Figlio,HFiglio),
	append(Azioni,[Azione],AzioniFiglio),
	generaFigli_aux(nodo(N,G,H,Azioni),AltreAzioni,ListaFigli).
	
calcola_H(pos(X,Y),H):-
	finale(pos(Xf,Yf)),
	DistX is Xf - X,
	DistY is Yf - Y,
	H is (abs(DistX)+abs(DistY)).
	
min([Head|Tail],NodoMinimo):-
	min_aux(Tail,Head,NodoMinimo).
	
min_aux([],MinAttuale,MinAttuale).
min_aux([Head|Tail],MinAttuale,Result):-
	lessthen(Head,MinAttuale),!,
	min_aux(Tail,Head,Result).
min_aux([_|Tail],MinAttuale,Result):-
	min_aux(Tail,MinAttuale,Result).

/*
min_aux([Head|Tail],MinAttuale,Result):-
	lessthen(Head,MinAttuale,Res), min_aux(Tail,Head,Result);
	min_aux(Tail,MinAttuale,Result).
*/
	
lessthen(nodo(_,G1,H1,_),nodo(_,G2,H2,_)):-
	(G1 + H1) < (G2 + H2).

	
	













ida(Result):-
	iniziale(Start),
	calcola_H(Start,H),
	asserta(minimo(0)),
	ida_aux(nodo(Start,0,H,[]),Result).
	
ida_aux(Nodo,Res):-
	minimo(OldMin),
	generaFigli(Nodo,Figli),
	retract(minimo(OldMin)),
	asserta(minimo(0)),
	ida_aux2(Nodo,Figli,OldMin,Res).

ida_aux2(_,Figli,OldMin,Res):-
	x(Figli,OldMin,Res),!.
ida_aux2(Nodo,_,_,Res):-
	ida_aux(Nodo,Res).
%	(x(Figli,OldMin,Res);	ida_aux(Nodo,Res)).

x([],_,_):- !,fail.
x([nodo(N,_,_,Az)|_],_,Az):-finale(N),!.		%se è nodo finale
x([nodo(N,G,H,Az)|Figli],PesoSoglia,Res):-					%se il nodo espanso ha costo F <PesoSoglia allora espando figli
	F is G + H,
	F =< PesoSoglia,!,
	generaFigli(nodo(N,G,H,Az),FigliDiN),
	append(FigliDiN,Figli,NuoviFigli),
	x(NuoviFigli,PesoSoglia,Res).
x(nodo(_,G,H,_),PesoSoglia,Res):-					%se il nodo espanso ha costo > PesoSoglia, non lo espando e salvo il suo costo
	minimo(NewPesoSoglia),
	F is G + H,
	(F < NewPesoSoglia ; NewPesoSoglia == 0),!,	
	retract(minimo(NewPesoSoglia)),
	asserta(minimo(F)),
	x(Figli,PesoSoglia,Res).		% invece di usare una lista di valori tengo traccia solo del PesoSoglia
x([_|Tail],PesoSoglia,Res):-
	x(Tail,PesoSoglia,Res).
	
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

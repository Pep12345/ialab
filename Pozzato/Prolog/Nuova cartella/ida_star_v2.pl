
ida(Result):-
	iniziale(Start),
	asserta(minimo(0)),
	ida_aux(nodo(Start,[]),Result).
	
ida_aux(Nodo,Res):-
	minimo(OldMin),
	generaFigli(Nodo,[],Figli),
	retract(minimo(OldMin)),%write("riparto da 0 con min: "),write_ln(OldMin),
	asserta(minimo(0)),
	ida_aux2(Nodo,Figli,OldMin,Res).

ida_aux2(_,Figli,OldMin,Res):-
	x(Figli,OldMin,Res),!.
ida_aux2(Nodo,_,_,Res):-
	ida_aux(Nodo,Res).
%	(x(Figli,OldMin,Res);	ida_aux(Nodo,Res)).

x([],_,_):- !,fail.
x([nodo(N,Az)|_],_,Az):-finale(N),!.		%se è nodo finale
x([nodo(N,Az)|Figli],PesoSoglia,Res):-					%se il nodo espanso ha costo F <PesoSoglia allora espando figli
	calcola_F(nodo(N,Az),F),
	F =< PesoSoglia,!,
	generaFigli(nodo(N,Az),Figli,FigliDiN),
	append(FigliDiN,Figli,NuoviFigli),
	x(NuoviFigli,PesoSoglia,Res).
x([nodo(N,Az)|Figli],PesoSoglia,Res):-					%se il nodo espanso ha costo > PesoSoglia, non lo espando e salvo il suo costo
	minimo(NewPesoSoglia),
	calcola_F(nodo(N,Az),F),
	(F < NewPesoSoglia ; NewPesoSoglia == 0),!,	
	retract(minimo(NewPesoSoglia)),
	asserta(minimo(F)),%write("nuovo minimo: "),write(F + " "),writeln(N),
	x(Figli,PesoSoglia,Res).		% invece di usare una lista di valori tengo traccia solo del PesoSoglia
x([_|Tail],PesoSoglia,Res):-
	x(Tail,PesoSoglia,Res).
	
generaFigli(nodo(N,Azioni),Visitati,Result):-
	findall(Azione,applicabile(Azione,N),ListaApplicabili),
	generaFigli_aux(nodo(N,Azioni),ListaApplicabili,Visitati,Result).
	
generaFigli_aux(_,[],_,[]).
generaFigli_aux(nodo(N,Azioni),[Azione|AltreAzioni],[nodo(Figlio,AzioniFiglio)|Visitati],[nodo(Figlio,AzioniFiglio)|ListaFigli]):-
	trasforma(Azione,N,Figlio),
	\+member(Figlio,Visitati),!,
	append(Azioni,[Azione],AzioniFiglio),
	generaFigli_aux(nodo(N,Azioni),AltreAzioni,Visitati,ListaFigli).
generaFigli_aux(nodo(N,Azioni),[_|AltreAzioni],Visitati,ListaFigli):-
	generaFigli_aux(nodo(N,Azioni),AltreAzioni,Visitati,ListaFigli).

calcola_F(nodo(pos(X,Y),Az),F):-
	calcola_G(Az,G),
	calcola_H(pos(X,Y),H),
	F is G + H.
	
calcola_G(Az,G):-length(Az,G).
	
calcola_H(pos(X,Y),H):-
	finale(pos(Xf,Yf)),
	DistX is Xf - X,
	DistY is Yf - Y,
	H is (abs(DistX)+abs(DistY)).
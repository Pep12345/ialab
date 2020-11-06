
ida_star(Soluzione):-
	iniziale(Start),
	asserta(minimo(0)),
	ida_aux(nodo(Start,[]),Soluzione).

ida_aux(Nodo,Res):-
	minimo(OldMin),
	generaFigli(Nodo,[],Figli),
	retract(minimo(OldMin)),%write("riparto da 0 con min: "),write_ln(OldMin),
	asserta(minimo(0)),
	ida_aux2(Nodo,Figli,OldMin,[Nodo],Res).

ida_aux2(_,Figli,OldMin,Espansi,Res):-  % non si può mettere in una riga sola con ; perchè si perderebbe il cut che evita backtracking
	ida_aux3(Figli,OldMin,Espansi,Res),!.
ida_aux2(Nodo,_,_,_,Res):-
	ida_aux(Nodo,Res),!.		%DOMANDA: perchèquesta riga causa casi di backtracking con altre soluzioni senza cut?

%ida_aux3([],_,_,_):- !,fail.   % DOMANDA: perchè questa riga causa casi di backtracking con altre soluzioni?
ida_aux3([nodo(N,Az)|_],_,_,Az):-finale(N),!.		%se è nodo finale
ida_aux3([nodo(N,Az)|Figli],PesoSoglia,Espansi,Res):-					%se il nodo espanso ha costo F <PesoSoglia allora espando figli
	calcola_F(nodo(N,Az),F),
	F =< PesoSoglia,!,
	generaFigli(nodo(N,Az),Espansi,FigliDiN),%write("genero i figli di "+ N + " "),write_ln(FigliDiN),
	append(FigliDiN,Figli,NuoviFigli),
	append(FigliDiN,Espansi,NuoviEspansi),
	ida_aux3(NuoviFigli,PesoSoglia,NuoviEspansi,Res).
ida_aux3([nodo(N,Az)|Figli],PesoSoglia,Espansi,Res):-					%se il nodo espanso ha costo > PesoSoglia, non lo espando e salvo il suo costo
	minimo(NewPesoSoglia),
	calcola_F(nodo(N,Az),F),
	(F < NewPesoSoglia ; NewPesoSoglia == 0),!,	
	retract(minimo(NewPesoSoglia)),
	asserta(minimo(F)),%write("nuovo minimo: "),write(F + " "),writeln(N),
	ida_aux3(Figli,PesoSoglia,Espansi,Res).		% invece di usare una lista di valori tengo traccia solo del PesoSoglia
ida_aux3([_|Tail],PesoSoglia,Espansi,Res):-
	ida_aux3(Tail,PesoSoglia,Espansi,Res).%,!.
	
generaFigli(nodo(N,Azioni),Espansi,Result):- 
	findall(Azione,applicabile(Azione,N),ListaApplicabili),
	generaFigli_aux(nodo(N,Azioni),ListaApplicabili,Espansi,Result).
	
generaFigli_aux(_,[],_,[]).
generaFigli_aux(nodo(N,Azioni),[Azione|AltreAzioni],Espansi,[nodo(Figlio,AzioniFiglio)|ListaFigli]):-
	trasforma(Azione,N,Figlio),
	\+isMember(Figlio,Azioni,Espansi),!,
	append(Azioni,[Azione],AzioniFiglio),
	generaFigli_aux(nodo(N,Azioni),AltreAzioni,Espansi,ListaFigli).
generaFigli_aux(nodo(S,Azioni),[_|AltreAzioni],Espansi,FigliTail):- 
	generaFigli_aux(nodo(S,Azioni),AltreAzioni,Espansi,FigliTail).
		

isMember(N,Az,[nodo(N1,Az2)|_]):-
	N == N1,
	length(Az,A1),
	length(Az2,A2),
	A2 =< A1 + 1,!.	
isMember(N,Az,[_|Tail]):-
	isMember(N,Az,Tail).
		
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
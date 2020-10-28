% Visita in ampiezza
bfs(Soluzione):-
	iniziale(S),
	bfs_aux([nodo(S,[])],[],Soluzione).
	
% bfs_aux(Coda,Visitati,Soluzione)
bfs_aux([nodo(S,Azioni)|_],_,Azioni):-finale(S),!.
bfs_aux([nodo(S,Azioni)|Tail],Visitati,Soluzione):-
	findall(Azione,applicabile(Azione,S),ListaApplicabili), %lista di azioni applicabili in S
	generaFigli(nodo(S,Azioni),ListaApplicabili,Visitati,ListaFigli),
	append(Tail,ListaFigli,NuovaCoda),
	bfs_aux(NuovaCoda,[S|Visitati],Soluzione).

generaFigli(_,[],_,[]).
generaFigli(nodo(S,Azioni),[Azione|AltreAzioni],Visitati,[nodo(SNuovo,[Azione|Azioni])|FigliTail]):-
	trasforma(Azione,S,SNuovo),
	\+member(SNuovo,Visitati),!, % uso cat come if/else
	generaFigli(nodo(S,Azioni),AltreAzioni,Visitati,FigliTail).
generaFigli(nodo(S,Azioni),[_|AltreAzioni],Visitati,FigliTail):- % viene eseguita soltanto se \+member fallisce, altrimenti il cut lo impedisce
	generaFigli(nodo(S,Azioni),AltreAzioni,Visitati,FigliTail).
	

/*
nodo(S,[]) è una struttura complessa che conterrà lo stato corrente e una lista di azioni per giungere in quello stato
di conseguenza la coda è una lista di nodi 

findall(a,b,c): restituisce tutte i possibili valori di a per cui abbia successo il predicato b (c è il risultato).
findall(X,member(X,[1,2,3,4]),Ris)-> Ris= [1,2,3,4]
*/

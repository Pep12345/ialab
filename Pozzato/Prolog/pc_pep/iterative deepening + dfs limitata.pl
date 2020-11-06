% dfs_aux(S,ListaAzioni,Visitati,Soglia)
prova(Soluzione):-
	prova_aux(Soluzione,1).

prova_aux(Soluzione,Soglia):-
	depth_limit_search(Soluzione,Soglia),!.
prova_aux(Soluzione,Soglia):-
	NuovaSoglia is Soglia+1,
	prova_aux(Soluzione,NuovaSoglia),!.

depth_limit_search(Soluzione,Soglia):-
	iniziale(S),
	dfs_aux(S,Soluzione,[S],Soglia).

dfs_aux(S,[],_,_):-finale(S).
dfs_aux(S,[Azione|AzioniTail],Visitati,Soglia):-
	Soglia>0,
	applicabile(Azione,S),
	trasforma(Azione,S,SNuovo),
	\+member(SNuovo,Visitati),
	NuovaSoglia is Soglia-1,
	dfs_aux(SNuovo,AzioniTail,[SNuovo|Visitati],NuovaSoglia).
	
/* 
due variazioni da fare:

1) algoritmo iterativo dove parte da soglia 1 ed aumenta ad ogni fallimenti [iterative]
2) algoritmo a* che sfrutta euristica per determinare quanto buono è lo stato

*/
iterative_deepening(Soluzione):-
  iterative_deepening_aux(Soluzione,1).

iterative_deepening_aux(Soluzione,Soglia):-
  depth_limit_search(Soluzione,Soglia),!.	
iterative_deepening_aux(Soluzione,Soglia):-
  num_colonne(NumCol),
  num_righe(NumRighe),
  NuovaSoglia is Soglia+1,
  NuovaSoglia < (log(NumCol * NumRighe) * 6), 
  iterative_deepening_aux(Soluzione,NuovaSoglia).

/* prove soglie:	
	- NumCol * NumRighe / N ... N = 2,3,4...8 ma non ci piaceva perchè dava soglie troppo grandi per labirinti grandi
	- log(NumCol * NumRighe) * N) ... N = 4,5...8 forse troppo corta
*/
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
  

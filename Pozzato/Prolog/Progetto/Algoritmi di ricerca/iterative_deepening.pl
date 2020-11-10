%aggiungere soglia limite
% nota: trovare un modo per farlo finire quando non c'è la soluzione
iterative_deepening(Soluzione):-
  iterative_deepening_aux(Soluzione,1).

iterative_deepening_aux(Soluzione,Soglia):-
  depth_limit_search(Soluzione,Soglia),!.	% questo cut serve per cercare soluzioni alternative senza aumentare soglia
iterative_deepening_aux(Soluzione,Soglia):-
  NuovaSoglia is Soglia+1,
  iterative_deepening_aux(Soluzione,NuovaSoglia).

depth_limit_search(Soluzione,Soglia):-
  iniziale(S),
  dfs_aux(S,Soluzione,[S],Soglia).

dfs_aux(S,[],_,_):-finale(S).	% secondo me il cut qua non serve perchè trovato il finale fa backtracking, col cut tagliamo eventuali soluzioni alternative ottenibili da ;
dfs_aux(S,[Azione|AzioniTail],Visitati,Soglia):-
  Soglia>0,
  applicabile(Azione,S),
  trasforma(Azione,S,SNuovo),
  \+member(SNuovo,Visitati),
  NuovaSoglia is Soglia-1,
  dfs_aux(SNuovo,AzioniTail,[SNuovo|Visitati],NuovaSoglia).
  

bfs(Soluzione):-
  iniziale(S),
  bfs_aux([nodo(S,[])],[],Soluzione).
%bfs_aux(Coda,Visitati,Soluzione).
%Coda=[nodo(S,Azioni)|...]
bfs_aux([nodo(S,Azioni)|_],_,Azioni):-finale(S),!.
bfs_aux([nodo(S,Azioni)|Tail],Visitati,Soluzione):-
  findall(Azione,applicabile(Azione,S),ListaApplicabili),
  generafigli(nodo(S,Azioni),ListaApplicabili,Visitati,ListaFigli),
  append(Tail,ListaFigli,NuovaCoda),
  bfs_aux(NuovaCoda,[S|Visitati],Soluzione).

generafigli(_,[],_,[]).
generafigli(nodo(S,AzioniPerS),[Azione|AltreAzioni],Visitati,[nodo(SNuovo,[Azione|AzioniPerS])|FigliTail]):-
  trasforma(Azione,S,SNuovo),
  \+member(SNuovo,Visitati),!,
  generafigli(nodo(S,AzioniPerS),AltreAzioni,Visitati,FigliTail).
generafigli(nodo(S,AzioniPerS),[_|AltreAzioni],Visitati,FigliTail):-
  generafigli(nodo(S,AzioniPerS),AltreAzioni,Visitati,FigliTail).

  

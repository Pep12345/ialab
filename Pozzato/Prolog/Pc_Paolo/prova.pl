order(X):-
iniziale(S),
manhattan(Euristic,S),
manhattan(Euristic1,pos(2,3)),
manhattan(Euristic2,pos(6,6)),
T=[Euristic-nodo(1,7),Euristic1-nodo(1,4),Euristic2-nodo(1,3),Euristic2-nodo(1,3)],
keysort(T,Lista),
risolvi(X,T).


risolvi(X,[A-nodo(_,_)|_]):-
X=A.

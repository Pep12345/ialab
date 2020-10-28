manhattan(Distanza,Nodo):-
finale(F),
distanza_x(Distanzax,Nodo,F),
distanza_y(Distanzay,Nodo,F),
Distanza is Distanzax+Distanzay.

manhattan(Distanza,Nodo1,Nodo2):-
distanza_x(Distanzax,Nodo1,Nodo2),
distanza_y(Distanzay,Nodo1,Nodo2),
Distanza is Distanzax+Distanzay.


distanza_x(Distanzax,pos(X1,_),pos(X2,_)):-
distanza(Distanzax,X1,X2).

distanza_y(Distanzay,pos(_,Y1),pos(_,Y2)):-
distanza(Distanzay,Y1,Y2).

distanza(Distanza,N1,N2):-
N1>N2,!,
Distanza is N1-N2.
distanza(Distanza,N1,N2):-
Distanza is N2-N1.



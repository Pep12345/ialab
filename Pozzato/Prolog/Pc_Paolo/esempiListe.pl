/* Predicato per calcolare la somma degli
   elementi di una lista
   somma(Lista,SommaElementi)
         Lista->input
         SommaElementi->output
         is->operatore assegnamento per funzioni aritmetiche
   */
somma([],0).
somma([Head|Tail],Somma):-somma(Tail,SommaTail),Somma is Head+SommaTail.

%Inversione di una lista: formulazione inefficiente
inverti([],[]).
inverti([Head|Tail],Res):-inverti(Tail,TailInv),append(TailInv,[Head],Res).

%Inversione di una lista: formulazione efficiente
invertiAux([],Temp,Temp).
invertiAux([Head|Tail],Temp,Res):-invertiAux(Tail,[Head|Temp],Res).
invertiEff(Lista,ListaInvertita):-invertiAux(Lista,[],ListaInvertita).

%Unione di due liste che rappresentano insiemi
%Unione(A,B,AUnitoB)
unione([],B,B).
unione(A,[],A).
unione([X|TailA],B,Unione):-member(X,B),unione(TailA,B,Unione).
unione([X|TailA],B,[X|Unione]):-true,\+member(X,B),unione(TailA,B,Unione).
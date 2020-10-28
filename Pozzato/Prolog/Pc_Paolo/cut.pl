/* Conta quanti elementi positivi
    ci sono in una lista */
contaPositivi([],0).
contaPositivi([Head|Tail],Positivi):-
     Head>0,!,
     contaPositivi(Tail,PositiviInTail),
     Positivi is PositiviInTail+1.
contaPositivi([_|Tail],PositiviInTail):-
     contaPositivi(Tail,PositiviInTail).
/*
più costosto
contaPositivi([Head|Tail],Positivi):-
     \+Head>0,
     contaPositivi(Tail,PositiviInTail),
     Positivi is PositiviInTail+1.
*/

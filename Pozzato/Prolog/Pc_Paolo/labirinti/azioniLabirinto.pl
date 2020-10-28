%applicabile(Az,S) true se Az è applicabile ad S
applicabile(sud,pos(Riga,Colonna)):-
      num_righe(NR),
      Riga<NR,
      RigaSotto is Riga+1,
      \+occupata(pos(RigaSotto,Colonna)).
applicabile(est,pos(Riga,Colonna)):-
      num_colonne(NC),
      Colonna<NC,
      ColonnaAccanto is Colonna+1,
      \+occupata(pos(Riga,ColonnaAccanto)).

applicabile(ovest,pos(Riga,Colonna)):-
      Colonna>1,
      ColonnaAccanto is Colonna-1,
      \+occupata(pos(Riga,ColonnaAccanto)).

applicabile(nord,pos(Riga,Colonna)):-
      Riga>1,
      RigaSopra is Riga-1,
      \+occupata(pos(RigaSopra,Colonna)).


%trasforma(Az,S,S_Nuovo)
trasforma(est,pos(Riga,Colonna),pos(Riga,ColonnaAccanto)):-
      ColonnaAccanto is Colonna+1.
trasforma(ovest,pos(Riga,Colonna),pos(Riga,ColonnaAccanto)):-
      ColonnaAccanto is Colonna-1.
trasforma(nord,pos(Riga,Colonna),pos(RigaSopra,Colonna)):-
      RigaSopra is Riga-1.
trasforma(sud,pos(Riga,Colonna),pos(RigaSotto,Colonna)):-
      RigaSotto is Riga+1.



% le azioni è meglio farle in un file separato in caso volessimo modificare il labirinto

applicabile(est,pos(Riga,Colonna)):-
	num_col(NC),
	Colonna<NC,
	ColonnaAccanto is Colonna+1,
	\+occupata(pos(Riga,ColonnaAccanto)).

applicabile(sud,pos(Riga,Colonna)):-
	num_righe(NR),
	Riga<NR,
	RigaSotto is Riga+1,
	\+occupata(pos(RigaSotto,Colonna)).


	
applicabile(ovest,pos(Riga,Colonna)):-
	Colonna>1,
	ColonnaAccanto is Colonna-1,
	\+occupata(pos(Riga,ColonnaAccanto)).
	
applicabile(nord,pos(Riga,Colonna)):-
	Riga>1,
	RigaSopra is Riga-1,
	\+occupata(pos(RigaSopra,Colonna)).

	
trasforma(est,pos(R,C),pos(R,C1)):-
	C1 is C+1.
	
trasforma(ovest,pos(R,C),pos(R,C1)):-
	C1 is C-1.
	
trasforma(nord,pos(R,C),pos(R1,C)):-
	R1 is R-1.
	
trasforma(sud,pos(R,C),pos(R1,C)):-
	R1 is R+1.
	
%nuovo file per controllo

%depth_search
depth_search(Soluzione):-
	iniziale(S),
	%dfs(S,Soluzione).
	dfs(S,Soluzione,[S]).

%dfs
dfs(S,[]):-finale(S),!. %trovo la soluzione e blocco eventuali altri cammini
dfs(S,[Az|SequenzaAzioni]):-
	applicabile(Az,S),
	trasforma(Az,S,S_Nuovo),
	dfs(S_Nuovo,SequenzaAzioni).
	
%versione per evitare il loop, tengo traccia degli stati già visitati.
dfs(S,[],_):-finale(S),!. %trovo la soluzione e blocco eventuali altri cammini(magari altre soluzioni)
dfs(S,[Az|SequenzaAzioni],Visitati):-
	applicabile(Az,S),
	trasforma(Az,S,S_Nuovo),
	\+member(S_Nuovo,Visitati),
	dfs(S_Nuovo,SequenzaAzioni,[S_Nuovo|Visitati]).
	
%lo spostamento vero viene eseguito al termine del ragionamento, risale il percorso trovato e crea il percorso per spostarsi

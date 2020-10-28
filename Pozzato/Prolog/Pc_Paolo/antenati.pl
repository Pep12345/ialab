genitore(edoardo,gianni).
genitore(edoardo,clara).
genitore(edoardo,susanna).
genitore(edoardo,umberto).
genitore(edoardo,mariasole).
genitore(edoardo,giorgio).
genitore(edoardo,cristiana).
genitore(virginiaBourbon,gianni).
genitore(virginiaBourbon,clara).
genitore(virginiaBourbon,susanna).
genitore(virginiaBourbon,umberto).
genitore(virginiaBourbon,mariasole).
genitore(virginiaBourbon,giorgio).
genitore(virginiaBourbon,cristiana).
genitore(gianni,margherita).
genitore(marellaCaracciolo,margherita).
genitore(gianni,edoardo2).
genitore(marellaCaracciolo,edoardo2).
genitore(margherita,johnElkann).
genitore(alainElkann,johnElkann).
genitore(margherita,lapoElkann).
genitore(alainElkann,lapoElkann).
genitore(margherita,ginevraElkann).
genitore(alainElkann,ginevraElkann).
genitore(umberto,giovanni).
genitore(antonellaBechiPiaggio,giovanni).
genitore(umberto,andrea).
genitore(umberto,anna).
genitore(allegraCaracciolo,andrea).
genitore(allegraCaracciolo,anna).

antenato(X,Y):-genitore(X,Y).
antenato(X,Y):-genitore(X,Z),antenato(Z,Y).

fratelloGermano(X,Y):-
    genitore(PrimoGenitore,X),
    genitore(PrimoGenitore,Y),
    X \== Y,
    genitore(SecondoGenitore,X),
    genitore(SecondoGenitore,Y),
    PrimoGenitore \== SecondoGenitore.

fratelloUnilaterale(X,Y):-
    genitore(GenitoreComune,X),
    genitore(GenitoreComune,Y),
    X \== Y,
    genitore(AltroGenitoreX,X),
    GenitoreComune \== AltroGenitoreX,
    genitore(AltroGenitoreY,Y),
    GenitoreComune \== AltroGenitoreY,
    AltroGenitoreX \== AltroGenitoreY.
settimane(1..24).
giorni_per_settimana_full(lunedi;martedi;mercoledi;giovedi).
giorni_settimana(venerdi;sabato).
insegnamenti(project_Management;fondamenti_di_ICT_e_Paradigmi_di_Programmazione;linguaggi_di_markup;
             la_gestione_della_qualita;ambienti_di_sviluppo_e_linguaggi_client_side_per_il_web;
             progettazione_grafica_e_design_di_interfacce;progettazione_di_basi_di_dati;
             strumenti_e_metodi_di_interazione_nei_Social_media;acquisizione_ed_elaborazione_di_immagini_statiche_grafica;
             accessibilita_e_usabilita_nella_progettazione_multimediale;marketing_digitale;elementi_di_fotografia_digitale;
             risorse_digitali_per_il_progetto_collaborazione_e_documentazione;tecnologie_server_side_per_il_web;
             tecniche_e_strumenti_di_Marketing_digitale;introduzione_al_social_media_management;
             acquisizione_ed_elaborazione_del_suono;acquisizione_ed_elaborazione_di_sequenze_di_immagini_digitali;
             comunicazione_pubblicitaria_e_comunicazione_pubblica;semiologia_e_multimedialita;
             crossmedia_articolazione_delle_scritture_multimediali;grafica_3D;
             progettazione_e_sviluppo_di_applicazioni_web_su_dispositivi_mobile_I;
             progettazione_e_sviluppo_di_applicazioni_web_su_dispositivi_mobile_II;la_gestione_delle_risorse_umane;
             i_vincoli_giuridici_del_progetto_diritto_dei_media
            ).
professori(muzzetto;pozzato;gena;tomatis;micalizio;merranova;mazzei;giordani;zanchetta;vargiu;boniolo;damiano;
           suppini;valle;ghidelli;gabardi;santangelo;taddeo;gribaudo;schifanella;lombardo;travostino
	       ).
inizio_ora(8;9;10;11;12;14;15;16).

settimana_full_time(7;16).

professori_Insegnamento(project_Management,muzzetto).
professori_Insegnamento(fondamenti_di_ICT_e_Paradigmi_di_Programmazione,pozzato).
professori_Insegnamento(linguaggi_di_markup,gena).
professori_Insegnamento(la_gestione_della_qualita,tomatis).
professori_Insegnamento(ambienti_di_sviluppo_e_linguaggi_client_side_per_il_web,micalizio).
professori_Insegnamento(progettazione_grafica_e_design_di_interfacce,terranova).
professori_Insegnamento(progettazione_di_basi_di_dati,mazzei).
professori_Insegnamento(strumenti_e_metodi_di_interazione_nei_Social_media,giordani).
professori_Insegnamento(acquisizione_ed_elaborazione_di_immagini_statiche_grafica,zanchetta).
professori_Insegnamento(accessibilita_e_usabilita_nella_progettazione_multimediale,gena).
professori_Insegnamento(marketing_digitale,muzzetto).
professori_Insegnamento(elementi_di_fotografia_digitale,vargiu).
professori_Insegnamento(risorse_digitali_per_il_progetto_collaborazione_e_documentazione,boniolo).
professori_Insegnamento(tecnologie_server_side_per_il_web,damiano).
professori_Insegnamento(tecniche_e_strumenti_di_Marketing_digitale,zanchetta).
professori_Insegnamento(introduzione_al_social_media_management,suppini).
professori_Insegnamento(acquisizione_ed_elaborazione_del_suono,valle).
professori_Insegnamento(acquisizione_ed_elaborazione_di_sequenze_di_immagini_digitali,ghidelli).
professori_Insegnamento(comunicazione_pubblicitaria_e_comunicazione_pubblica,gabardi).
professori_Insegnamento(semiologia_e_multimedialita,santangelo).
professori_Insegnamento(crossmedia_articolazione_delle_scritture_multimediali,taddeo).
professori_Insegnamento(grafica_3D,gribaudo).
professori_Insegnamento(progettazione_e_sviluppo_di_applicazioni_web_su_dispositivi_mobile_I,pozzato).
professori_Insegnamento(progettazione_e_sviluppo_di_applicazioni_web_su_dispositivi_mobile_II,schifanella).
professori_Insegnamento(la_gestione_delle_risorse_umane,lombardo).
professori_Insegnamento(i_vincoli_giuridici_del_progetto_diritto_dei_media,travostino).


ore_insegnamento(project_Management,14).
ore_insegnamento(fondamenti_di_ICT_e_Paradigmi_di_Programmazione,14).
ore_insegnamento(linguaggi_di_markup,20).
ore_insegnamento(la_gestione_della_qualita,10).
ore_insegnamento(ambienti_di_sviluppo_e_linguaggi_client_side_per_il_web,20).
ore_insegnamento(progettazione_grafica_e_design_di_interfacce,10).
ore_insegnamento(progettazione_di_basi_di_dati,20).
ore_insegnamento(strumenti_e_metodi_di_interazione_nei_Social_media,14).
ore_insegnamento(acquisizione_ed_elaborazione_di_immagini_statiche_grafica,14).
ore_insegnamento(accessibilita_e_usabilita_nella_progettazione_multimediale,14).
ore_insegnamento(marketing_digitale,10).
ore_insegnamento(elementi_di_fotografia_digitale,10).
ore_insegnamento(risorse_digitali_per_il_progetto_collaborazione_e_documentazione,10).
ore_insegnamento(tecnologie_server_side_per_il_web,20).
ore_insegnamento(tecniche_e_strumenti_di_Marketing_digitale,10).
ore_insegnamento(introduzione_al_social_media_management,14).
ore_insegnamento(acquisizione_ed_elaborazione_del_suono,10).
ore_insegnamento(acquisizione_ed_elaborazione_di_sequenze_di_immagini_digitali,20).
ore_insegnamento(comunicazione_pubblicitaria_e_comunicazione_pubblica,14).
ore_insegnamento(semiologia_e_multimedialita,10).
ore_insegnamento(crossmedia_articolazione_delle_scritture_multimediali,20).
ore_insegnamento(grafica_3D,20).
ore_insegnamento(progettazione_e_sviluppo_di_applicazioni_web_su_dispositivi_mobile_I,10).
ore_insegnamento(progettazione_e_sviluppo_di_applicazioni_web_su_dispositivi_mobile_II,10).
ore_insegnamento(la_gestione_delle_risorse_umane,10).
ore_insegnamento(i_vincoli_giuridici_del_progetto_diritto_dei_media,10).

insegnamento_successivo(ambienti_di_sviluppo_e_linguaggi_client_side_per_il_web,fondamenti_di_ICT_e_Paradigmi_di_Programmazione).
insegnamento_successivo(progettazione_e_sviluppo_di_applicazioni_web_su_dispositivi_mobile_I,ambienti_di_sviluppo_e_linguaggi_client_side_per_il_web).
insegnamento_successivo(progettazione_e_sviluppo_di_applicazioni_web_su_dispositivi_mobile_II,progettazione_e_sviluppo_di_applicazioni_web_su_dispositivi_mobile_I).
insegnamento_successivo(tecnologie_server_side_per_il_web,progettazione_di_basi_di_dati).
insegnamento_successivo(ambienti_di_sviluppo_e_linguaggi_client_side_per_il_web,linguaggi_di_markup).
insegnamento_successivo(marketing_digitale,project_Management).
insegnamento_successivo(progettazione_grafica_e_design_di_interfacce,project_Management).
insegnamento_successivo(elementi_di_fotografia_digitale,acquisizione_ed_elaborazione_di_immagini_statiche_grafica).
insegnamento_successivo(acquisizione_ed_elaborazione_di_sequenze_di_immagini_digitali,elementi_di_fotografia_digitale).
insegnamento_successivo(grafica_3D,acquisizione_ed_elaborazione_di_immagini_statiche_grafica).




%ad ogni settimana  associamo i suoi giorni 
2{giorno_in_settimana(S,G):giorni_settimana(G)}2:-settimane(S).

%ad ogni settimana full time associamo i  giorni in più
4{giorno_in_settimana(S,G):giorni_per_settimana_full(G)} 4:-settimane(S),settimana_full_time(S).

%se non è sabato, abbiamo 8 ore
8{inizio_ora_giorno_settimana(S,G,O):inizio_ora(O)}8:-giorno_in_settimana(S,G),not G==sabato.

%se è sabato  abbiamo 4 o 5  ore
4{inizio_ora_giorno_settimana(S,G,O):inizio_ora(O)}5:-giorno_in_settimana(S,G),G==sabato.

%per ogni ora di ogni giorno di ogni settimana associo uno ed un solo insegnamento
1{lezione(S,G,O,I):insegnamenti(I)}1:-inizio_ora_giorno_settimana(S,G,O).

%un professore non può tenere corsi diversi nello stesso slot 
:-lezione(S,G,O,I),lezione(S,G,O,I2),I!=I2, professori_Insegnamento(I,P),professori_Insegnamento(I2,P).

%in un giorno un corso deve avere minimo 2 ore e massimo 4 ore(count)

%la somma delle durate  delle lezioni di un insegnamento deve essere uguale alle ore assegnate all'insegnamento(count)


%lo stesso docente non può fare più di 4 ore in un giorno(count)

%due blocchi liberi di due ore ciascuno

%project management deve essere finito prima della prima settimana_full_time(7)
:-lezione(S,_,_,I),I==project_Management,S>6.

%il primo giorno di lezione prevede che, nelle prime due ore, vi sia la presentazione del master
lezione(1,venerdi,8,presentazione_master).
lezione(1,venerdi,9,presentazione_master).

%prima lezione
prima_lezione(S,I):-lezione(S,G,O,I),lezione(S2,G2,O2,I),S<=S2.
%la prima lezione è unica
1{prima_lezione(S,I):lezione(S,G,O,I)}1:-insegnamenti(I).

%ultima lezione
ultima_lezione(S,I):-lezione(S,G,O,I),lezione(S2,G2,O2,I),S>=S2.
%ultima lezione è unica
1{ultima_lezione(S,I):lezione(S,G,O,I)}1:-insegnamenti(I).

%la prima lezione dell’insegnamento “Accessibilità e usabilità nella
%progettazione multimediale” deve essere collocata prima che siano
%terminate le lezioni dell’insegnamento “Linguaggi di markup”
:-prima_lezione(S,accessibilita_e_usabilita_nella_progettazione_multimediale),ultima_lezione(S2,linguaggi_di_markup),S>S2.

%se un corso è successivo ad un altro, la prima lezione del corso deve essere successiva all'ultima lezione 
%dell'altro corso
:-insegnamento_successivo(I,I2),prima_lezione(S,I),ultima_lezione(S2,I2),S<=S2.



#show lezione/4.


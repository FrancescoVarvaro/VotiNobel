package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {
	
	private List<Esame> esami;
	private Set <Esame> migliore; //non importa l'ordine!
	private double mediaMigliore;
	
	public Model() {
		EsameDAO dao = new EsameDAO();
		this.esami = dao.getTuttiEsami();
	}
	
	
	public Set<Esame> calcolaSottoinsiemeEsami(int m) {
//		RIPRISTINO SOLUZIONE MIGLIORE
		migliore = new HashSet<Esame>();  // INIZIALIZZO QUI PERCHE QUANDO RICHIAMO IL METODO RIPORTO LA LISTA A NUOVO
		mediaMigliore = 0;
		Set<Esame> parziale = new HashSet<Esame>();
//		cerca1(parziale,0,m);
		cerca2(parziale,0,m);
		return migliore;	
	}
	
	private void cerca2(Set<Esame> parziale, int L, int m) {
		int sommaCrediti = sommaCrediti(parziale);
		if(sommaCrediti > m)
			return;
		else if(sommaCrediti == m) {
//			soluzione VALIDA controlliamo se è la migliore fino a qui
			double mediaVoti = calcolaMedia(parziale);
			if(mediaVoti > mediaMigliore) {
				migliore = new HashSet<Esame>(parziale);
				mediaMigliore = mediaVoti;
			}
			
			return;
		}
//		Se arriviamo qui i crediti sono sicuramente < m
		if(L == esami.size()) 
			return;
//		provo ad aggiungere esami[L]
		parziale.add(esami.get(L));//AGGIUNGO L'ESAME A PARZIALE E CHIAMO LA RICORSIONE --> NEL CASO TROVA UNA BUONA SOLUZIONE --> SALVO SOL. IN LISTA MIGLIORE
		cerca2(parziale, L+1, m); 					//	 MA DEVO ANDARE AVANTI AD AGGIUNGERE ESAMI E VEDERE SE CI SONO ALTRE SOL. OTTIME
//		provo a NON aggiungere esami[L]             // NEL CASO NON ABBIAMO ANCORA RAGGIUNTO m AGGIUNGO ANCORA ESAMI                       
		parziale.remove(esami.get(L));    // TOLGO L'ULTIMO ESAME AGGIUNTO PERCHE' SE SONO QUI VUOL DIRE CHE ABBIAMO RAGGIUNTO/SUPERATO m
		cerca2(parziale, L+1, m);         // E VADO AVANTI CON IL LIVELLO SUCCESSIVO COSì AGGIUNGO L'ESAME DI DOPO
		}


	/*
	 * COMPLESSITA' N! (fattoriale)
	 * */
	
	private void cerca1(Set<Esame> parziale, int L, int m) {
//		Controllare i casi terminali
		int sommaCrediti = sommaCrediti(parziale);
		if(sommaCrediti > m)
			return;
		else if(sommaCrediti == m) {
//			soluzione VALIDA controlliamo se è la migliore fino a qui
			double mediaVoti = calcolaMedia(parziale);
			if(mediaVoti > mediaMigliore) {
				migliore = new HashSet<Esame>(parziale);
				mediaMigliore = mediaVoti;
			}
			
			return;
		}
//		Se arriviamo qui i crediti sono sicuramente < m
		if(L == esami.size())
			return;
		
//		generiamo i sotto-problemi
		for(Esame e : esami) {
			if(!parziale.contains(e)) {
				parziale.add(e);
				cerca1(parziale, L+1, m);
				parziale.remove(e); // se parziale fosse una LINKEDLIST dovremmo togliere l'ultimo elemento della lista (parziale.size()-1)
			}
		}
	}


	public double calcolaMedia(Set<Esame> esami) {
		
		int crediti = 0;
		int somma = 0;
		
		for(Esame e : esami){
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		
		return somma/crediti;
	}
	
	public int sommaCrediti(Set<Esame> esami) {
		int somma = 0;
		
		for(Esame e : esami)
			somma += e.getCrediti();
		
		return somma;
	}

}

package sax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class LeerActaHandler extends DefaultHandler{

	// leer acta.xml y calcular la media de las calificaciones
	
	private int contadorCalificaciones;
	private double acumuladorNotas;
	private boolean isCalificacion;
	private boolean isNota;
	
	@Override
	public void startDocument() throws SAXException {
		System.out.println("Invoca comienzo de documento");
		contadorCalificaciones = 0;
		acumuladorNotas = 0;
		isCalificacion = false;
		isNota = false;
	}

	@Override
	public void endDocument() throws SAXException {
		System.out.println("Invoca el final del documento");
		double media = acumuladorNotas / contadorCalificaciones;
		System.out.println("La media es "+media);
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		
		// como acceder a los atributos
		for(int i=0; i<attributes.getLength();i++) {
			String valor = attributes.getValue(i);
			String nombreAtributo = attributes.getQName(i);
			System.out.println("atributo "+nombreAtributo+" del elemento "+qName+" con valor "+valor);
		}
		
		//se puede acceder a los atributos directamente por nombre
		String cursoValor = attributes.getValue("curso");
		
		if(qName.equals("calificacion")) {
			isCalificacion = true;
		}
		
		if(qName.equals("nota") && isCalificacion) {
			isNota = true;
		}
		
		
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if(qName.equals("calificacion")) {
			isCalificacion = false;
		}
		if(qName.equals("nota")) {
			isNota = false;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if(isCalificacion && isNota) {
			String texto = new String(ch,start,length);
			Double d = Double.parseDouble(texto);
			acumuladorNotas += d;
			++contadorCalificaciones;
			/*
			//Esto lo puedo hacer aquí o llevármelo al cierre del elemento
			isCalificacion = false;
			isNota = false;
			*/
		}
		
		
	}
	
	
	

}

package dom;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Main {

	public static void main(String[] args) throws Exception {

		// 1. Obtener una factoría
		DocumentBuilderFactory factoria = DocumentBuilderFactory.newInstance();

		// 2. Pedir a la factoría la construcción del analizador
		DocumentBuilder analizador = factoria.newDocumentBuilder();

		// 3. Analizar el documento
		Document documento = analizador.parse("xml/acta.xml");
		
		//crear un documeto vacio si queremos generar xml desde cero
		Document nuevoDocumento = analizador.newDocument();
		
		
		Element raiz = documento.getDocumentElement();
		
		String convocatoria = raiz.getAttribute("convocatoria"); //leer valor del atributo
		Attr cursoNodo = raiz.getAttributeNode("curso");
		String curso = cursoNodo.getValue();
		String asignatura = raiz.getAttribute("asignatura");
		
		System.out.println("Leyendo acta de "+convocatoria+" del curso "+curso+" de la asignatura"
				+ " "+asignatura);
		
		System.out.println("Text content "+raiz.getTextContent());
		
		
		NodeList nodeList = raiz.getChildNodes();
		
		for(int i=0;i<nodeList.getLength();i++) {
			Node nodeItem = nodeList.item(i);
			
			//nodeItem.getNodeName();
			if(nodeItem.getNodeType() == Node.ELEMENT_NODE) {
				Element elemento = (Element)nodeItem;
				System.out.println("Elemento "+i+": "+elemento.getTagName());
			}
			if(nodeItem.getNodeType() == Node.TEXT_NODE) {
				System.out.println("Ha encontrado un nodo de texto");
			}			
		}
		
		// Quiero todas las calificaciones
		NodeList listadoCalificaciones = documento.getElementsByTagName("calificacion");
		for(int i=0;i<listadoCalificaciones.getLength();i++) {
			Element calificacion = (Element)listadoCalificaciones.item(i);
			
			// quiero las notas
			// opcion 1: obtenego los hijos, itero, y me quedo con los elemento nota
			
			NodeList hijos = calificacion.getChildNodes();
			for(int j=0; j<hijos.getLength();j++) {
				Node hijo = hijos.item(j);
				if(hijo.getNodeType() == Node.ELEMENT_NODE) {
					Element h = (Element) hijo;
					if(h.getTagName().equals("nota")) { //si el hijo es elemento nota
						System.out.println("Nota "+i+": "+h.getTextContent());
					}
				}
			}
			// opcion 2 obtengo todas las notas que dependen de esa calificación
			NodeList listadoNotas = calificacion.getElementsByTagName("nota");
			Element notaElement = (Element)listadoNotas.item(0);
			System.out.println("opcion 2 Nota "+i+": "+notaElement.getTextContent());
		}
		
		
		
		
		
		
		

		// Ejemplo 2: creación de una diligencia

		Element diligencia = documento.createElement("diligencia");
		diligencia.setAttribute("fecha", "2023-09-02");

		Element nif = documento.createElement("nif");
		nif.setTextContent("22334312C");

		Element nota = documento.createElement("nota");
		nota.setTextContent("10");

		diligencia.appendChild(nif);
		diligencia.appendChild(nota);

		// Situarla en el árbol como último elemento del documento
		documento.getDocumentElement().appendChild(diligencia);

		// Ejemplo 1, creación de una calificación

		Element calificacion = documento.createElement("calificacion");
		nif = documento.createElement("nif");
		nif.setTextContent("22334312C");

		nota = documento.createElement("nota");
		nota.setTextContent("9");

		calificacion.appendChild(nif);
		calificacion.appendChild(nota);

		// La situamos en el documento antes de las diligencias.
		NodeList diligencias = documento.getElementsByTagName("diligencia");
		
		// Si no hay diligencias, se puede colocar al final del documento
		if (diligencias.getLength() == 0) {
			documento.getDocumentElement().appendChild(calificacion);
		} else {
			// Obtener como referencia la primera diligencia
			Element diligenciaReferencia = (Element) diligencias.item(0);
			Element padre = (Element) diligenciaReferencia.getParentNode();
			padre.insertBefore(calificacion, diligenciaReferencia);
		}
		
		// Guardar los cambios
		
		// 1. Construye la factoría de transformación y obtiene un 
		// transformador
		TransformerFactory tFactoria = TransformerFactory.newInstance(); 
		Transformer transformacion = tFactoria.newTransformer();
		
		// 2. Establece la entrada, como un árbol DOM 
		
		Source input = new DOMSource(documento);
		
		// 3. Establece la salida, un fichero en disco 
		Result output = new StreamResult("xml/acta3.xml");
		// 4. Aplica la transformación 
		
		transformacion.transform(input, output);
			
		System.out.println("fin.");
	}
}

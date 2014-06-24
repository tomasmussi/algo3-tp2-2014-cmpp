package algo3.modelo.mapa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import algo3.controlador.XMLParser;
import algo3.modelo.excepcion.CiudadNoEncontradaException;

public class InformacionMapa {
	private Map<String, InformacionCiudad> ciudadesDelMundo;
	private static InformacionMapa instance;

	private InformacionMapa(){
	}

	public synchronized static InformacionMapa getInstance() {
		if (instance == null) {
			instance = new InformacionMapa();
			instance.refrescarInformacion();
		}
		return instance;
	}

	private void refrescarInformacion(){
		ciudadesDelMundo = new HashMap<String, InformacionCiudad>();
		Iterator<InformacionCiudad> iter = XMLParser.cargarCiudades().iterator();
		while (iter.hasNext()){
			InformacionCiudad infoCiudad = iter.next();
			ciudadesDelMundo.put(infoCiudad.getNombreCiudad(), infoCiudad);
		}
	}

	public List<InformacionCiudad> getListadoCiudades(){
		return new ArrayList<InformacionCiudad>(ciudadesDelMundo.values());
	}

	public InformacionCiudad getCiudadDeNombre(String ciudadOrigen) throws CiudadNoEncontradaException {
		if (!ciudadesDelMundo.containsKey(ciudadOrigen)){
			throw new CiudadNoEncontradaException("La ciudad " + ciudadOrigen + " no se encuentra en la configuracion de ciudades");
		}
		return ciudadesDelMundo.get(ciudadOrigen);
	}

	public void cargarCiudades(List<InformacionCiudad> ciudades){
		ciudadesDelMundo = new HashMap<String, InformacionCiudad>();
		Iterator<InformacionCiudad> iter = ciudades.iterator();
		while (iter.hasNext()){
			InformacionCiudad info = iter.next();
			ciudadesDelMundo.put(info.getNombreCiudad(), info);
		}
	}

}

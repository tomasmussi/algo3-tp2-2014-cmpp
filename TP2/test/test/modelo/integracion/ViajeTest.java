package test.modelo.integracion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import algo3.modelo.edificio.Aeropuerto;
import algo3.modelo.edificio.Banco;
import algo3.modelo.edificio.Edificio;
import algo3.modelo.edificio.Embajada;
import algo3.modelo.entidad.Bandera;
import algo3.modelo.entidad.Gobierno;
import algo3.modelo.entidad.Moneda;
import algo3.modelo.ladron.CaracteristicaLadron;
import algo3.modelo.ladron.Ladron;
import algo3.modelo.mapa.mundi.Ciudad;
import algo3.modelo.mapa.mundi.InformacionCiudad;
import algo3.modelo.objeto.ObjetoComun;
import algo3.modelo.objeto.Robable;
import algo3.modelo.policia.Policia;
import algo3.modelo.tiempo.Reloj;
import algo3.modelo.viaje.Recorrido;

public class ViajeTest {

	/* ************** Metodos auxiliares ************** */
	private Policia policia;
	private Reloj reloj;

	@Before
	public void crearPolicia(){
		this.reloj = new Reloj();
		this.policia = new Policia(reloj);
	}

	/**
	 * Crea un ladron con una ciudad de origen dada
	 * y crea otra ciudad para escapar.
	 * */
	private Ladron crearLadronConObjetoComunYRecorrido(InformacionCiudad ciudadInicial) {
		CaracteristicaLadron caracteristica = new CaracteristicaLadron("Nick Brunch", "Masculino", "Mountain Climbing", "Negro", "Anillo", "Motocicleta");
		Robable objeto = new ObjetoComun("Buda dorado", "Bangkok");

		List<InformacionCiudad> ciudadesARecorrer = new ArrayList<InformacionCiudad>();
		InformacionCiudad segundaCiudad = crearInformacionCiudad("New York", "Azul, Roja y Blanca", "Dolar", "Presidente");
		ciudadesARecorrer.add(ciudadInicial);
		ciudadesARecorrer.add(segundaCiudad);
		ciudadesARecorrer.add(ciudadInicial);
		ciudadesARecorrer.add(segundaCiudad);
		Recorrido recorrido = new Recorrido(ciudadesARecorrer, objeto.getCantidadDeCiudades());

		return new Ladron(caracteristica, objeto, recorrido.getCiudadesRecorrido().iterator());
	}

	private InformacionCiudad crearInformacionCiudad(String nombre, String bandera, String moneda, String gobierno) {
		return new InformacionCiudad(nombre, bandera, moneda, gobierno);
	}

	/**
	 * Devuleve una ciudad sin edificios pero con informacion para pedirle: colores bandera, moneda, etc.
	 */
	private Ciudad crearCiudadPrueba(String nombre, String bandera, String moneda, String gobierno) {
		InformacionCiudad informacion = new InformacionCiudad(nombre, bandera, moneda, gobierno);
		return new Ciudad(0, 0, null, null, null, informacion);
	}

	/**
	 * Devuelve una Ciudad con Edificios Fijos {Aeropuerto, Banco, Embajada} pero sin informacion de si misma.
	 */
	private Ciudad crearCiudad(String nombre, Ciudad siguienteCiudad) {
		Edificio edificio1 = new Aeropuerto(new Bandera(siguienteCiudad.getColoresBandera()));
		Edificio edificio2 = new Banco(new Moneda(siguienteCiudad.getMoneda()));
		Edificio edificio3 = new Embajada(new Gobierno(siguienteCiudad.getGobierno()));
		return new Ciudad(1, 1, edificio1, edificio2, edificio3, new InformacionCiudad());
	}

	/* ********************************************************** */

	@Test
	public void testPoliciaViajaCiudadLadronEscapa() {
		InformacionCiudad ciudadInicial = crearInformacionCiudad("Rio de Janeiro", "Verde y Amarillo", "Reales", "Presidente");
		// Creo ladron en una ciudad inicial.
		Ladron ladron = crearLadronConObjetoComunYRecorrido(ciudadInicial);
		Ciudad inicial = ladron.getCiudadActual();
		// Hago viajar al policia al pais donde puedo encontrar al ladron. En este caso a la ciudad inicial.
		policia.viajarA(inicial);
		// En esta situacion deberian estar en la misma ciudad.
		assertTrue(policia.getCiudadActual().esMismaCiudadQue(ladron.getCiudadActual()));
		// El ladron se escapa.
		ladron.moverAlSiguientePais();
		// Ahora no deberian estar en la misma ciudad, porque el ladron se fue.
		assertFalse(policia.getCiudadActual().esMismaCiudadQue(ladron.getCiudadActual()));
	}

	@Test
	public void testPoliciaAtrapaLadronConOrdenDeArrestoCorrecta() {
		// Caso con 2 ciudades. Ejemplo facil.
		InformacionCiudad ciudadInicial = crearInformacionCiudad("Rio de Janeiro", "Verde y Amarillo", "Reales", "Presidente");
		// Creo ladron en una ciudad inicial.
		Ladron ladron = crearLadronConObjetoComunYRecorrido(ciudadInicial);
		Ciudad inicial = ladron.getCiudadActual();
		// Creo policia sin ciudad inicial.
		// Hago viajar al policia al pais donde puedo encontrar al ladron. En este caso a la ciudad inicial.
		policia.viajarA(inicial);
		// En esta situacion deberian estar en la misma ciudad.
		assertTrue(policia.getCiudadActual().esMismaCiudadQue(ladron.getCiudadActual()));
		// El ladron se escapa.
		ladron.moverAlSiguientePais();
		// Ahora no deberian estar en la misma ciudad, porque el ladron se fue.
		assertFalse(policia.getCiudadActual().esMismaCiudadQue(ladron.getCiudadActual()));
		// Hago viajar al policia al pais donde puedo encontrar al ladron. Utilizo la ciudad del ladron.
		policia.viajarA(ladron.getCiudadActual());
		// Ambos deberian estar en la misma ciudad.
		assertTrue(policia.getCiudadActual().esMismaCiudadQue(ladron.getCiudadActual()));
		// El ladron se escapa
		ladron.moverAlSiguientePais();
		// Ambos deberian estar en la misma ciudad.
		assertFalse(policia.getCiudadActual().esMismaCiudadQue(ladron.getCiudadActual()));
		// Hago viajar al policia al pais donde puedo encontrar al ladron. Utilizo la ciudad del ladron.
		policia.viajarA(ladron.getCiudadActual());
		// Ambos deberian estar en la misma ciudad.
		assertTrue(policia.getCiudadActual().esMismaCiudadQue(ladron.getCiudadActual()));
		// El ladron se escapa (Ultima vez que se puede cambiar de pais)
		ladron.moverAlSiguientePais();
		assertFalse(policia.getCiudadActual().esMismaCiudadQue(ladron.getCiudadActual()));
		// Hago viajar al policia al pais donde puedo encontrar al ladron. Utilizo la ciudad del ladron.
		policia.viajarA(ladron.getCiudadActual());
		// Ambos deberian estar en la misma ciudad.
		assertTrue(policia.getCiudadActual().esMismaCiudadQue(ladron.getCiudadActual()));
		// El ladron se escapa (No se puede seguir escapando)
		ladron.moverAlSiguientePais();
		assertTrue(policia.getCiudadActual().esMismaCiudadQue(ladron.getCiudadActual()));
		// Crear Orden de arresto con las caracteristicas del ladron (En este caso el que cree al inicio)
		policia.emitirOrdenDeArresto(new CaracteristicaLadron("Nick Brunch", "Masculino", "Mountain Climbing", "Negro", "Anillo", "Motocicleta"));
		// Arrestar ladron.
		assertTrue(policia.arrestar(ladron));
	}

	@Test
	public void testPoliciaNoAtrapaLadronConOrdenDeArrestoIncorrecta() {
		// Caso con 2 ciudades. Ejemplo facil.
		InformacionCiudad ciudadInicial = crearInformacionCiudad("Rio de Janeiro", "Verde y Amarillo", "Reales", "Presidente");
		// Creo ladron en una ciudad inicial.
		Ladron ladron = crearLadronConObjetoComunYRecorrido(ciudadInicial);
		// Creo policia sin ciudad inicial.
		// Hago viajar al policia al pais donde puedo encontrar al ladron. En este caso a la ciudad inicial.
		policia.viajarA(ladron.getCiudadActual());
		// En esta situacion deberian estar en la misma ciudad.
		assertTrue(policia.getCiudadActual().esMismaCiudadQue(ladron.getCiudadActual()));
		// El ladron se escapa.
		ladron.moverAlSiguientePais();
		// Ahora no deberian estar en la misma ciudad, porque el ladron se fue.
		assertFalse(policia.getCiudadActual().esMismaCiudadQue(ladron.getCiudadActual()));
		// Hago viajar al policia al pais donde puedo encontrar al ladron. Utilizo la ciudad del ladron.
		policia.viajarA(ladron.getCiudadActual());
		// Ambos deberian estar en la misma ciudad.
		assertTrue(policia.getCiudadActual().esMismaCiudadQue(ladron.getCiudadActual()));
		// El ladron se escapa.
		ladron.moverAlSiguientePais();
		// Ambos deberian estar en la misma ciudad.
		assertFalse(policia.getCiudadActual().esMismaCiudadQue(ladron.getCiudadActual()));
		// Hago viajar al policia al pais donde puedo encontrar al ladron. Utilizo la ciudad del ladron.
		policia.viajarA(ladron.getCiudadActual());
		// Ambos deberian estar en la misma ciudad.
		assertTrue(policia.getCiudadActual().esMismaCiudadQue(ladron.getCiudadActual()));
		// El ladron se escapa .
		ladron.moverAlSiguientePais();
		// Ambos deberian estar en la misma ciudad.
		assertFalse(policia.getCiudadActual().esMismaCiudadQue(ladron.getCiudadActual()));
		// Hago viajar al policia al pais donde puedo encontrar al ladron. Utilizo la ciudad del ladron.
		policia.viajarA(ladron.getCiudadActual());
		// Ambos deberian estar en la misma ciudad.
		assertTrue(policia.getCiudadActual().esMismaCiudadQue(ladron.getCiudadActual()));
		// El ladron se escapa (No tiene mas paises, no deberia poder irse).
		ladron.moverAlSiguientePais();
		// Ambos deberian estar en la misma ciudad.
		assertTrue(policia.getCiudadActual().esMismaCiudadQue(ladron.getCiudadActual()));
		// Crear Orden de arresto con las caracteristicas del ladron (En este caso el que cree al inicio)
		policia.emitirOrdenDeArresto(new CaracteristicaLadron("Merey Laroc", "Femenino", "Croquet", "Marron", "Joyas", "Limusina"));
		// Arrestar ladron.
		assertFalse(policia.arrestar(ladron));
	}

	@Test
	public void testPoliciaNoAtrapaLadronSinOrdenDeArresto() {
		// Caso con 2 ciudades. Ejemplo facil.
		InformacionCiudad ciudadInicial = crearInformacionCiudad("Rio de Janeiro", "Verde y Amarillo", "Reales", "Presidente");
		// Creo ladron en una ciudad inicial.
		Ladron ladron = crearLadronConObjetoComunYRecorrido(ciudadInicial);
		// Creo policia sin ciudad inicial.
		// Hago viajar al policia al pais donde puedo encontrar al ladron. En este caso a la ciudad inicial.
		policia.viajarA(ladron.getCiudadActual());
		// En esta situacion deberian estar en la misma ciudad.
		assertTrue(policia.getCiudadActual().esMismaCiudadQue(ladron.getCiudadActual()));
		// El ladron se escapa.
		ladron.moverAlSiguientePais();
		// Ahora no deberian estar en la misma ciudad, porque el ladron se fue.
		assertFalse(policia.getCiudadActual().esMismaCiudadQue(ladron.getCiudadActual()));
		// Hago viajar al policia al pais donde puedo encontrar al ladron. Utilizo la ciudad del ladron.
		policia.viajarA(ladron.getCiudadActual());
		// Ambos deberian estar en la misma ciudad.
		assertTrue(policia.getCiudadActual().esMismaCiudadQue(ladron.getCiudadActual()));
		// El ladron se escapa (No tiene mas paises, no deberia poder irse).
		ladron.moverAlSiguientePais();
		// Ambos deberian estar en la misma ciudad.
		assertFalse(policia.getCiudadActual().esMismaCiudadQue(ladron.getCiudadActual()));
		// Hago viajar al policia al pais donde puedo encontrar al ladron. Utilizo la ciudad del ladron.
		policia.viajarA(ladron.getCiudadActual());
		// Ambos deberian estar en la misma ciudad.
		assertTrue(policia.getCiudadActual().esMismaCiudadQue(ladron.getCiudadActual()));
		// El ladron se escapa .
		ladron.moverAlSiguientePais();
		// Ambos deberian estar en la misma ciudad.
		assertFalse(policia.getCiudadActual().esMismaCiudadQue(ladron.getCiudadActual()));
		// Hago viajar al policia al pais donde puedo encontrar al ladron. Utilizo la ciudad del ladron.
		policia.viajarA(ladron.getCiudadActual());
		// Ambos deberian estar en la misma ciudad.
		assertTrue(policia.getCiudadActual().esMismaCiudadQue(ladron.getCiudadActual()));
		// El ladron se escapa (No tiene mas paises, no deberia poder irse).
		ladron.moverAlSiguientePais();
		// Ambos deberian estar en la misma ciudad.
		assertTrue(policia.getCiudadActual().esMismaCiudadQue(ladron.getCiudadActual()));
		// Arrestar ladron.
		assertFalse(policia.arrestar(ladron));
	}

	// @Test
	// public void testRecorridoCorrectoMeLLevaAlLadron(){
	// throw new UnsupportedOperationException("No implementada");
	// }

	@Test
	public void testEdificioDaPistaSiguienteCiudad() {
		Ciudad siguienteCiudad = crearCiudadPrueba("Buenos Aires", "Celeste y Blanca", "Australes", "Presidente");
		Ciudad bangkok = crearCiudad("Bangkok", siguienteCiudad);
		Edificio[] edificiosPosibles = bangkok.getTodosLosEdificios();
		String pista = policia.visitarEdificioYObtenerPista(edificiosPosibles[0]); // 0 = aeropuerto.
		assertTrue(pista.equals("Me dicen mis fuentes que se fue en un avion con Celeste y Blanca en sus alas."));
	}

	// Entrar a un edificio (1hr la primera vez , 2 hs 2da vez, 3hs 3ra vez).
	@Test
	public void testEdificioRestaUnaHoraPorPrimerEdifico() {
		Edificio banco = new Banco(new Moneda("Peso"));
		policia.visitarEdificioYObtenerPista(banco);
		assertEquals("Lunes 08:00 horas",reloj.tiempoActual());
	}

	@Test
	public void testEdificioRestaDosHorasPorSegundoEdificio() {
		Edificio banco = new Banco(new Moneda("Peso"));
		Edificio aeropuerto = new Aeropuerto(new Bandera("Verde y azul"));
		policia.visitarEdificioYObtenerPista(banco);
		policia.visitarEdificioYObtenerPista(aeropuerto);
		assertEquals("Lunes 10:00 horas", reloj.tiempoActual());
	}

	@Test
	public void testEdificioRestaSoloUnaHoraPorEdificioSinImportarCantidadDeEntradas() {
		Edificio banco = new Banco(new Moneda("Peso"));
		policia.visitarEdificioYObtenerPista(banco);
		policia.visitarEdificioYObtenerPista(banco);
		policia.visitarEdificioYObtenerPista(banco);
		policia.visitarEdificioYObtenerPista(banco);
		assertEquals("Lunes 08:00 horas", reloj.tiempoActual());
	}
}

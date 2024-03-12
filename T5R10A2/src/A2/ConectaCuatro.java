package A2;

import java.util.Scanner;

public class ConectaCuatro {
	public static final int CUATRO_FICHAS = 4;
	public static final String SIN_FICHA = "-";
	public static final String FICHA_ROJA = "\033[1;31mX\033[0m";
	public static final String FICHA_AMARILLA = "\033[1;33mO\033[0m";
	public static final String JUGADOR_ROJO = "\033[1;31mROJO\033[0m";
	public static final String JUGADOR_AMARILO = "\033[1;33mAMARILLO\033[0m";

	private static Scanner comunicador = new Scanner(System.in);

	public static void main(String[] args) {

		String[][] tablero = generadorTablero();

		iniciarJuego(tablero);

	}

	/**
	 * Inicia el bucle en el que las dos personas juegan hasta que una gana o se
	 * quedan sin espacios
	 * 
	 * @param turnoRojo
	 * @param salida
	 * @param tablero
	 */
	private static void iniciarJuego(String[][] tablero) {
		int columnaUsuario;
		boolean turnoRojo = false;

		// comienzo con un jugador aleatorio
		if (Math.round(Math.random()) == 0) {
			turnoRojo = false;
		}

		do {
			boolean fichaPuesta = false;

			System.out.println("\nTurno del " + ((turnoRojo) ? JUGADOR_ROJO : JUGADOR_AMARILO));
			pintarTablero(tablero);
			System.out.print("Inserta la fila: ");
			columnaUsuario = Integer.parseInt(comunicador.nextLine()) - 1;

			// Inserta la ficha del color corecto si la condicion se cumple
			for (int indiceFila = tablero[0].length; indiceFila > 0 && !fichaPuesta; indiceFila--) {
				// rellena las fichas por debajo

				if (tablero[indiceFila][columnaUsuario] == SIN_FICHA) {

					if (turnoRojo) {
						tablero[indiceFila][columnaUsuario] = FICHA_ROJA;
					} else {
						tablero[indiceFila][columnaUsuario] = FICHA_AMARILLA;
					}

					fichaPuesta = true;
					turnoRojo = !turnoRojo;

				} else if (indiceFila == 1) {
					System.out.println("No caben mas fichas en esta fila");
				}
			}

		} while (!buscarGanador(tablero) && !tableroLleno(tablero));

		// final del juego
		if (tableroLleno(tablero)) {// no hay mas movimientos
			System.out.println("No hay ganadores, el tablero está completo");
		} else {// hay un ganador
			// proporciona al ganador en pantalla
			System.out.print("EL GANADOR ES EL ");
			if (turnoRojo) { // ganador = !turnoRojo
				System.out.println(JUGADOR_AMARILO);
			} else {
				System.out.println(JUGADOR_ROJO);
			}
			pintarTablero(tablero);

		}
	}

	/**
	 * Revisa si las columnas superiores tienen fichas, ya que si es asi significara
	 * que la columna esta completa, si todas tienen fichas significara que el
	 * tablero esta lleno
	 * 
	 * @param tablero
	 */
	private static boolean tableroLleno(String[][] tablero) {
		int columnaCompleta = 0;

		for (int indiceColumna = 0; indiceColumna < tablero[0].length; indiceColumna++) {

			if (tablero[1][indiceColumna] != SIN_FICHA) { // rojas
				columnaCompleta++;

			} else {
				columnaCompleta = 0;
			}

		}

		return (columnaCompleta == (tablero.length - 1)) ? true : false;
	}

	/**
	 * Busca si hay un posible 4 ficahs del mismo color conectadas
	 * 
	 * @param tablero
	 * @return boolean
	 */
	private static boolean buscarGanador(String[][] tablero) {
		boolean ganador = false;

		for (int indiceFila = 1; indiceFila < tablero.length && !ganador; indiceFila++) {
			for (int indiceColumna = 0; indiceColumna < tablero[0].length && !ganador; indiceColumna++) {

				if (tablero[indiceFila][indiceColumna] != SIN_FICHA) { // si hay una ficha comprueba las de
																		// alrededor, por filas y columnas
					int conteoRojas = 0, conteoAmarillas = 0;

					// busca fichas horizontalmente
					for (int j = indiceColumna; j < tablero[0].length && !ganador; j++) {

						if (tablero[indiceFila][j] == FICHA_ROJA) { // rojas
							conteoRojas++;
							conteoAmarillas = 0;

						} else if (tablero[indiceFila][j] == FICHA_AMARILLA) { // amarillas
							conteoAmarillas++;
							conteoRojas = 0;

						} else { // vacio
							conteoAmarillas = 0;
							conteoRojas = 0;

						}

						// evalua si hay 4 fichas encontradas cerca de ellas mismas
						if (conteoAmarillas == CUATRO_FICHAS || conteoRojas == CUATRO_FICHAS) {
							ganador = true;

						}

					}

					conteoRojas = 0;
					conteoAmarillas = 0;
					// busca fichas verticalmente
					for (int i = indiceFila; i < tablero.length && !ganador; i++) {

						if (tablero[i][indiceColumna] == FICHA_ROJA) { // rojas
							conteoRojas++;
							conteoAmarillas = 0;

						} else if (tablero[i][indiceColumna] == FICHA_AMARILLA) { // amarillas
							conteoAmarillas++;
							conteoRojas = 0;

						} else { // vacio
							conteoAmarillas = 0;
							conteoRojas = 0;

						}

						// evalua si hay 4 fichas encontradas cerca de ellas mismas
						if (conteoAmarillas == CUATRO_FICHAS || conteoRojas == CUATRO_FICHAS) {
							ganador = true;
						}
					}

				}
			}
		}
		return ganador;
	}

	/**
	 * Devuelve el tablero pintado F
	 * 
	 * @param tablero
	 */
	private static void pintarTablero(String[][] tablero) {
		// tabla de juego
		for (int indiceFila = 0; indiceFila < tablero.length; indiceFila++) {
			for (int indicerColumna = 0; indicerColumna < tablero[0].length; indicerColumna++) {

				System.out.print("[" + tablero[indiceFila][indicerColumna] + "]");

			}
			System.out.print("\n");
		}
	}

	/**
	 * Crea el tablero y lo rellean en blanaco
	 * 
	 * @return String[][]
	 */
	private static String[][] generadorTablero() {
		String[][] tablero = new String[11 /* filas */][10 /* columnas */];
		// se generan 11 filas porque la primera indica la cantidad de columnas que hay
		// for para pponer numeros a las columnas
		for (int i = 0; i < tablero[0].length; i++) {
			tablero[0][i] = Integer.toString(i + 1);
		}
		// for para rellenar los espacios vacios
		for (int indiceFila = 1; indiceFila < tablero.length; indiceFila++) {
			for (int indicerColumna = 0; indicerColumna < tablero[0].length; indicerColumna++) {
				tablero[indiceFila][indicerColumna] = SIN_FICHA;

			}

		}
		return tablero;
	}

}

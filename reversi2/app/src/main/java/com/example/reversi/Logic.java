
package com.example.reversi;
import java.util.ArrayList;
import java.util.Scanner;

public class Logic {

	private Scanner sc = new Scanner(System.in);
	private RandomPlayer rp = new RandomPlayer(); // esto va haber que quitarlo
	private String options = "";//opciones disponibles
	private int boardSize = 6; // Siempre va a ser cuadrado y numero par
	private int whiteTiles = 2, blackTiles= 2;
	private int jugadorConTurno = 1;
	private boolean HayJugadaDisponible = true;
	private Cell board[][] = new Cell[boardSize][boardSize]; // Tablero del juego
	private String playerPlay = ""; // esta es la jugada del jugador
	private String result = "Hola";
	private boolean bResult = false;
	private int dificulty;
	//AI with alpha beta prunning
	private AI AI;
	private int intAI = 0;

	// 0 -> vacío; 1 -> ficha negra; 2 -> ficha blanca 8 -> disponible

	// TEMPORAL PARA DEBUG

	public Logic(int dificulty){
		reset();
		marcaDisponibles(board, 1);
		this.dificulty = dificulty;
	}

	public int getWhiteTiles(){
		return whiteTiles;
	}

	public int getBlackTiles(){
		return blackTiles;
	}

	public int getPlayerTurn() {return jugadorConTurno;}

	public Cell[][] getBoard(){
		System.out.println("ESTE ES EL REAL");
		return board;
	}

	public String getResult(){
		return result;
	}
	public boolean getBResult(){return bResult;}

	public boolean setPlayerPlay(String play){ //actualizar la jugada del jugador, si no está permitido devuelve false, en caso contrario devuelve true
		if (!ComprobarCasillaDisponible(board, play)) {
			return false;
		}else{
			playerPlay = play;
			if(intAI == 0){
				AI = new AI();
				intAI++;
			}
			AI.setHumanPlay(play);
			return true;
		}
	}

	public void setDificulty(int dificulty){
		this.dificulty = dificulty;
	}

	/***************
	 * REVERSI***************** El board puede ser de cualquier tamaño par Te pide
	 * que introduzcas las coordenadas de la posición donde quieres colocar la
	 * ficha. Ambas coordenadas X e Y empiezan a contar arriba a la izquierda: //y empieza desde cero si no me equivoco
	 * Si introduces una erronea no te permite y te
	 * las vuelve a pedir
	 */

	/*public void playGame() {

		// Pone el board
		reset(this.board);
		imprimeTablero(this.board);

		System.out.println("EMPIEZAN NEGRAS (1)");

		// Siempre empiezan negras
		marcaDisponibles(this.board, 1);
		imprimeTablero(this.board);
		
		String jugador = "";
		String casilla;

		// Bucle de turnos.
		while (HayJugadaDisponible) {

			System.out.println("¿ACTIVAR MODO DEBUG IA?");
			System.out.println("1. SI");
			System.out.println("2. JUGAR NORMAL");
			System.out.println("Las opciones son: " + options);
			if (sc.nextInt() == 1) {
				jugadorConTurno = IADebug(this.board, jugadorConTurno);
			} else {
				
				// Pide posicion de la ficha a poner
				if(jugadorConTurno == 1){
					casilla = introduceCasilla(this.board);
				}else{
					casilla = rp.randomSelection(options);
				}

				ponerFicha(this.board, Integer.parseInt("" + casilla.charAt(0)), Integer.parseInt("" + casilla.charAt(1)), jugadorConTurno);
				jugadorConTurno = getOponente(jugadorConTurno);
			}

			// PEQUEÑO DEBUG
			if (jugadorConTurno == 1) {
				jugador = "NEGRAS (1)";
			} else {
				jugador = "BLANCAS (2)";
			}

			System.out.println("ES EL TURNO DE " + jugador);

			marcaDisponibles(this.board, jugadorConTurno);
			imprimeTablero(this.board);

			if (!comprobarDisponibles(this.board, jugadorConTurno)) {

				System.out.println("NO HAY MOV POSIBLES PARA " + jugador);
				jugadorConTurno = getOponente(jugadorConTurno);
				marcaDisponibles(this.board, jugadorConTurno);
				imprimeTablero(this.board);

				if (!comprobarDisponibles(this.board, jugadorConTurno)) {

					System.out.println("GAME OVER");
					HayJugadaDisponible = false;
				}
			}

		}

		// Comprobaciones de quien ha ganado
		if (!HayJugadaDisponible) {
			blackTiles = contarFichas(this.board, 1);
			whiteTiles = contarFichas(this.board, 2);

			if(blackTiles > whiteTiles) {
				result = "NEGRAS";
			}else if(blackTiles < whiteTiles) {
				result = "WHITE";
			}else {
				result = "DRAW";
			}
			sc.close();
			System.exit(0);
		}

		sc.close();

	}*/

	public void playTurn(){

		if(jugadorConTurno == 1){// jugador real
			playTurnPlayer();
		}else{//AI
			playTurnAI();
		}
		blackTiles = contarFichas(this.board, 1);
		whiteTiles = contarFichas(this.board, 2);
		jugadorConTurno = getOponente(jugadorConTurno);
		thereIsNoMovements();
		//imprimeTablero(board);

	}

	private void playTurnPlayer(){
		ponerFicha(board, Integer.parseInt("" + playerPlay.charAt(0)), Integer.parseInt("" + playerPlay.charAt(1)), jugadorConTurno);
	}

	private void playTurnAI(){
		if(dificulty == 1){
			String option = rp.randomSelection(options);
			ponerFicha(board, Integer.parseInt("" + option.charAt(0)), Integer.parseInt("" + option.charAt(1)), jugadorConTurno);
		}else{

			System.out.println("Voy a pedir un movimiento a la IA");
			System.out.println("El movimiento del humano es: " + playerPlay);
			System.out.println("Los movimientos posibles son: " + options);
			//AI = new AI();
			String option = AI.getMovement(options);
			ponerFicha(board, Integer.parseInt("" + option.charAt(0)), Integer.parseInt("" + option.charAt(1)), jugadorConTurno);
		}
	}

	private void thereIsNoMovements(){
		marcaDisponibles(board, jugadorConTurno);

		if (!comprobarDisponibles(board, jugadorConTurno)) {
			jugadorConTurno = getOponente(jugadorConTurno);
			marcaDisponibles(board, jugadorConTurno);

			if (!comprobarDisponibles(this.board, jugadorConTurno)) {

				System.out.println("GAME OVER");
				if(blackTiles > whiteTiles) {
					result = "Black wins";
					System.out.println("Black wins");
				}else if(blackTiles < whiteTiles) {
					result = "White wins";
					System.out.println("White wins");
				}else {
					System.out.println("Draw");
					result = "Draw";
				}
				bResult = true;
				intAI = 0;
			}
		}
	}
	// Devuelve el numero de fichas de X jugador que hay en el board

	private int contarFichas(Cell tableroContar[][], int jugador) {
		// Contamos las fichas del jugador
		int numeroFichas = 0;
		for (int i = 0; i < boardSize; i++) {
			for (int w = 0; w < boardSize; w++) {
				if (tableroContar[i][w].getEstado() == jugador) {
					numeroFichas++;
				}
			}
		}
		if(jugador == 1){
			blackTiles = numeroFichas;
		}else{
			whiteTiles = numeroFichas;
		}

		return numeroFichas;
	}

	// Devuelve si hay algun movimiento posible para el jugador X
	private boolean comprobarDisponibles(Cell tableroComprobado[][], int jugador) {
		// Buscamos movimientos posibles
		for (int i = 0; i < boardSize; i++) {
			for (int w = 0; w < boardSize; w++) {
				if (tableroComprobado[i][w].getEstado() == 8) {
					return true;
				}
			}
		}
		return false;
	}

	// Devuelve si hay algun movimiento posible para el jugador X
	public ArrayList<String> getDisponibles(Cell tableroComprobado[][], int jugador) {

		ArrayList<String> disponibles = new ArrayList<String>();

		// Buscamos movimientos posibles
		for (int i = 0; i < boardSize; i++) {
			for (int w = 0; w < boardSize; w++) {
				if (tableroComprobado[i][w].getEstado() == 8) {

					String casilla = "" + i + w;
					disponibles.add(casilla);
				}
			}
		}

		return disponibles;

	}

	// Marca en el board los posibles movimientos del jugador X
	private void marcaDisponibles(Cell tableroDisponible[][], int jugador) {
		options = "";
		// Actualiza a 0
					for (int i = 0; i < boardSize; i++) {
						for (int w = 0; w < boardSize; w++) {
							if (tableroDisponible[i][w].getEstado() == 8) {
								tableroDisponible[i][w].setEstado(0);
							}
						}
					}

		// Buscamos las fichas del jugador que tiene el turno
		for (int i = 0; i < boardSize; i++) {
			for (int w = 0; w < boardSize; w++) {
				if (tableroDisponible[i][w].getEstado() == jugador) {
					// Por cada ficha, comprobamos las posiciones alrededor
					for (int dir = 0; dir < 8; dir++) {
						marcaCasillas(tableroDisponible, getOponente(jugador), i, w, dir);
					}

				}
			}
		}
	}

	// Recorre en una direccion (dir) dada desde una casilla (posX,posY) dada
	// comprobando si hay
	// una jugada posible

	private void marcaCasillas(Cell tableroMarcar[][], int oponente, int posX, int posY, int dir) {

		ArrayList<Integer> desplazamiento = getDireccion(posX, posY, dir);
		// me desplazo
		posX = desplazamiento.get(0);
		posY = desplazamiento.get(1);

		// si está fuera del board, salimos de la función
		if (limitesTablero(posX, posY)) {
			return;
		}
		// si a la izquierda hay una ficha enemiga, seguimos recorriendo hasta encontrar
		// una celda vacía o el límite del board
		if (tableroMarcar[posX][posY].getEstado() == oponente) {
			while (tableroMarcar[posX][posY].getEstado() == oponente) {
				if (limitesTablero(posX, posY)) {
					break;
				} else {

					desplazamiento = getDireccion(posX, posY, dir);
					int desplX = desplazamiento.get(0);
					int desplY = desplazamiento.get(1);

					if (limitesTablero(desplX, desplY)) {
						break;

					} else {
						posX = desplX;
						posY = desplY;
					}
				}
			}
			if (tableroMarcar[posX][posY].getEstado() == 0) {
				tableroMarcar[posX][posY].setEstado(8);
				options = options + posX + posY + ",";
			}
		}
	}

	// Coloca la ficha del jugador en la posicion (posX,posY)
	// Elimina el resto de posiciones posibles, dejando el board
	// limpio para que la función encargada marque las posiciones posibles nuevas

	private void ponerFicha(Cell tableroActivo[][], int posX, int posY, int jugador) {

		if (tableroActivo[posX][posY].getEstado() == 8) {
			//System.out.println("Logica Está disponible");
			tableroActivo[posX][posY].setEstado(jugador);


			for (int dir = 0; dir < 8; dir++) {
				ArrayList<Integer> desplazamiento = getDireccion(posX, posY, dir);
				ArrayList<Cell> ganadas = new ArrayList<Cell>();
				int X = desplazamiento.get(0);
				int Y = desplazamiento.get(1);

				// si está fuera del board, salimos de la función
				if (limitesTablero(X, Y)) {
					continue;
				}
				while (tableroActivo[X][Y].getEstado() == getOponente(jugador)) {
					if (limitesTablero(X, Y)) {
						break;
					} else {

						ganadas.add(tableroActivo[X][Y]);

						desplazamiento = getDireccion(X, Y, dir);
						int desplX = desplazamiento.get(0);
						int desplY = desplazamiento.get(1);

						if (limitesTablero(desplX, desplY)) {
							break;

						} else {
							X = desplX;
							Y = desplY;
						}

					}
				}
				if (tableroActivo[X][Y].getEstado() == jugador && ganadas.size() > 0) {
					for (int l = 0; l < ganadas.size(); l++) {
						for (int i = 0; i < boardSize; i++) {
							for (int w = 0; w < boardSize; w++) {
								if (tableroActivo[i][w].equals(ganadas.get(l))) {
									tableroActivo[i][w].setEstado(jugador);
								}
							}
						}

					}
				}
			}

			return;
		} else {
			//System.out.println("LOGICA Esta casilla no esta disponible: " + posX + posY);
		}

	}

	// Devuelve los desplazamientos necesarios desde una casilla para moverse en
	// la direccion indicada (dir)
	// NO SE PUEDE HACER LOS .ADD DESPUES DEL SWITCH. SEGURAMENTE SE PODRÍA CON UNA
	// VARIABLE LOCAL
	// SE PUEDE MIRAR

	/*
	 * dir: 0 -> izq 1 -> arriba 2 -> derecha 3 -> abajo 4, 5, 6, 7 -> diagonales
	 */
	private ArrayList<Integer> getDireccion(int posX, int posY, int dir) {

		ArrayList<Integer> desplazamiento = new ArrayList<Integer>();

		switch (dir) {

		case 0:
			posX--;
			desplazamiento.add(posX);
			desplazamiento.add(posY);

		case 1:
			posY--;
			desplazamiento.add(posX);
			desplazamiento.add(posY);

		case 2:
			posX++;
			desplazamiento.add(posX);
			desplazamiento.add(posY);

		case 3:
			posY++;
			desplazamiento.add(posX);
			desplazamiento.add(posY);

		case 4:
			posX--;
			posY--;
			desplazamiento.add(posX);
			desplazamiento.add(posY);

		case 5:
			posX--;
			posY++;
			desplazamiento.add(posX);
			desplazamiento.add(posY);

		case 6:
			posX++;
			posY--;
			desplazamiento.add(posX);
			desplazamiento.add(posY);

		case 7:
			posX++;
			posY++;
			desplazamiento.add(posX);
			desplazamiento.add(posY);

		default:
			desplazamiento.add(posX);
			desplazamiento.add(posY);
		}

		return desplazamiento;
	}

	// Indica si la posicion (posX,posY) está fuera del board

	private boolean limitesTablero(int posX, int posY) {
		if (posX < 0 || posX >= boardSize || posY < 0 || posY >= boardSize) {
			return true;
		}

		return false;
	}

	// Devuelve el oponente del jugador X

	private int getOponente(int jugador) {
		if (jugador == 1) {
			return 2;
		}
		return 1;
	}

	// Resetea el board y coloca las 4 fichas iniciales

	public void resetAI(Cell[][] board) {

		// Creamos el board vacío
		for (int i = 0; i < boardSize; i++) {
			for (int w = 0; w < boardSize; w++) {
				board[i][w] = new Cell(0);
			}
		}
		bResult = false;
		jugadorConTurno = 1;

		// Colocamos fichas iniciales

		// Fichas blancas

		board[boardSize / 2][boardSize / 2].setEstado(2);
		board[(boardSize - 1) / 2][(boardSize - 1) / 2].setEstado(2);

		// Fichas negras

		board[boardSize / 2][(boardSize - 1) / 2].setEstado(1);
		board[(boardSize - 1) / 2][boardSize / 2].setEstado(1);
		blackTiles = contarFichas(this.board, 1);
		whiteTiles = contarFichas(this.board, 2);
		marcaDisponibles(board, jugadorConTurno);
		result = "";
	}

	public void reset() {

		// Creamos el board vacío
		for (int i = 0; i < boardSize; i++) {
			for (int w = 0; w < boardSize; w++) {
				board[i][w] = new Cell(0);
			}
		}
		bResult = false;
		jugadorConTurno = 1;

		// Colocamos fichas iniciales

		// Fichas blancas

		board[boardSize / 2][boardSize / 2].setEstado(2);
		board[(boardSize - 1) / 2][(boardSize - 1) / 2].setEstado(2);

		// Fichas negras

		board[boardSize / 2][(boardSize - 1) / 2].setEstado(1);
		board[(boardSize - 1) / 2][boardSize / 2].setEstado(1);
		blackTiles = contarFichas(this.board, 1);
		whiteTiles = contarFichas(this.board, 2);
		marcaDisponibles(board, jugadorConTurno);
		result = "";
	}

	/* ******************** FUNCIONES PARA IA ****************** */

	

	/*
	 * ACLARACIÓN!!!!!! Uso un ArrayList de ArrayLists<Integer> para tener un array
	 * con las coordenadas La estructura de eso es algo asi:
	 * [(X1,Y1),(X2,Y2),(Xn,Yn)] Donde (Xn,Yn) es el ArrayList<Integer> Se podría
	 * hacer, y sería mejor con un array normal de int[] sería ArrayList<int[]>
	 * puesto que las coordenadas son de tamaño fijo (siempre es un int[1] de tamaño
	 * 2) entonces no necesita el reservado dinamico de memoria que da el ArrayList
	 * no lo hago asi porq para esto da igual no da mucha mas carga a la aplicacion
	 * en cambio con ArrayList es mas comodo manejarlo por las funciones que trae
	 * (los .get(),.add() etc.) con tener cuidado de no añadir a cada
	 * ArrayList<Integer> más de 2 coordenadas ta to ok
	 */

	public GameStruct futurosMovimientos(ArrayList<String> movimientos) {
		//System.out.println("Logica Soy la lógica que usa la IA y los movimientos que me pasó son: " + movimientos);
		reset();
		marcaDisponibles(board,1);
		Cell copiatablero[][] = board;//aqui usaremos un tablero en blanco, en movimientos le pasaremos todos.
		reset();
		boolean myTurn = true;
		int jugador = 1;
		int jugadorConTurno = jugador;

		boolean gameOver = false;
		for (int i = 0; i < movimientos.size(); i++) {
			//System.out.println("Logica El movimiento a realizar es: " + movimientos.get(i).charAt(0) + movimientos.get(i).charAt(1));
			ponerFicha(copiatablero, Integer.parseInt("" + movimientos.get(i).charAt(0)), Integer.parseInt("" + movimientos.get(i).charAt(1)), jugadorConTurno);

			jugadorConTurno = getOponente(jugadorConTurno);
			marcaDisponibles(copiatablero, jugadorConTurno);
			//System.out.println("Logica Las opciones después de jugar " + i + " veces son: " + options);

			if (!comprobarDisponibles(copiatablero, jugadorConTurno)) {//this.board

				//System.out.println("Logica NO HAY MOV POSIBLES PARA " + jugadorConTurno);
				jugadorConTurno = getOponente(jugadorConTurno);
				marcaDisponibles(copiatablero, jugadorConTurno);//This.board
				imprimeTablero(copiatablero);//this.board

				if (!comprobarDisponibles(copiatablero, jugadorConTurno)) {//this.board
					gameOver = true;
					//System.out.println("Logica GAME OVER");
				}
			}

		}

		if (jugadorConTurno == 1) {
			myTurn = false;
		}

		//System.out.println("EL TABLERO");
		//imprimeTablero(copiatablero);
		ArrayList<String> x = new ArrayList<String>();
		x.addAll(movimientos);
		GameStruct struct = new GameStruct(contarFichas(copiatablero, 2),
										   contarFichas(copiatablero, 1),
										   getDisponibles(copiatablero, jugador), x,
										   myTurn, gameOver, squarePosition(movimientos.get(movimientos.size() - 1)),
											borderPosition(movimientos.get(movimientos.size() - 1)));

		return struct;
	}

	public  boolean squarePosition(String position){
		if((position.charAt(0) == '0' || position.charAt(0) == '5' )
			&& (position.charAt(1) == '0' || position.charAt(1) == '5')){
			//System.out.println("es una casilla de esquina");
			return true;
		}

		return false;
	}

	public boolean borderPosition(String position){
		if(position.charAt(0) == '0' || position.charAt(0) == '5'
			|| position.charAt(1) == '0' || position.charAt(1) == '5'){
			//System.out.println("es una casilla de borde");
			return true;
		}
		return false;
	}
	/*
	 * **************************** FUNCIONES SOLO PARA DEBUG
	 * ****************************
	 */

	public int IADebug(Cell tableroDebug[][], int jugador) {
		
		boolean seguirIntroduciendo = true;
		ArrayList<String> movimientosfuturos = new ArrayList<String>();
		System.out.println("DEBUG IA");
		System.out.println("INTRODUCE MOVIMIENTOS FUTUROS (X,Y): ");
		int jugadorConTurno = jugador;
		
		while(seguirIntroduciendo) {
			
			movimientosfuturos.add("" + introduceCasillaSinComprobar(tableroDebug));//esto igual esta mal
			System.out.println("MAS MOVIMIENTOS?: ");								//pero como es una funcion
			System.out.println("1. SI");											//de debug da igual.
			System.out.println("2. NO");

			if(sc.nextInt() != 1) {
				seguirIntroduciendo = false;
			}
			
			jugadorConTurno = getOponente(jugadorConTurno);
		}
		
		
		//GameStruct structDebug = futurosMovimientos(movimientosfuturos, jugador);
		
		System.out.println("LOS RESULTADOS DE ESTOS MOVIMIENTOS SON: ");
		//structDebug.imprimeGameStruct();
		
		return jugadorConTurno;
		
	}

	public void imprimeTablero(Cell tableroImprimir[][]) {
		for (int i = 0; i < boardSize; i++) {
			for (int w = 0; w < boardSize; w++) {
				System.out.print(tableroImprimir[i][w].getEstado() + " ");
				if (w == boardSize - 1) {
					System.out.println("");
				}
			}
		}
	}

	// Para el check de que la casilla introducida está en el board
	// y es un movimiento disponible. Seguramente no sea solo DEBUG
	// por ahora esta comprobacion se hace en los inputs del terminal.
	// Seguramente deberia hacerse en la lógica

	public boolean ComprobarCasillaDisponible(Cell tableroComprobar[][], String index) {

		if (limitesTablero(Integer.parseInt("" + index.charAt(0)), Integer.parseInt("" + index.charAt(1)))) {
			return false;
		}
		if (tableroComprobar[Integer.parseInt("" + index.charAt(0))][Integer.parseInt("" + index.charAt(1))].getEstado() != 8) {
			return false;
		}

		return true;

	}

	// Pide posX y posY por terminal al jugador

	public String introduceCasilla(Cell tableroIntroducir[][]) {
		String indexCasilla = "";

		boolean CasillaAnhadida = false;
		// DEBUG
		while (!CasillaAnhadida) {

			do{
				System.out.print("Indica casilla XY: ");
				indexCasilla = sc.next();
				System.out.println(" index XY selected: " + indexCasilla);

			}while(indexCasilla.length() != 2);

			if (!ComprobarCasillaDisponible(tableroIntroducir, indexCasilla)) {
				System.out.println("ESTA CASILLA NO ESTA PERMITIDA");
			} else {
				CasillaAnhadida = true;
			}

		}

		return indexCasilla;
	}
	
	public ArrayList<Integer> introduceCasillaSinComprobar(Cell tableroIntroducir[][]) {
		ArrayList<Integer> indexCasilla = new ArrayList<Integer>();

		// DEBUG
			System.out.print("Indica casilla Y: ");
			indexCasilla.add(sc.nextInt() - 1);
			System.out.println("indexY: " + indexCasilla.get(0));
			System.out.print("Indica casilla X: ");
			indexCasilla.add(sc.nextInt() - 1);
			System.out.println("indexX: " + indexCasilla.get(1));

		return indexCasilla;
	}

}

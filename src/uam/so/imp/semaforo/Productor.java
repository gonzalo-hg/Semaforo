package uam.so.imp.semaforo;

public class Productor implements Runnable{
	
	static final int COMIDA = 5;
	static int tamales =0;
	static Semaforo sem1 = null;
	static boolean consumidor = true;
	
	public Productor(Semaforo semaforo) {
		// TODO Auto-generated constructor stub
		Productor.sem1 = semaforo;
	}
	
	/** 
	 * Este medoto se utliza ir disminuyendo la variable tarta
	 * si no hay tartas el consumidor se bloquea
	 * */
	static void consumiendo() {
		// TODO Auto-generated method stub
		synchronized (sem1) {
			if(tamales > 0) {
				tamales--;
				System.out.println("Queda "+tamales+ " para comer.");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				sem1.waitSemaforo();
			}
			
			sem1.signal();// verificar si esta 
			consumidor = false;
		}
	}
	
	/** 
	 * Este método se utiliza para volver a cocinar 
	 * alimentos para los conusmidores 
	 * si hay tartas > 0  el cocinero se bloquea
	 * */
	static void cocinando() {
		//synchronized (sem1) {
			if(tamales == 0) {
				//semaforo.release();
				tamales = COMIDA;
				System.out.println("Soy el cocinero y quedan "+tamales+" tamalesmmm");
			}
			else {
				sem1.waitSemaforo();
			}
			sem1.signal();
		//}
	}
			
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//consumidor
		while(true) {
			if(tamales > 0) {
				consumiendo();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				cocinando();
			}//fin if-else
		}//fin while
	}//fin run
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Semaforo semaforo1 = new Semaforo(5);
		Thread[] hilos = new Thread[COMIDA];
				
		for (int i = 0; i < hilos.length; i++) {
			hilos[i] = new Thread(new Productor(semaforo1));
			hilos[i].start();
			
		}
		
		for (int i = 0; i < hilos.length; i++) {
			try {
				hilos[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}

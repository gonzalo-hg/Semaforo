package uam.so.imp.semaforo;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

public class Semaforo {
	
	private ConcurrentLinkedQueue<Thread> cola = new ConcurrentLinkedQueue<Thread>();
	private final AtomicInteger estado;
	@SuppressWarnings("unused")
	private Thread propietario=null;


	
	public Semaforo(){
		
		estado =  new AtomicInteger(0);
	}
	
	/** 
	 * 
	 * */
	public Semaforo(int valor) {

		estado =  new AtomicInteger(valor);
	}

	public void waitSemaforo() {

		boolean interrumpido = false;
		//Hilo actual
		Thread hilo = Thread.currentThread();

		//Se forma el hilo en la cola concurrente
		cola.add(hilo);

		//System.out.println("Thread: "+hilo.getId()+"-> Lock 0");
		System.out.println("estado: "+estado);
		//El hilo se bloquea si no esta al frente o bien si no puede adquirir el candado
		while (!estado.compareAndSet(estado.get(), 0)) {

			//El método park() deshabilita el hilo actual del planificador para propositos de programacion de hilos.
			//El hilo se habilitara si ocurre:
			//   1.- Otro hilo ejecuta el método unpark con el el valor del hilo actual
			//   2.- Otro hilo interumpe al hilo actual
			//   3.- Otra razon propia de eventos en la máquina virtal
			LockSupport.park( );
			estado.decrementAndGet();
			System.out.println("estado wait: "+estado);
			//System.out.println("Thread: "+hilo.getId()+"-> Lock 1");
			if (Thread.interrupted()) // Verifica si el hilo fue interumpido
				interrumpido= true;
		}
		
		propietario=cola.remove(); //Guardamos el propitario del candado
	
		if (interrumpido)          // reassert interrupt status on exit
			hilo.interrupt();      //Interrumpimos el hilo 
		//System.out.println("Thread: "+hilo.getId()+"-> Lock 2");
		
	}
	
/*	public boolean tryWait() {

		boolean flag=estado.compareAndSet(1, 0);
		if(flag) {
			propietario=Thread.currentThread();
			return true;
		}
		return false;
	}*/

	
	/** 
	 * abre el semaforo
	 * */
	public void signal() {

		//estado.set(cola.size());
		estado.incrementAndGet();
		//El metodo unpark (Thread thread) habilita el hilo dado
		LockSupport.unpark(cola.peek());
		System.out.println("estado signal: "+estado);
	}

}

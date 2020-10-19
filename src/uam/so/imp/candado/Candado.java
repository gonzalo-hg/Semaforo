package uam.so.imp.candado;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

public class Candado{

	private ConcurrentLinkedQueue<Thread> cola = new ConcurrentLinkedQueue<Thread>();//crea una cola  de subrpocesos den modo FIFO
	
	
	private final AtomicBoolean estado;
	@SuppressWarnings("unused")
	private Thread propietario=null;


	
	public Candado() {

		estado =  new AtomicBoolean(true);
	}
	
	public Candado(boolean valor) {

		estado =  new AtomicBoolean(valor);
	}
	

    /**
     *Trata de obtener el candado. Si lo obtiene regresa, si no lo obtiene se bloquea
     *
     * @param -----
     * @return ----
     * @throws ----
	 */

	public void lock() {

		boolean interrumpido = false;
		//Hilo actual
		Thread hilo = Thread.currentThread();

		//Se forma el hilo en la cola concurrente
		cola.add(hilo);
		//System.out.println("Thread: "+hilo.getId()+"-> Lock 0");

		//El hilo se bloquea si no esta al frente o bien si no puede adquirir el candado
		while (cola.peek() != hilo || !estado.compareAndSet(true, false)) {

			//El método park() deshabilita el hilo actual del planificador para propósitos de programación de hilos.
			//El hilo se habilitara si ocurre:
			//   1.- Otro hilo ejecuta el método unpark con el el valor del hilo actual
			//   2.- Otro hilo interumpe al hilo actual
			//   3.- Otra razon propia de eventos en la máquina virtal
			LockSupport.park( );

			//System.out.println("Thread: "+hilo.getId()+"-> Lock 1");
			if (Thread.interrupted()) // Verifica si el hilo fue interumpido
				interrumpido= true;
		}

		propietario=cola.remove(); //Guardamos el propitario del candado
	
		if (interrumpido)          // reassert interrupt status on exit
			hilo.interrupt();      //Interrumpimos el hilo 
		//System.out.println("Thread: "+hilo.getId()+"-> Lock 2");
	}

  

	
	public boolean tryLock() {

		boolean flag=estado.compareAndSet(true, false);
		if(flag) {
			propietario=Thread.currentThread();
			return true;
		}
		return false;
	}

	
	
	public void unlock() {

		estado.set(true);
		//El metodo unpark (Thread thread) habilita el hilo dado
		LockSupport.unpark(cola.peek());


	}

}

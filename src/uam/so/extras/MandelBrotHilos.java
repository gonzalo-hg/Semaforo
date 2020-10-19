package uam.so.extras;
/**
 * Se utiliza el estándar de Oracle
 * @author Jose Luis Quiroz Fabian
 *
 */


import java.awt.Color;

import uam.so.imp.candado.Candado;

class MandelBrotHilos extends Thread{

	double iniciox,finx,inicioy,finy;
    static Candado c = new Candado(); 
	
	MandelBrotHilos(double iniciox, double finx,double inicioy, double finy){

		this.iniciox=iniciox;
		this.finx=finx;
		this.inicioy=inicioy;
		this.finy=finy;
	}
	/**
	 * Dibuja el conjunto de Mandelbrot
	 */
	public void run(){

		double epsilon = 0.01; 
		double x;
		double y;
		int maxIteraciones = 10;
		int iteraciones;
		Complejo Z=null;
		Complejo C=null;
		Complejo aux=null;
		/*Se cambia la escala, por default es 0-1*/      
		StdDraw.setXscale(-2,2);
		StdDraw.setYscale(-2,2);
		StdDraw.text(0.0, 1.5, "Conjunto de Mandelbrot");

		for(x=iniciox; x<=finx; x+= epsilon)
		{
			for(y=inicioy; y<=finy; y+= epsilon)
			{
				
				iteraciones = 0;
				C = new Complejo(x, y);
				Z = new Complejo(0,0);

				while(Complejo.abs(Z) < 2 && iteraciones < maxIteraciones)
				{
					aux = Complejo.suma(Complejo.mult(Z, Z),C);
					Z = aux;
					iteraciones++;
				}
				if(iteraciones==maxIteraciones){   
					c.lock();
					StdDraw.setPenColor(Color.RED);
					StdDraw.point(x, y);
					c.unlock();
				}else{
					c.lock();
					StdDraw.setPenColor(Color.BLUE);
					StdDraw.point(x, y);
					c.unlock();
				}	
			}
		}  


	}



	public static void main(String arg[]){

		int j;
		double i;

		MandelBrotHilos[] t = new MandelBrotHilos[4];
		for(i=-2.0,j=0;i<2;i++,j++) {
			t[j]= new MandelBrotHilos(i,i+1,-2,2);
			t[j].start();


		}
		for(j=0;j<4;j++) 
			try {
				t[j].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


	}
}

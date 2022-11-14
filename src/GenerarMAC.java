import java.util.Random;

public class GenerarMAC {

	public static void main(String[] args) {
		String[] dadesAleatories = new String[50];
		Random aleatori = new Random();
		
		generarMAC(dadesAleatories, aleatori);
		
		for(int i=0;i<dadesAleatories.length;i++) {
			System.out.println(dadesAleatories[i]);
		}
	}

	public static void generarMAC(String[] dadesAleatories, Random aleatori) {
		String[] numeros = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
		int numero;
		String mac;
		for (int i=0;i<dadesAleatories.length;i++) {
			mac = "";
			for(int i2=0;i2<4;i2++) {
				for(int i3=0;i3<2;i3++) {
					mac += numeros[aleatori.nextInt(16)];
				}
				if (i2 != 3) {
					mac+= "-";
				}
			}
			dadesAleatories[i] = mac;
		}
	}
}

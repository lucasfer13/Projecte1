import java.util.Random;

public class GenerarNull {

	public static void main(String[] args) {
		String dadesAleatories[] = {"AAAA", "BBBB", "CCCC", "DDDD", "EEEE"};
		int pBlancs = 40;
		generarNull(dadesAleatories, pBlancs, new Random());
		for(int i=0;i<dadesAleatories.length;i++) {
			System.out.println(dadesAleatories[i]);
		}
	}
	
	public static void generarNull(String[] dadesAleatories, int pBlancs, Random aleatori) {
		int nBlancs = (int) (dadesAleatories.length*((pBlancs*1.0)/100));
		int posicio;
		
		for(int i=0;i<nBlancs;i++) {
			posicio = aleatori.nextInt(dadesAleatories.length);
			if (dadesAleatories[posicio] == null) {
				i--;
			} else {
				dadesAleatories[posicio] = null;
			}
		}
	}
}

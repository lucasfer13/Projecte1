import java.io.File;
import java.io.PrintStream;

public class CSV {
	public static void generarCsv(String [][] dadesAleatories,String [][] camps, File sortida) {
		try {
			File sortidaCsv = new File (sortida.getParent()+"\\csv.csv");
			if (!sortidaCsv.exists()) {
				sortidaCsv.createNewFile();
			}
			sortidaCsv.createNewFile();
			PrintStream out = new PrintStream(sortidaCsv);
			for(int i=0; i<camps.length;i++) {
				if(i==(camps.length-1)) {
						out.print(""+camps[i][0]);
				} else out.print(""+camps[i][0]+";");
			}
			out.println();
			for(int i=0; i<dadesAleatories[i].length;i++) {
				for(int j=0; j<dadesAleatories.length;j++) {
					if(j==(dadesAleatories.length)-1) out.print(""+dadesAleatories[j][i]);
					else out.print(""+dadesAleatories[j][i]+";");
				}
				out.println();
			}
		}catch (Exception ex) {
			System.out.println(ex);
		}
	}
}

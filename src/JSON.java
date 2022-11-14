import java.io.File;
import java.io.PrintStream;

public class JSON {
	public static void sortidaJSON(String dadesAleatories[][],String camps[][], File sortida ) {
		PrintStream escriuJSON;
		try {
			escriuJSON =new PrintStream(sortida);
			escriuJSON.println("{");
			escriuJSON.println('"'+"camps"+'"'+":"+'"'+"dades"+'"'+",");
			escriuJSON.println(" "+'"'+"dades"+'"'+": [");
			for (int i=0;i<dadesAleatories[0].length;i++) {
				escriuJSON.println(" {");
				for(int a=0;a<camps.length;a++) {
					if(a==camps.length-1)
					{//Si es l'ultim no cal cap coma
						escriuJSON.println("	"+'"'+camps[a][0]+'"'+":"+'"'+dadesAleatories[a][i]+'"');
					}else {
						escriuJSON.println("	"+'"'+camps[a][0]+'"'+":"+'"'+dadesAleatories[a][i]+'"'+",");
					}
				}
				if(i==dadesAleatories[0].length-1)escriuJSON.println("	}");
				else escriuJSON.println("	},");
			}
			escriuJSON.println("]");
			escriuJSON.println("}");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

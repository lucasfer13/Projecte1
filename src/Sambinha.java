import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Sambinha{

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		Random aleatori = new Random();
		int opcio = 0, maxim, minim, decimals, anyInicial, anyMaxim, iniciCont, numRegistres, numLinies, i, i2, cont = 0, id;
		int numLletres, numNumeros, numMajuscules, numMinuscules, numSimbols, llarg;
		int linies[] = {0};
		String[][] camps = null, dadesAleatories = null, dades = new String [10][200];
		String tmp, formatSortida = null, nomDomini = null, extensioDomini = null, opcioS = null;
		String[] linia=null;
		String [] nomsEtiquetes = new String[2];
		String nomSortida="";
		int numSortida =0;
		BufferedReader br;
		FileReader fr;
		File entrada, sortida = null;
		boolean end = false, opcioValida = false, nom = false;
		extreureDades(dades);
		
		while(!end) {
			// El programa primer mostra el menu amb les diferents funcions i es pregunta que es vol fer.
			// Comprova que l'opcio es valida
			opcioValida = false;
			while (!opcioValida) {
				menu();
				opcioS = in.nextLine();
				if (opcioS.length() == 1 && Character.isDigit(opcioS.charAt(0))) {
					opcio = Integer.parseInt(opcioS);
					if (opcio >= 1 || opcio <= 2) {
						opcioValida = true;
					}
				}
				if (!opcioValida) {
					System.out.println("Opció no valida.");
				}
			}
			// La opcio 1 es per pasar el fitxer d'entrada i que generi el fitxer de sortida
			if (opcio==1) {
				linies[0] = 0;
				// Comprova que fican una ruta valida
				entrada = new File("");
				while (!entrada.exists()) {
					System.out.print("Introdueix la ruta de l'arxiu inicial: ");
					entrada = new File(in.nextLine());
					if (!entrada.exists()) {
						System.out.println("No existeix l'arxiu d'entrada");
					}
				}
				
				// Primer verifica amb una funcio que el fitxer es valid
				if (validarEntrada(entrada, linies)) {
					try {
					// Despres es guarda totes les dades del fitxer en una array
						System.out.println("Arxiu validat correctament!");
						fr = new FileReader(entrada);
						br = new BufferedReader(fr);
						numLinies = linies[0]-1;
						tmp = br.readLine();
						linia = tmp.split(" ");
						numSortida = linia.length;
						sortida = new File(linia[0]);
						if(linia[2].equals("XML") && linia.length == 4) {
							nomsEtiquetes[0] = "registres";
							nomsEtiquetes[1] = "registre";
						}
						else if(linia.length==4) {
							nomSortida = linia[3];
						}
						else if(linia.length==5) {
							nomSortida = linia[4];
							nomsEtiquetes = linia[3].split("/");
						} 
						numRegistres = Integer.parseInt(linia[1]);
						formatSortida = linia[2];
						camps = new String[numLinies][9];
						dadesAleatories = new String[numLinies][numRegistres];
						cont = 0;
						while(br.ready()) {
							tmp = br.readLine();
							linia = tmp.split(" ");
							for (i=0;i<linia.length;i++) {
								camps[cont][i] = linia[i];
							}
							cont++;
						}
						br.close();
						fr.close();
					} catch (Exception error) {
						error.printStackTrace();
					}
					// Despres es cridan les funcions per guardar les dades en una array segons els tipus de dades que habia en cada linia
					for (i=0;i<camps.length;i++) {
						// Primer es posen els valors per defecte
						id = Integer.parseInt(camps[i][1]);
						maxim = 1000; 
						minim = decimals = 0;
						iniciCont = 1;
						anyMaxim = 2023;
						anyInicial = 1900;
						extensioDomini = ".com";
						
						if (id >= 0 && id <= 10) {
							if (id == 1) {
								if (!nom) {
									aleatoritzarDades(dadesAleatories[i], dades[id-1], aleatori);
								} else {
									nom = false;
								}
							} else {
								aleatoritzarDades(dadesAleatories[i], dades[id-1], aleatori);
							}
							
						} else {
							switch (id) {
								case 11:
									generarBoolean(aleatori, dadesAleatories[i]);
									break;
								case 12:
									// Es mira si hi han parametres opcionals
									for (i2 = 2;i2<5 && !(camps[i][i2] == null);i2++) {
										if (camps[i][i2].charAt(0) == 'M') {
											minim = Integer.parseInt(camps[i][i2].substring(1));
										} else if (camps[i][i2].charAt(0) == 'X') {
											maxim = Integer.parseInt(camps[i][i2].substring(1));
										} else if (camps[i][i2].charAt(0) == 'D') {
											decimals = Integer.parseInt(camps[i][i2].substring(1));;
										}
									}
									generarNumber(aleatori, dadesAleatories[i], decimals, maxim, minim);
									break;
								case 13:
									nomDomini = dades[9][aleatori.nextInt(200)].toString().split(" ")[0].toLowerCase();
									for (i2 = 2;i2<4 && !(camps[i][i2] == null);i2++) {
										if (camps[i][i2].charAt(0) == 'D') {
											nomDomini = camps[i][i2].substring(1);
										} else if (camps[i][i2].charAt(0) == 'X') {
											extensioDomini = camps[i][i2].substring(1);
										}
									}
									nom = false;
									for (i2 = 0;i2 < camps.length && !nom; i2++) {
										if (camps[i2][1].equals("1")) {
											nom = true;
										}
									}
									i2--;
									// Comprova si es demanen noms per generar els emails amb concordancia amb els nom del registre.
									if (i2>i && nom) {
										aleatoritzarDades(dadesAleatories[i2], dades[0], aleatori);
										generarEmail(dadesAleatories[i], dadesAleatories[i2], nomDomini, extensioDomini, false);
									} else if (i2<i && nom) {
										generarEmail(dadesAleatories[i], dadesAleatories[i2], nomDomini, extensioDomini, false);
										nom = false;
									} else {
										generarEmail(dadesAleatories[i], dades[0], nomDomini, extensioDomini, true);
									}
									break;
								case 14:
									generarIp(dadesAleatories[i], aleatori);
									break;
								case 15:
									// int numLletres, numNumeros, numMajuscules, numMinuscules, numSimbols, llarg;
									numLletres = Integer.parseInt(camps[i][2]);
									numNumeros = Integer.parseInt(camps[i][3]);
									numMajuscules = Integer.parseInt(camps[i][4]);
									numMinuscules = Integer.parseInt(camps[i][5]);
									numSimbols = Integer.parseInt(camps[i][6]);
									llarg = Integer.parseInt(camps[i][7]);
									//int longitud, int lletres, int majuscules, int minuscules, int numeros, int simbols
									generarPassword(dadesAleatories[i], llarg, numLletres, numMajuscules, numMinuscules, numNumeros, numSimbols, aleatori);
									break;
								case 16:
									for (i2=2;i2<4 && !(camps[i][i2] == null);i2++) {
										if (camps[i][i2].charAt(0) == 'M') {
											anyInicial = Integer.parseInt(camps[i][i2].substring(1));
										} else if (camps[i][i2].charAt(0) == 'X') {
											anyMaxim = Integer.parseInt(camps[i][i2].substring(1));
										}
									}
									generarData(aleatori, dadesAleatories[i], anyInicial, anyMaxim);
									break;
								case 17:
									generarIban(dadesAleatories[i]);
									break;
								case 18:
									generarDNI(aleatori, dadesAleatories[i]);
									break;
								case 19:
									if (!(camps[i][2] == null)) {
										iniciCont = Integer.parseInt(camps[i][2]);
									}
									generarAutonumeric(dadesAleatories[i], iniciCont);
									break;
								case 20:
									generarMAC(dadesAleatories[i], aleatori);
									break;
								case 21:
									generarColorsHexadecimals(dadesAleatories[i], aleatori);
									break;
								case 22:
									colorsRGB(dadesAleatories[i]);
									break;
							}
						}
					}
					for (i=0;i<camps.length;i++) {
						for (i2=2;i2<camps[i].length;i2++) {
							if (camps[i][i2] != null && camps[i][i2].charAt(0) == 'B') {
								int percentatje = Integer.parseInt(camps[i][i2].substring(1));
								generarNull(dadesAleatories[i], percentatje, aleatori);
							}
						}
					}
					
					mostraPre(dadesAleatories, camps);
					
					// Mira quin format de sortida s'habia especificat i segons quin sigui crida la funcio corresponen per generar l'arxiu de sortida
					if(despresDePre(dadesAleatories,camps)==1)
					{
						if (formatSortida.equals("XML")) {
							if(numSortida == 4 || numSortida == 5) {
								sortida = new File(sortida.getAbsolutePath()+"\\"+nomSortida+".xml");
							}else {
								sortida = new File(sortida.getAbsolutePath()+"\\dades.xml");								
							}
							try {
								if (!sortida.exists()) {
									sortida.createNewFile();
								}
								sortidaXML(dadesAleatories, camps, sortida, nomsEtiquetes);
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
							generarXSLT(camps, sortida, nomsEtiquetes);
							generarXSD(camps, sortida, nomsEtiquetes);
							System.out.println("Generat arxiu XML.");
						} else if (formatSortida.equals("SQL")) {
							if(numSortida == 4) {
							sortida = new File(sortida.getAbsolutePath()+"\\"+nomSortida+".sql");
							}else {
								sortida = new File(sortida.getAbsolutePath()+"\\dades.sql");								
							}
							if (!sortida.exists()) {
								try {
									sortida.createNewFile();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							generarSQL(camps, dadesAleatories, sortida);
							System.out.println("Generat arxiu SQL.");
						} else if (formatSortida.equals("CSV")) {
							if(numSortida == 4) {
								sortida = new File(sortida.getAbsolutePath()+"\\"+nomSortida+".csv");
							}else {
								sortida = new File(sortida.getAbsolutePath()+"\\dades.csv");
							}
							if (!sortida.exists()) {
								try {
									sortida.createNewFile();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							generarCSV(dadesAleatories, camps, sortida);
						} else if (formatSortida.equals("JSON")) {
							if(numSortida == 4) {
								sortida = new File(sortida.getAbsolutePath()+"\\"+nomSortida+".json");
							}else {
								sortida = new File(sortida.getAbsolutePath()+"\\dades.json");								
							}
							if (!sortida.exists()) {
								try {
									sortida.createNewFile();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							generarJSON(dadesAleatories, camps, sortida);
						}
						System.out.println();
					}
				}
			} else if (opcio == 0) {
				end = true;
			} else if (opcio == 2) {
				mostrarDades(in);
			} else if (opcio == 3) {
				entrada = new File("");
				while (!entrada.exists()) {
					System.out.print("Introdueix la ruta de l'arxiu inicial: ");
					entrada = new File(in.nextLine());
					if (!entrada.exists()) {
						System.out.println("No existeix l'arxiu d'entrada");
					}
				}
				if (validarEntrada(entrada, linies)) {
					System.out.println("Arxiu correcte!");
				}
			}
		}
	}
	
	// Funcio per generarXSD
	public static void generarXSD(String[][] camps, File sortida, String[] etiquetes) {
		File sortidaXsd = new File (sortida.getParent()+"\\xsd.xsd");
		try {
			if (!sortidaXsd.exists()) {
				sortidaXsd.createNewFile();
			}
			sortidaXsd.createNewFile();
			PrintStream out = new PrintStream(sortidaXsd);
			out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			out.println("<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" elementFormDefault=\"qualified\">");
			out.println("\t<xs:element name=\""+etiquetes[0]+"\">");
			out.println("\t\t<xs:complexType>");
			out.println("\t\t\t<xs:sequence>");
			out.println("\t\t\t\t<xs:element ref=\""+etiquetes[1]+"\" maxOccurs=\"unbounded\"/>");
			out.println("\t\t\t</xs:sequence>");
			out.println("\t\t</xs:complexType>");
			out.println("\t</xs:element>");
			out.println("\t<xs:element name=\""+etiquetes[1]+"\">");
			out.println("\t\t<xs:complexType>");
			out.println("\t\t\t<xs:sequence>");
			// Per cada tipus de dada el programa fica diferents restriccions.
			for(int i=0;i<camps.length;i++) {
				if(camps[i][1].equals("11")) { //BOOLEAN
					out.println("\t\t\t\t<xs:element name=\""+camps[i][0]+"\" type=\"xs:boolean\" nillable=\"true\"/>");
				}
				else if(camps[i][1].equals("12")) { //NUMBER
					out.println("\t\t\t\t<xs:element name=\""+camps[i][0]+"\" nillable=\"true\">");
					out.println("\t\t\t\t\t<xs:simpleType>");
					out.println("\t\t\t\t\t\t<xs:restriction base=\"xs:decimal\">");
					out.println("\t\t\t\t\t\t\t<xs:minInclusive value=\"0\"/>");
					out.println("\t\t\t\t\t\t</xs:restriction>");
					out.println("\t\t\t\t\t</xs:simpleType>");
					out.println("\t\t\t\t</xs:element>");
				}
				else if(camps[i][1].equals("15")) { //PASSWORD
					out.println("\t\t\t\t<xs:element name=\""+camps[i][0]+"\" nillable=\"true\">");
					out.println("\t\t\t\t\t<xs:simpleType>");
					out.println("\t\t\t\t\t\t<xs:restriction base=\"xs:string\">");
					out.println("\t\t\t\t\t\t\t<xs:length value=\""+camps[i][7]+"\"/>");
					out.println("\t\t\t\t\t\t</xs:restriction>");
					out.println("\t\t\t\t\t</xs:simpleType>");
					out.println("\t\t\t\t</xs:element>");
				}
				else if(camps[i][1].equals("17")) { //IBAN
					out.println("\t\t\t\t<xs:element name=\""+camps[i][0]+"\" nillable=\"true\">");
					out.println("\t\t\t\t\t<xs:simpleType>");
					out.println("\t\t\t\t\t\t<xs:restriction base=\"xs:string\">");
					out.println("\t\t\t\t\t\t\t<xs:pattern value=\"[E][S][0-9]{22}\"/>");
					out.println("\t\t\t\t\t\t</xs:restriction>");
					out.println("\t\t\t\t\t</xs:simpleType>");
					out.println("\t\t\t\t</xs:element>");
				}
				else if(camps[i][1].equals("18")) { //DNI
					out.println("\t\t\t\t<xs:element name=\""+camps[i][0]+"\" nillable=\"true\">");
					out.println("\t\t\t\t\t<xs:simpleType>");
					out.println("\t\t\t\t\t\t<xs:restriction base=\"xs:string\">");
					out.println("\t\t\t\t\t\t\t<xs:pattern value=\"[0-9]{8}[A-Z]{1}\"/>");
					out.println("\t\t\t\t\t\t</xs:restriction>");
					out.println("\t\t\t\t\t</xs:simpleType>");
					out.println("\t\t\t\t</xs:element>");
				}
				else if(camps[i][1].equals("19")) { //AUTONUMÈRIC
					out.println("\t\t\t\t<xs:element name=\""+camps[i][0]+"\" type=\"xs:positiveInteger\" default=\"1\" nillable=\"true\"/>");
				} 
				else if(camps[i][1].equals("20")) { // MAC
					out.println("\t\t\t\t<xs:element name=\""+camps[i][0]+"\" nillable=\"true\">");
					out.println("\t\t\t\t\t<xs:simpleType>");
					out.println("\t\t\t\t\t\t<xs:restriction base=\"xs:string\">");
					out.println("\t\t\t\t\t\t\t<xs:pattern value=\"[0-9ABCDEF]{2}-[0-9ABCDEF]{2}-[0-9ABCDEF]{2}-[0-9ABCDEF]{2}\"/>");
					out.println("\t\t\t\t\t\t</xs:restriction>");
					out.println("\t\t\t\t\t</xs:simpleType>");
					out.println("\t\t\t\t</xs:element>");
				}
				else { //LA RESTA DE CAMPS
					out.println("\t\t\t\t<xs:element name=\""+camps[i][0]+"\" type=\"xs:string\" nillable=\"true\"/>");
				}
			}
			out.println("\t\t\t</xs:sequence>");
			out.println("\t\t</xs:complexType>");
			out.println("\t</xs:element>");
			out.println("</xs:schema>");
			out.flush();
			out.close();
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}
	
	// Funcio per poder ficar les dades en una array i despres fer aleatori les seves posicions
	public static void aleatoritzarDades(String[] dadesAleatories, String[] dades, Random aleatori) {
		int canvi, i;
		String tmp;
		for(i=0;i<dadesAleatories.length;i++) {
			dadesAleatories[i] = dades[i%200];
		}
		
		for (i=0;i<dadesAleatories.length;i++) {
			canvi = aleatori.nextInt(dadesAleatories.length);
			tmp = dadesAleatories[canvi];
			dadesAleatories[canvi] = dadesAleatories[i];
			dadesAleatories[i] = tmp;
		}
	}

	// Funció per generar arxiu XSLT
	public static void generarXSLT(String[][] camps, File sortida, String[] etiquetes) {
		int i;
		File sortidaXslt = new File(sortida.getParent()+"\\xslt.xsl");
		try {
			if (!sortidaXslt.exists()) {
				sortidaXslt.createNewFile();
			}
			PrintStream out = new PrintStream(sortidaXslt);
			out.println("<?xml version=\"1.0\"?>");
			out.println("<xsl:stylesheet version=\"1.0\"" + " xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">");
			
			out.println("<xsl:template match=\"/\">");
			out.println("	<html>");
			out.println("		<head>");
			out.println("			<style>");
			out.println("				table, th, td {");
			out.println("					border: 1px solid black");
			out.println("				}");
			out.println("			</style>");
			out.println("		</head>");
			out.println("		<body>");
			out.println("			<table>");
			out.println("				<tr>");
			// Al generar la taula primer va el encapçalament que conte el nom dels camps
			for(i=0;i<camps.length;i++) {
				out.println("					<th>"+camps[i][0]+"</th>");
			}
			out.println("				</tr>");
			out.println("				<xsl:for-each select=\""+etiquetes[0]+"/"+etiquetes[1]+"\">");
			out.println("					<tr>");
			for (i=0;i<camps.length;i++) {
				out.println("						<td><xsl:value-of select=\""+camps[i][0]+"\"/></td>");
			}
			out.println("					</tr>");
			out.println("				</xsl:for-each>");
			out.println("			</table>");
			out.println("		</body>");
			out.println("	</html>");
			out.println("</xsl:template>");
			out.println("</xsl:stylesheet>");
			out.flush();
			out.close();
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
	
	public static void sortidaXML (String dadesAleatories[][],String camps[][], File sortida, String[] etiquetes) {
		PrintStream escriuXML;
		try {
			escriuXML = new PrintStream (sortida);
			int a;
			escriuXML.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			escriuXML.println("<?xml-stylesheet type=\"text/xls\" href=\"xslt.xsl\"?>");
			escriuXML.println("	<"+etiquetes[0]+" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"xsd.xsd\">");
			for(int i=0;i<dadesAleatories[0].length;i++) {
	
				escriuXML.println("		<"+etiquetes[1]+">");
				for(a=0;a<camps.length;a++) {
					// Per generar el XML es fan varies etiquetes "dades" on dintre hi son les dades amb les etiquetes amb el nom del camp
					if (dadesAleatories[a][i] == null) {
						escriuXML.println("			<"+camps[a][0]+"></"+camps[a][0]+">");
					} else {
						escriuXML.println("			<"+camps[a][0]+">"+dadesAleatories[a][i]+"</"+camps[a][0]+">");
					}
				}
				escriuXML.println("		</"+etiquetes[1]+">");
				
			}
			escriuXML.println("	</"+etiquetes[0]+">");
			escriuXML.flush();
			escriuXML.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}

	// Funcio per generar l'arxiu de sortida SQL
	public static void generarSQL(String[][] camps, String[][] dades, File sortida) {
		int i, i2, id = 0;
		
		try {
			PrintStream out = new PrintStream(sortida);
			// Primer crea la taula, possa el nom del camp i despres segons la ip canvia el seu tipus de dada
			out.println("CREATE TABLE data (");
			for(i=0;i<camps.length;i++) {
				id = Integer.parseInt(camps[i][1]);
				if ((id >= 1 && id <= 10) || (id >= 13 && id <= 16)) {
					out.print("\t"+camps[i][0]+" VARCHAR(50)");
				} else if (id == 11) {
					out.print("\t"+camps[i][0]+" BOOLEAN");
				} else if (id == 12) {
					out.print("\t"+camps[i][0]+" DOUBLE");
				} else if (id == 19) {
					out.print("\t"+camps[i][0]+" INTEGER");
				} else if (id == 18) {
					out.print("\t"+camps[i][0]+" VARCHAR(9)");
				} else if (id == 20) {
					out.print("\t"+camps[i][0]+" VARCHAR(11)");
				} else {
					out.print("\t"+camps[i][0]+" VARCHAR(24)");
				}
				if (i+1 != camps.length) {
					out.print(",");
				}
				out.println();
			}
			out.println(");");
			out.println();
			// Finalment genera les insercions, nomes controla que si el tipus de dada es varchar els posa entre dobles cometes.
			for (i=0;i<dades[0].length;i++) {
				out.print("INSERT INTO data VALUES (");
				for (i2=0;i2<dades.length;i2++) {
					id = Integer.parseInt(camps[i2][1]);
					if (((id >= 1 && id <= 10) || (id >= 13 && id <= 18) || id == 20 || id == 21) && dades[i2][i] != null) {
						out.print("\""+dades[i2][i]+"\"");
					} else if (dades[i2][i] != null) {
						out.print(dades[i2][i]);
					} else {
						out.print("null");
					}
					if (i2+1 != camps.length) {
						out.print(",");
					}
				}
				out.print(");");
				if (i+1 != dades[0].length) {
					out.println();
				}
			}
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();;
		}
	}

	// Funcio per generar un autoincremental
	public static void generarAutonumeric(String[] dades, int iniciCont) {
		int total=iniciCont;
		for(int i=0;i<dades.length;i++) {
			dades[i]=total+"";
			total++;
		}
	}

	// Funcio per generar DNI, primer genera un numero aleatori de 8 digits i despres calcula quina es la lletra
	public static void generarDNI(Random aleatori, String[] dades) {
		int residu, tmp;
		char letras[] = {'T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E'};
		
		for (int i=0;i<dades.length;i++) {
			tmp = aleatori.nextInt(99999999-10000000)+10000000;
			residu = tmp % 23;
			dades[i] = tmp + "" + letras[residu];
		}
	}

	// Funcio per generar IBAN
	public static void generarIban(String[] dades) {
		String[] entitatBancaria = {"2080","1544","0241","0188","0182","0130","0234","9000","2105","0240","0487","0186","0238","3873","2038","0128","0138","0152","3842","3025","2100","2045","3035","3081","2000","1474","3877","0239","2085","1465","2095","2048","0131","0108","2103"};
		String[] oficinaBancaria = {"5778","1809","5461","6828","3378","3380","6641","1454","2420","7675","8759","6759","3570","4610","3656","2465","0203","3554","1197","0148","3138","3051","0840","0870","0286","2699","2211","2850","4828","6308","3830"};
		for(int y=0; y<dades.length; y++)
		{
			//CREACIO DE CONTABANCARIA:
			int[] contaBancaria = new int[10];
			for(int i=0; i<10;i++) {
				contaBancaria[i]=(int)(Math.random()*10+0);
			}
			//TROBAR NUMERO CONTROL:
			//PRIMER NUMERO
			//ENTITAT BANCARIA
			int indexEntitat = (int)(entitatBancaria.length*Math.random());
			int sumaA=0;
			for(int i=0;i<4;i++)
			{
				char numero = entitatBancaria[indexEntitat].charAt(i);
				int numeroActual = Character.getNumericValue(numero);
				if(i==0) {sumaA=sumaA+(numeroActual*4);} if(i==1) {sumaA=sumaA+(numeroActual*8);}
				if(i==2) {sumaA=sumaA+(numeroActual*5);} if(i==3) {sumaA=sumaA+(numeroActual*10);}
			}
			//OFICINA BANCARIA
			int indexOficina = (int)(oficinaBancaria.length*Math.random());
			int sumaB=0;
			for(int i=0;i<4;i++)
			{
				char numero = oficinaBancaria[indexOficina].charAt(i);
				int numeroActual = Character.getNumericValue(numero);
				if(i==0) {sumaB=sumaB+(numeroActual*9);} if(i==1) {sumaB=sumaB+(numeroActual*7);}
				if(i==2) {sumaB=sumaB+(numeroActual*3);} if(i==3) {sumaB=sumaB+(numeroActual*6);}
			}
			int restoC = 11-((sumaA+sumaB) % 11);
			if(restoC == 10) restoC = 1;
			else if (restoC == 11) restoC = 0;
			
			//SEGON NUMERO
			//CONTA BANCARIA
			int sumaD=0;
			for(int i=0;i<10;i++)
			{
				int numeroActual = contaBancaria[i];
				if(i==0) {sumaD=sumaD+(numeroActual*1);} if(i==1) {sumaD=sumaD+(numeroActual*2);}
				if(i==2) {sumaD=sumaD+(numeroActual*4);} if(i==3) {sumaD=sumaD+(numeroActual*8);}
				if(i==4) {sumaD=sumaD+(numeroActual*5);} if(i==5) {sumaD=sumaD+(numeroActual*10);}
				if(i==6) {sumaD=sumaD+(numeroActual*9);} if(i==7) {sumaD=sumaD+(numeroActual*7);}
				if(i==8) {sumaD=sumaD+(numeroActual*3);} if(i==9) {sumaD=sumaD+(numeroActual*6);}
			}
			int restoE = 11-(sumaD % 11);
			if(restoE == 10) restoE = 1;
			else if (restoE == 11) restoE = 0;
			//TROBAR NUMERO ES	
			BigInteger numeroComplet = new BigInteger (entitatBancaria[indexEntitat]+""+oficinaBancaria[indexOficina]+""+restoC+""+restoE+""+contaBancaria[0]+""+contaBancaria[1]+""+contaBancaria[2]+""+contaBancaria[3]+""+contaBancaria[4]+""+contaBancaria[5]+""+contaBancaria[6]+""+contaBancaria[7]+""+contaBancaria[8]+""+contaBancaria[9]+""+142800);
			BigInteger modul = new BigInteger(""+97);
			int numeroES = numeroComplet.mod(modul).intValue();
			int total = 98 - numeroES;
			//MOSTRAR IBAN
			BigInteger numeroComplet2 = new BigInteger (entitatBancaria[indexEntitat]+""+oficinaBancaria[indexOficina]+""+restoC+""+restoE+""+contaBancaria[0]+""+contaBancaria[1]+""+contaBancaria[2]+""+contaBancaria[3]+""+contaBancaria[4]+""+contaBancaria[5]+""+contaBancaria[6]+""+contaBancaria[7]+""+contaBancaria[8]+""+contaBancaria[9]);
			if(total<=9) {
				if(entitatBancaria[indexEntitat].charAt(0)=='0') dades[y]="ES"+"0"+total+"0"+numeroComplet2;
				else dades[y]="ES"+"0"+total+numeroComplet2;
			}
			else {	
				if(entitatBancaria[indexEntitat].charAt(0)=='0') dades[y]="ES"+total+"0"+numeroComplet2;	
				else dades[y]="ES"+total+numeroComplet2;
			}
		}	
			
	}

	// Funcio per generar dates aleatories
	public static void generarData(Random num, String[] dades, int vell, int nou) {
		int any,dia,Nmes;
		String mes;
		String []TriaMes ={"Gener","Febrer","Marz","Abril","Maig","Juny","Juliol","Agost","Setembre","Octubre","Novembre","Desembre"};
		
		for(int i=0;i<dades.length;i++)
		{
			any=vell+num.nextInt(nou-vell);
			Nmes=1+num.nextInt(12);
			mes=TriaMes[Nmes-1];
			if((Nmes%2!=0&&Nmes<8)||(Nmes%2==0&&Nmes>=8)) //si el mes es imparell tindra 31 dies
			{
				dia=1+num.nextInt(31);
			}else {
				dia=1+num.nextInt(30);
			}
			if(Nmes==2&&any%4!=0) {
				dia=1+num.nextInt(28);
			}else if(Nmes==2&&any%4==0) {
				dia=1+num.nextInt(29);
			}
			dades[i]=dia+" de "+mes+" del "+any;
		}
	}
	
	// Funcio per generar passwords
	public static void generarPassword(String dades[], int longitud, int lletres, int majuscules, int minuscules, int numeros, int simbols, Random aleatori) {
		String simbol= "@#:;.,?/()|-_}{^*!%=~, +";
		String numbers="0123456789";
		int tipusCaracter, ascii, i, i2, cmajuscules = majuscules, clletres = lletres, cminuscules = minuscules, csimbols = simbols, cnumeros = numeros;
		char caracter = 0;
		String password;
		
		
		for(i2=0;i2<dades.length;i2++) {
			password = "";
			majuscules = cmajuscules;
			numeros = cnumeros;
			lletres = clletres;
			minuscules = cminuscules;
			simbols = csimbols;
			for(i=0;i<longitud;i++) {
				// La funcio primer decideix aleatoriament quin caracter posara.
				tipusCaracter = aleatori.nextInt(4)+1;
				switch (tipusCaracter) {
					case 1:
						// Mira si aquest caracter es pot posar sense afectar a les condicions minimes si afecta tornara a escollir.
						if (minuscules > 0 || (majuscules <= 0 && simbols <= 0 && numeros <= 0)) {
							ascii = aleatori.nextInt(122-97)+97;
							caracter = (char) ascii;
							minuscules--;	
							lletres--;
						} else {
							caracter = 0;
							i--;
						}
						break;
					case 2:
						if (majuscules > 0 || (minuscules<=0 && simbols<=0 && numeros <= 0)) {
							ascii = aleatori.nextInt(90-65)+65;
							caracter = (char) ascii;
							majuscules--;
							lletres--;
						} else {
							caracter = 0;
							i--;
						}
						break;
					case 3:
						if (simbols>0 || (minuscules<=0 && majuscules<=0 && lletres <=0 && numeros <= 0)) {
							ascii = aleatori.nextInt(simbol.length());
							caracter = simbol.charAt(ascii);
							simbols--;
						} else {
							caracter = 0;
							i--;
						}
						break;
					case 4:
						if (numeros>0 || (minuscules<=0 && majuscules<=0 && lletres <=0 && simbols <= 0)) {
							ascii = aleatori.nextInt(10);
							caracter = numbers.charAt(ascii);
							numeros--;
						} else {
							caracter = 0;
							i--;
						}
						break;
				}
				if (caracter != 0) {
					password=password+caracter+"";
				}
			}
			dades[i2] = password;
		}
	}
	
	// Funcio per generar colors hexadecimals
	public static void generarColorsHexadecimals(String dades[], Random aleatori) {
		char [] opcions = {'A','B','C','D','E','F','1','2','3','4','5','6','7','8','9','0'}; //posibles combinacions que hi ha dintre de un color hexadecimal
		
		for(int i=0; i<dades.length;i++) {
			String color = "#";
			for(int j=0;j<6;j++) { 
				color += opcions[aleatori.nextInt(opcions.length)]; 
			}
			dades[i] = color;
		}
	}
	
	// Funcio per generar IP
	public static void generarIp(String[] dades, Random num) {
		int primer,segon,tercer,cuart;
		for(int i=0;i<dades.length;i++) 
		{
			primer=1+num.nextInt(254);
			segon=1+num.nextInt(254);
			tercer=1+num.nextInt(254);
			cuart=1+num.nextInt(254);
			dades[i]=primer+"."+segon+"."+tercer+"."+cuart;
		}
	}
	
	// Funcio per generar email
	public static void generarEmail(String [] dades, String [] noms, String domini, String extensioDomini, boolean semafor) {
		int index;
		domini = domini.toLowerCase();
		extensioDomini = extensioDomini.toLowerCase();
		if(semafor==true) {
			for(int i=0;i<dades.length;i++) {
				index = (int)(noms.length * Math.random());
				dades[i]=noms[index].toLowerCase()+"@"+domini+""+extensioDomini;
			}
		} else {
			for(int i=0;i<dades.length;i++) {
				dades[i]=noms[i].toLowerCase()+"@"+domini+""+extensioDomini;
			}
		}
	}
	
	// Funcio per generar numero
	public static void generarNumber(Random aleatori, String[] dades, int decimals, int max, int min) {
		int enter, maxd = 0, mind = 0;
		String number;
		
		// Es mira el maxim i el minim dels decimals
		if (decimals != 0) {
			maxd = (int)Math.pow(10, decimals)-1;
			mind = (int)Math.pow(10, decimals-1);
		}
		
		for (int i=0;i<dades.length;i++) {
			enter = aleatori.nextInt(max-min)+min;
			number = enter+"";
			// Si hi ha decimals es genera un numero a part.
			if (decimals != 0) {
				enter = aleatori.nextInt(maxd-mind)+mind;
				number = number+'.'+enter;
			}
			dades[i] = number;
		}
	}
	// Funcio per extreure les dades del CSV
	public static void extreureDades(String[][] dades) {
		try {
			boolean agafaCol=true;
			File fitxer=new File("dades.csv");
			FileReader fr = new FileReader(fitxer);
			BufferedReader br = new BufferedReader(fr);
			String separa[] = new String[10];
			int i =0,z=0;
			
			// Per cada linia la separa per punt i coma i guarda on li toca segons la id.
			while (br.ready()) {
				separa = br.readLine().split(";");
				dades[0][i] = separa[0];
				dades[1][i] = separa[1];
				dades[2][i] = separa[5];
				dades[3][i] = separa[2];
				dades[4][i] = separa[7];
				dades[5][i] = separa[4];
				dades[6][i] = separa[3];
				// Com colors hi ha menys es controla amb una paraula i despres emplena lo que falta amb repetits.
				if (agafaCol) {
					if (separa[9].equals("zzz")) {
						agafaCol = false;
					} else {
						dades[7][i] = separa[9];
					}
				} else {
					dades[7][i] = dades[7][z];
					z++;
				}
				dades[8][i] = separa[6];
				dades[9][i] = separa[8];
				i++;
			}
			br.close();
			fr.close();
		} catch (Exception pasanCosas) {
			pasanCosas.printStackTrace();
		}
	}

	// Funcio per generar booleans
	public static void generarBoolean(Random aleatori, String[] dades) {
		boolean bol;
		
		for (int i=0;i<dades.length;i++) {
			bol = aleatori.nextBoolean();
			dades[i] = bol+"";
		}
	}
	
	// Funcio per generar MACs aleatories
	public static void generarMAC(String[] dadesAleatories, Random aleatori) {
		// Per treure el numero hexadecimal es guarden els numeros en una array
		char[] numeros = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
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
	
	public static void generarJSON(String dadesAleatories[][],String camps[][], File sortida ) {
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
						escriuJSON.println("	"+'"'+camps[a][0]+'"'+":"+'"'+dadesAleatories[a][i]==null?dadesAleatories[a][i]:"null"+'"');
					}else {
						escriuJSON.println("	"+'"'+camps[a][0]+'"'+":"+'"'+dadesAleatories[a][i]==null?dadesAleatories[a][i]:"null"+'"'+",");
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
	
	public static void generarCSV(String [][] dadesAleatories,String [][] camps, File sortida) {
		try {
			PrintStream out = new PrintStream(sortida);
			for(int i=0; i<camps.length;i++) {
				if(i==(camps.length-1)) {
						out.print(""+camps[i][0]);
				} else out.print(""+camps[i][0]+";");
			}
			out.println();
			for(int i=0; i<dadesAleatories[0].length;i++) {
				for(int j=0; j<dadesAleatories.length;j++) {
					if(dadesAleatories[j][i] != null) out.print(""+dadesAleatories[j][i]);
					if (j==dadesAleatories.length-1) out.println();
					else out.print(";");
				}
			}
		}catch (Exception ex) {
			System.out.println(ex);
		}
	}
	
	// Funcio per validar el format de l'arxiu d'entrada
	public static boolean validarEntrada(File entrada, int[] linies) {
		File sortida;
		String nomCampstmp = "";
		String[] camps = null, nomCamps, errors = {"Arxiu de sortida no vàlid", "Número de registres no vàlid", "Format de sortida no vàlid", "Nombre de camps no vàlid en línia -> ", "Paràmetre no vàlid en línia -> ", "ID no vàlid en línia -> ", "Caràcter no vàlid en el nom dels camps."};
		int i, i2, id, longitud, minuscules, majuscules, lletres, simbols, numeros, maxim, minim, decimals = 0;
		boolean correcte = true;
		
		try {
			// Primer verifica els parametres de la primera linia, l'arxiu de sortida, si es numero el numero de registres i el format de sortida
			FileReader fr = new FileReader(entrada);
			BufferedReader br = new BufferedReader(fr);
			linies[0]++;
			String comprovacio = br.readLine();
			if (comprovacio == null) {
				System.out.println("No hi ha res en el fitxer");
				return false;
			} else {
				camps = comprovacio.split(" ");
			}
			if (camps.length == 3 || camps.length == 4 || camps.length == 5) {
				sortida = new File(camps[0]);
				if (!sortida.isDirectory()) {
					System.out.println(errors[0]);
					correcte = false;
				}
				if(camps.length == 5 && camps[2].equals("XML")) {
					String [] ultimcampXML = camps[3].split("/");
					if(ultimcampXML.length!=2) {
						System.out.println(errors[4]+linies[0]);
						correcte = false;
					}	
				}
		/*		if(camps.length == 4) {
					String [] ultimcampXML = camps[3].split("/");
					if(ultimcampXML.length!=2 || !camps[2].equals("XML")) {
						System.out.println(errors[4]+linies[0]);
						correcte = false;
					}	
				} */
				for (i=0;i<camps[1].length();i++) {
					if (!Character.isDigit(camps[1].charAt(i))) {
						System.out.println(errors[1]);
						correcte = false;
					}
				}
				if (camps[1].length() > 6) {
					System.out.println("Número de registres superior a 6 digits.");
					correcte = false;
				}
				if (!camps[2].equals("XML") && !camps[2].equals("SQL") && !camps[2].equals("JSON") && !camps[2].equals("CSV")) {
					System.out.println(errors[2]);
					correcte = false;
				}
			} else {
				System.out.println(errors[3]+linies[0]);
				correcte = false;
			}
			if (!br.ready()) {
				System.out.println("No hi ha camps que processar.");
				correcte = false;
			} 
			while(br.ready()) {
				// Despres verifica cada linia i mira si te correcte el numero de camps
				camps = br.readLine().split(" ");
				linies[0]++;
				if (camps.length >= 2) {
					for (i=0;i<camps[1].length();i++) {
						if (!Character.isDigit(camps[1].charAt(i))) {
							correcte = false;
						}
					}
					if (camps[0].length() > 30) {
						System.out.println("Nom camp superior a 30 en linia -> "+linies[0]);
						correcte = false;
					}
					id = Integer.parseInt(camps[1]);
					// Finalment verifica les id i per cada id mira si hi ha parametres, si hauria d'haber o no i si son correctes.
					if (id < 1 || id > 21) {
						System.out.println(errors[5]+linies[0]);
						correcte = false;
					}
					if ((id >= 1 && id <= 11) || id == 14 || id == 17 || id == 18 || (id >= 20 && id <= 22)) {
						if (camps.length > 3) {
							System.out.println(errors[3]+linies[0]);
							correcte = false;
						}
						if (camps.length == 3) {
							if (camps[2].charAt(0) != 'B') {
								System.out.println(errors[4]+linies[0]);
								correcte = false;
							}
						}
					} else {
						switch (id) {
						case 12:
							if (camps.length > 6) {
								System.out.println(errors[3]+linies[0]);
								correcte = false;
							} else {
								maxim = 1000;
								minim = 0;
								for (i=2;i<camps.length;i++) {
									if ((camps[i].charAt(0) != 'X' && camps[i].charAt(0) != 'D' && camps[i].charAt(0) != 'M' && camps[i].charAt(0) != 'B') || camps[i].length() > 7) {
										System.out.println(errors[4]+linies[0]);
										correcte = false;
									} else {
										for (i2=1;i2<camps[i].length();i2++) {
											if (!Character.isDigit(camps[i].charAt(i2))) {
												correcte = false;
											}
										}
									}
									if (camps[i].charAt(0) == 'X') {
										maxim = Integer.parseInt(camps[i].substring(1));
									} else if (camps[i].charAt(0) == 'M') {
										minim = Integer.parseInt(camps[i].substring(1));
									} else if (camps[i].charAt(0) == 'D') {
										decimals = Integer.parseInt(camps[i].substring(1));
									}
								}
								if (maxim < minim) {
									System.out.println(errors[4]+linies[0]);
									correcte = false;
								}
								if (decimals > 5) {
									System.out.println("Numero de decimals no valid en linia -> "+linies[0]);
									correcte = false;
								}
							}
							break;
						case 13:
							if (camps.length > 5) {
								System.out.println(errors[3]+linies[0]);
								correcte = false;
							} else {
								for (i=2;i<camps.length;i++) {
									if (camps[i].charAt(0) != 'X' && camps[i].charAt(0) != 'D' && camps[i].charAt(0) != 'B') {
										System.out.println(errors[4]+linies[0]);
										correcte = false;
									}
								}
							}
							break;
						case 15:
							if (camps.length > 9) {
								System.out.println(errors[3]+linies[0]);
								correcte = false;
							} else {
								for (i=2;i<camps.length;i++) {
									for (i2=0;i2<camps[i].length();i2++) {
										if (!Character.isDigit(camps[i].charAt(i2))) {
											System.out.println(errors[4]+linies[0]);
											correcte = false;
										}
									}
								}
								if (Integer.parseInt(camps[7]) > 50) {
									System.out.println("Llarg password incorrecte a linia -> "+linies[0]);
									correcte = false;
								}
								lletres = Integer.parseInt(camps[2]);
								numeros = Integer.parseInt(camps[3]);
								majuscules = Integer.parseInt(camps[4]);
								minuscules = Integer.parseInt(camps[5]);
								simbols = Integer.parseInt(camps[6]);
								longitud = Integer.parseInt(camps[7]);
								if (longitud < 3) {
									System.out.println("Longitud password inferior a la minima a linia -> "+linies[0]);
									correcte = false;
								}
								if (longitud < simbols+lletres+numeros && lletres < majuscules+minuscules) {
									System.out.println(errors[4]+linies[0]);
									correcte = false;
								}
							}
							break;
						case 16:
							maxim = 2023;
							minim = 1900;
							if (camps.length > 4) {
								System.out.println(errors[3]+linies[0]);
								correcte = false;
							} else {
								for (i=2;i<camps.length;i++) {
									if ((camps[i].charAt(0) != 'X' && camps[i].charAt(0) != 'M' && camps[i].charAt(0) != 'B') || camps[i].length() > 5) {
										System.out.println(errors[4]+linies[0]);
										correcte = false;
									} else {
										for (i2=1;i2<camps[i].length();i2++) {
											if (!Character.isDigit(camps[i].charAt(i2))) {
												System.out.println(errors[4]+linies[0]);
												correcte = false;
											}
										}
									}
									if (camps[i].charAt(0) == 'X') {
										maxim = Integer.parseInt(camps[i].substring(1));
										if (maxim > 3000) {
											System.out.println("Any maxim major a 3000 a linia -> "+linies[0]);
											correcte = false;
										}
									} else if (camps[i].charAt(0) == 'M') {
										minim = Integer.parseInt(camps[i].substring(1));
									}
								}
								
							}
							if (maxim < minim) {
								System.out.println(errors[4]+linies[0]);
								correcte = false;
							}
							break;
						case 19:
							if (camps.length > 4) {
								System.out.println(errors[3]+linies[0]);
								correcte = false;
							} else {
								if (camps.length == 3) {
									if (camps[2].length() > 6) {
										System.out.println(errors[4]+linies[0]);
										correcte = false;
									} else {
										for (i=0;i<camps[2].length();i++) {
											if (!Character.isDigit(camps[2].charAt(i))) {
												System.out.println(errors[4]+linies[0]);
												correcte = false;
											}
										}
									}
								}
							}
							break;
						}
					}
				} else {
					System.out.println(errors[3]+linies[0]);
					correcte = false;
				}
				nomCampstmp += " "+camps[0];
			}
			br.close();
			fr.close();
		} catch (Exception error) {
			error.printStackTrace();
		}
		// Comprova si els noms dels camps son unics o si hi ha caracters que poden donar problemes.
		nomCamps = nomCampstmp.split(" ");
		if (nomCamps.length == linies[0]) {
			for (i = 0;i < nomCamps.length;i++) {
				for (i2 = i+1;i2 < nomCamps.length;i2++) {
					if (nomCamps[i].equals(nomCamps[i2])) {
						System.out.println("Hi ha dos camps amb noms iguals, han de ser únics.");
						correcte = false;
					}
				}
			}
			for (i = 0;i < nomCamps.length;i++) {
				for (i2 = 0;i2<nomCamps[i].length();i2++) {
					if (!Character.isAlphabetic(nomCamps[i].charAt(i2)) && nomCamps[i].charAt(i2) != '_' && !Character.isDigit(nomCamps[i].charAt(i2))) {
						System.out.println(errors[6]);
						correcte = false;
					}
				}
			}
		} else {
			System.out.println(errors[6]);
			correcte = false;
		}
		return correcte;
	}
	
	//funció per a mostrar una taula amb totes les dades aleatories a ver com es veuria
	public static void mostraPre(String dadesAleatories[][],String camps[][]) {
		DefaultTableModel model = new DefaultTableModel();
		for(int i=0;i<camps.length;i++) {
			model.addColumn(camps[i][0]);
		}
		String dades[]=new String[dadesAleatories.length] ;
		for(int a=0;a<dadesAleatories[0].length;a++) {
			for(int i=0;i<camps.length;i++) {
					dades[i]=dadesAleatories[i][a];
			}
			model.addRow(ficarFiles(dades));
		}
		JTable taula = new JTable(model);
		taula.setBounds(30,40,200,300);
		JScrollPane sp=new JScrollPane(taula);
		JFrame f = new JFrame();
		f.add(sp);
		f.setSize(3000,4000);
		f.setVisible(true);
	}
	
	//funció per a crear les files per a la taula 
	public static Object[] ficarFiles(String dades[]) {
		Object n[]=new Object[dades.length];
			for(int a = 0; a < dades.length; a++) {
				n[a]=dades[a];
			}
		return n;
	}
	
	// menú per a la previsualització
	public static int despresDePre(String dadesAleatories[][],String camps[][]) {
		String opcioS="";int opcio=-1;
		boolean opcioValida=false;
		Scanner teclat=new Scanner(System.in);
		System.out.println("Que vols fer ara?");
		System.out.println("0. Sortir");
		System.out.println("1. Generar amb aquestes dades");
		System.out.println("2. Canviar la ruta del fitxer d'entrada.");
		opcioS=teclat.next();
		if (opcioS.length() == 1 && Character.isDigit(opcioS.charAt(0))) {//validem la pocio triada
			opcio = Integer.parseInt(opcioS);
			if (opcio >= 1 || opcio <= 2 || opcio <= 3) {
				opcioValida = true;
			}
			if(opcio>2||opcio<0) {
				System.out.println("Opció no valida");
				despresDePre(dadesAleatories, camps);
			}
		}
		if(!opcioValida) {//si no es valida utilitzem recursivitat
			System.out.println("Opció no valida");
			despresDePre(dadesAleatories, camps);
		}else {
			return opcio;
		}
		return opcio;
	}
	
	public static void colorsRGB(String dades[]) { 
		for(int i=0; i<dades.length;i++) {
			String rgb="rgb=";
			int r=(int)(Math.random()*256+0);
			int g=(int)(Math.random()*256+0);
			int b=(int)(Math.random()*256+0);
			
			rgb = rgb + "("+r+","+g+","+b+")";
			dades[i]=rgb;
		}
		
	}
	
	public static void mostrarDades(Scanner teclat) {
		System.out.println("Voleu mostrar els tipus de dades(1) els tipus de sortides(2) o un exemple del fitxer d'entrada(3)");
		boolean numero=false;
		String opcio="";
		while(numero==false) {
			System.out.println("Voleu mostrar els tipus de dades(1) els tipus de sortides(2) o un exemple del fitxer d'entrada(3)");
			opcio=teclat.nextLine();
			if(opcio.equals("1")||opcio.equals("2")) {
				numero=true;
			}else {
				System.out.println("Opció incorrecta torna a provar");
			}
		}		
		
		int i=Integer.parseInt(opcio);
		switch(i) {
		case 1: 
			System.out.println("Tipus de dades:");
			System.out.println("1- Nom");
			System.out.println("2- Cognom");
			System.out.println("3- Ciutats");
			System.out.println("4- Adreces");
			System.out.println("5- Professions");
			System.out.println("6- País");
			System.out.println("7- Estudis");
			System.out.println("8- Colors");
			System.out.println("9- URL");
			System.out.println("10- Nom de la companyia");
			System.out.println("11- Boolean");
			System.out.println("12- Number");
			System.out.println("13- Emails ---> Nom domini, Extensió del domini");
			System.out.println("14- IP4");
			System.out.println("15- Password ---> Lletres,Números,Majúscules,Minúscules,Símbols i Longitud");
			System.out.println("16- Dates ---> Any mínim,Any màxim");
			System.out.println("17- IBAN");
			System.out.println("18- DNI");
			System.out.println("19- Autonumèric ---> Valor d'inici");
			System.out.println("20- MAC");
			System.out.println("21- Colors hexadecimals");
			break;
		case 2: 
			System.out.println("Tipus de sortides: ");
			System.out.println("SQL");
			System.out.println("XML");
			System.out.println("CSV");
			System.out.println("JSON");
			break;
		case 3: 
			System.out.println("Exemple del fitxer d'entrada:");
			System.out.println("C:\\Eclipse\\Projecte1(ruta) 200(número de registres) XML(sortida)");
			System.out.println("nom 1");
			System.out.println("cognom 2");
			System.out.println("emails 13 Dnom_domini Xextensió");
			System.out.println("password 15 3 2 1 2 3 10");
			System.out.println("dates M1900 X2023");
			System.out.println("autonumèric 19 10");
			break;
		}
	}
	
	// Funcio per mostrar el menu d'opcions
	public static void menu() {
		System.out.println("Què vols fer?");
		System.out.println("0. Sortir.");
		System.out.println("1. Introduir arxiu d'entrada per generar dades aleatòries.");
		System.out.println("2. Informacío programa.");
		System.out.println("3. Verificar arxiu entrada.");
		System.out.print("Introdueix l'opció pel número: ");
	}
}
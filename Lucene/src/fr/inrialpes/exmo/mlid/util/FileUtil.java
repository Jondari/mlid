package fr.inrialpes.exmo.mlid.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class FileUtil {

	/**
	 * Méthode qui permet de charger de le contenu d'un fichier dans une chaine
	 * de caractère.
	 * 
	 * @param filePath
	 *            chemin vers le fichier
	 * @return
	 */
	public static String getText(String filePath) {
		Scanner scanner;
		try {
			scanner = new Scanner(new File(filePath));
			String text = " ";

			// On boucle sur chaque champ detecté
			int i = 0;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();

				// System.out.println(line);
				// faites ici votre traitement
				if (i == 0) {
					text = text + line;
					i++;
				} else {
					text = text + " " + line;
				}
			}
			scanner.close();
			return text;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Méthode qui permet d'écrire le contenu d'une chaîne de charactère dans un
	 * fichier.
	 * 
	 * @param filePath
	 *            chemin vers le fichier sur lequel on souhaite écrire
	 * @param text
	 *            chaine de caractère qui sera écrite dans le fichier
	 */
	public static void writeText(String filePath, String text) {
		FileWriter writer = null;
		try {
			writer = new FileWriter(filePath, false);
			writer.write(text, 0, text.length());
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Méthode qui permet d'écrire le contenu d'une chaîne de charactère dans un
	 * fichier.
	 * 
	 * @param filePath
	 *            chemin vers le fichier sur lequel on souhaite écrire
	 * @param text
	 *            chaine de caractère qui sera écrite dans le fichier
	 * @param nonOverWrite
	 *            booléen valant vrai s'il l'on ne souhaite pas écraser le
	 *            contenu présent dans le fichier faux sinon
	 */
	public static void writeText(String filePath, String text,
			boolean nonOverWrite) {
		FileWriter writer = null;
		try {
			writer = new FileWriter(filePath, nonOverWrite);
			writer.write(text, 0, text.length());
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Méthode qui permet d'écrire le contenu d'une liste dans un fichier.
	 * 
	 * @param filePath
	 *            chemin vers le fichier sur lequel on souhaite écrire
	 * @param listText
	 *            liste des chaines de caractères qui sera écrite dans le
	 *            fichier
	 * @param token
	 *            booléen valant vrai s'il l'on souhaite afficher un mot par
	 *            ligne
	 */
	public static void writeText(String filePath, List<String> listText,
			boolean token) {

		FileWriter writer = null;
		if (token) {
			try {
				writer = new FileWriter(filePath, false);
				for (String text : listText) {
					writer.write(text, 0, text.length());
					writer.write("\r\n");
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} else {
			try {
				writer = new FileWriter(filePath, false);
				for (String text : listText) {
					writer.write(text, 0, text.length());
					writer.write(" ");
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * Méthode qui permet d'écrire le contenu d'une liste dans un fichier.
	 * 
	 * @param filePath
	 *            chemin vers le fichier sur lequel on souhaite écrire
	 * @param listText
	 *            liste des chaines de caractères qui sera écrite dans le
	 *            fichier
	 * @param token
	 *            booléen valant vrai s'il l'on souhaite afficher un mot par
	 *            ligne
	 * @param nonOverWrite
	 *            booléen valant vrai s'il l'on ne souhaite pas écraser le
	 *            contenu présent dans le fichier faux sinon
	 */
	public static void writeText(String filePath, List<String> listText,
			boolean token, boolean nonOverWrite) {

		FileWriter writer = null;
		if (token) {
			try {
				writer = new FileWriter(filePath, nonOverWrite);
				for (String text : listText) {
					writer.write(text, 0, text.length());
					writer.write("\r\n");
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} else {
			try {
				writer = new FileWriter(filePath, nonOverWrite);
				for (String text : listText) {
					writer.write(text, 0, text.length());
					writer.write(" ");
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * Méthode qui retourne la liste des noms des fichier txt contenu dans le
	 * répertoire entré en paramètre
	 * 
	 * @param pathDir
	 * @return
	 */
	public static List<String> getListNameFile(String pathDir) {
		String[] listeFichiers;
		List<String> listNameFile = new ArrayList<String>();
		File repertoire = new File(pathDir);
		// on récupère la liste des éléments présent dans le répertoire
		listeFichiers = repertoire.list();
		if (listeFichiers.length > 0) {
			for (int i = 0; i < listeFichiers.length; i++) {
				// si le fichier a l'extension .txt on l'ajoute à la liste
				if (listeFichiers[i].contains(".txt")) {
					System.out.println("le fichier est " + listeFichiers[i]);
					listNameFile.add(listeFichiers[i]);
				}
			}
			if (listNameFile.isEmpty()) {
				System.out
						.println("Le répertoire ne contient pas de fichier .txt");
				return null;
			} else
				return listNameFile;
		} else {
			System.out.println("Le répertoire est vide!");
			return null;
		}
	}

	/**
	 * Méthode qui de récupérer le contenu texte des fichiers présent dans un
	 * répertoire et de l'ajouter à liste
	 * 
	 * @param pathDirectory
	 * @return
	 */
	public static List<String> getListOfText(String pathDirectory) {
		List<String> listOfText = new ArrayList<String>();
		List<String> listFile = FileUtil.getListNameFile(pathDirectory);
		// si le dossier n'est pas vide on récupère chacun de ses fichiers
		// textes et on les ajoute
		// a la liste des listes
		if (listFile != null) {
			for (String textFile : listFile) {
				String tempText = FileUtil.getText(pathDirectory + "/"
						+ textFile);
				listOfText.add(tempText);
			}
		}
		// System.out.println(listOfText.toString());
		return listOfText;
	}

	/**
	 * Méthode qui permet d'écrire le contenu d'une Map dans un fichier.
	 * 
	 * @param filePath
	 *            chemin vers le fichier sur lequel on souhaite écrire
	 * @param listText
	 *            liste des chaines de caractères qui sera écrite dans le
	 *            fichier
	 * @param token
	 *            booléen valant vrai s'il l'on souhaite afficher un mot par
	 *            ligne
	 */
	public static void writeText(String filePath, Map<String, String> mapText,
			boolean token) {

		Map<String, String> mapTemp = MapUtil.sortByValue(mapText);
		FileWriter writer = null;
		if (token) {
			try {
				writer = new FileWriter(filePath, false);

				Iterator iterator = mapTemp.entrySet().iterator();
				while (iterator.hasNext()) {
					Map.Entry entry = (Map.Entry) iterator.next();
					String key = (String) entry.getKey();
					String value = (String) entry.getValue();

					String text = key + " = " + value;

					writer.write(text, 0, text.length());
					writer.write("\r\n");
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} else {
			try {
				writer = new FileWriter(filePath, false);
				Iterator iterator = mapTemp.entrySet().iterator();
				while (iterator.hasNext()) {
					Map.Entry entry = (Map.Entry) iterator.next();
					String key = (String) entry.getKey();
					String value = (String) entry.getValue();

					String text = key + " = " + value;
					writer.write(text, 0, text.length());
					writer.write(" ");
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

}

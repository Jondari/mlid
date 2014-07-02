package fr.inrialpes.exmo.mlid.util;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MapUtil {

	/**
	 * Méthode qui affiche le contenu d'une map
	 * 
	 * @param maptoShow
	 */
	public static <K, V> void showMap(Map<K, V> maptoShow) {
		Iterator iterator = maptoShow.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			K key = (K) entry.getKey();
			V value = (V) entry.getValue();

			System.out.println(key + " = " + value);
		}
	}

	/**
	 * Méthode retourne une Map triée en fonction des valeurs
	 * 
	 * @param map
	 * @return
	 */
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(
			Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(
				map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	/**
	 * Méthode retourne une Map triée (descendant) en fonction des valeurs
	 * 
	 * @param map
	 * @return
	 */
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueDesc(
			Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(
				map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	/*
	 * java 7 version
	 *//*
		 * public static <K, V extends Comparable<? super V>> Map<K, V>
		 * sortByValue( Map<K, V> map ) { List<Map.Entry<K, V>> list = new
		 * LinkedList<>( map.entrySet() ); Collections.sort( list, new
		 * Comparator<Map.Entry<K, V>>() {
		 * 
		 * @Override public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2
		 * ) { return (o1.getValue()).compareTo( o2.getValue() ); } } );
		 * 
		 * Map<K, V> result = new LinkedHashMap<>(); for (Map.Entry<K, V> entry
		 * : list) { result.put( entry.getKey(), entry.getValue() ); } return
		 * result; }
		 */

}

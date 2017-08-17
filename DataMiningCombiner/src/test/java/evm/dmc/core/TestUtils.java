package evm.dmc.core;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class TestUtils {
	public static String printMap(Map<? extends Object,? extends Object>  map){
		StringBuilder strbld = new StringBuilder();
		
		Iterator<?> iter = map.entrySet().iterator();
		while(iter.hasNext()){
			Entry<? extends Object, ? extends Object> entr = (Entry<? extends Object, ? extends Object>) iter.next();
			strbld.append(entr.getKey());
			strbld.append(" - ");
			strbld.append(entr.getValue());
			strbld.append(System.getProperty("line.separator"));
		}
		return strbld.toString();
		
	}
	
	public static String printMapCollection(Map<? extends Object, ? extends Collection<? extends Object>>  map){
		StringBuilder strbld = new StringBuilder();
		
		Iterator<?> iterMap = map.entrySet().iterator();
		while(iterMap.hasNext()){
			Entry<? extends Object, ? extends Collection<? extends Object>> entr = (Entry<? extends Object, ? extends Collection<? extends Object>>) iterMap.next();
			Iterator<?> iter = entr.getValue().iterator();
			while(iter.hasNext()){				
				strbld.append(entr.getKey());
				strbld.append(" - ");
				strbld.append(iter.next());
				strbld.append(System.getProperty("line.separator"));
			}
		}
		return strbld.toString();
		
	}

}

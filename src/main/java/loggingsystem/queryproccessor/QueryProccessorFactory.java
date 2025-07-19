package loggingsystem.queryproccessor;

import java.util.HashMap;
import java.util.Map;

public class QueryProccessorFactory {
    private static Map<String,QueryProccessor> queryProccessorMap = new HashMap<>();

    static {
        queryProccessorMap.put("select", new SelectQueryProccessor());
        queryProccessorMap.put("find", new FindQueryProccessor());
    }

    public QueryProccessor getQueryProccessor(String query) throws Exception{
        String[] queryElements = query.split(" ");
        if(!queryProccessorMap.containsKey(queryElements[0])){
            throw new RuntimeException("No Query Proccesor found");
        }
        return queryProccessorMap.get(queryElements[0]);
    }
}

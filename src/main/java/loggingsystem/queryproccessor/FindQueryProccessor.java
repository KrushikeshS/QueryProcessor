package loggingsystem.queryproccessor;

import loggingsystem.io.FileReader;
import loggingsystem.io.Reader;
import loggingsystem.pojo.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FindQueryProccessor implements QueryProccessor{

    private Reader reader = new FileReader();

    @Override
    public void process(String query,Object source) throws Exception{

        String searchTerm = "";
        try{
            String[] parts = query.split("'");
            if(parts.length > 1){
                searchTerm = parts[1];
            }else{
                throw new IllegalArgumentException("Find Query Format invalid. Expected: find 'something' from filepath");
            }
        }catch (Exception ex){
            System.err.println("Error parsing find query" + ex.getMessage());
        }

        Collection<Log> logs = reader.read(source);

        List<Log> matchingLogs = new ArrayList<>();
        for(Log log:logs){
            if(log.getData() != null && log.getData().toLowerCase().contains(searchTerm.toLowerCase())) {
                matchingLogs.add(log);
            }
        }

        if(matchingLogs.isEmpty()){
            System.out.println("No mathcing logs found");
        }else{
            System.out.println("\n---Found Logs ---");
            for(Log log:matchingLogs){
                System.out.println("Timestamp: " + log.getTimestamp() +
                        ", Thread: " + log.getThreadName() +
                        ", Severity: " + (log.getSeverity() != null ? log.getSeverity().getName() : "N/A") +
                        ", Data: " + log.getData());
            }
            System.out.println("---End of Found Logs (" + matchingLogs.size() + " entries) ---");
        }
    }
}


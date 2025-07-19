package loggingsystem.queryproccessor;

import loggingsystem.io.FileReader;
import loggingsystem.io.Reader;
import loggingsystem.pojo.Log;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class SelectQueryProccessor implements QueryProccessor{

    private Reader reader = new FileReader();

    @Override
    public void process(String query, Object source) throws Exception {
        List<String> queryElements = Arrays.asList(query.split(" "));

        //Format: select * from <filename> where data like 'text' and timestamp between 'YYYY-MM-DD' and 'YYYY-MM-DD' order by timestamp


        Timestamp startTime = null;
        Timestamp endTime = null;
        String dataLikeTerm = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);

        try{
            //parse data like
            int likeIndex = queryElements.indexOf("like");
            if(likeIndex != -1 && queryElements.size() > likeIndex+1){
                String potentialDataLikeTerm = queryElements.get(likeIndex+1);
                if(potentialDataLikeTerm.startsWith("'") && potentialDataLikeTerm.endsWith("'")){
                    dataLikeTerm = potentialDataLikeTerm.substring(1,potentialDataLikeTerm.length()-1);
                } else {
                    dataLikeTerm = potentialDataLikeTerm;
                }
            }

            //parse timestamp between
            int betweenIndex = queryElements.indexOf("between");
            if(betweenIndex != -1 && queryElements.size() > betweenIndex+3){
                String startDateStr= queryElements.get(betweenIndex+1);
                String endDateStr= queryElements.get(betweenIndex+3);

                Date startDate = dateFormat.parse(startDateStr);
                startTime = new Timestamp(startDate.getTime());

                Date endDate = dateFormat.parse(endDateStr);
                endTime = new Timestamp(endDate.getTime());

            }
        } catch (ParseException e) {
            System.err.println("Error parsing date in query: " + e.getMessage());
            throw new IllegalArgumentException("Invalid date format in query. Expected 'YYYY-MM-DD'. " + e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Error parsing select query: Missing elements around 'like' or 'between'. " + e.getMessage());
            throw new IllegalArgumentException("Invalid 'select' query format. Check 'like' and 'between' clauses. " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred during select query parsing: " + e.getMessage());
            throw new IllegalArgumentException("Error parsing 'select' query: " + e.getMessage());
        }

        Collection<Log> allLogs = reader.read(source);
        List<Log> filteredLogs = filter(allLogs,dataLikeTerm,startTime,endTime);

        Collections.sort(filteredLogs,Comparator.comparing(Log::getTimestamp));

        System.out.println("\n--- Selected Logs ---");
        for(Log log : filteredLogs){
            System.out.println("Timestamp: " + log.getTimestamp() +
                    ", Thread: " + log.getThreadName() +
                    ", Severity: " + (log.getSeverity() != null ? log.getSeverity().getName() : "N/A") +
                    ", Data: " + log.getData());
        }
        System.out.println("--- End of Selected Logs (" + filteredLogs.size() + " entries) ---");

    }

    private List<Log> filter(Collection<Log> logs, String dataLikeTerm, Timestamp startTime, Timestamp endTime){

        List<Log> result = new ArrayList<>();

        for(Log log : logs){
            boolean matchesData = true;
            if(dataLikeTerm != null){
                if(log.getData() == null || !log.getData().toLowerCase().contains(dataLikeTerm.toLowerCase())){
                    matchesData = false;
                }
            }

            boolean matchesTime = true;
            if(startTime != null && endTime != null){
                if(log.getTimestamp() == null || log.getTimestamp().before(startTime) || log.getTimestamp().after(endTime)){
                    matchesTime = false;
                }
            }

            if(matchesTime && matchesData){
                result.add(log);
            }
        }
        return result;
    }


}

package loggingsystem.queryproccessor;

public interface QueryProccessor {
    void process(String query, Object source) throws Exception;
}

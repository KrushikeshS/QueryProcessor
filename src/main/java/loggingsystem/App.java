package loggingsystem;

import loggingsystem.queryproccessor.QueryProccessor;
import loggingsystem.queryproccessor.QueryProccessorFactory;
import loggingsystem.utils.FilePathCheck;

import java.util.Scanner;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        String asciiLogger =
                "\n" +
                        " _                       ___    _                   _     \n" +
                        "( )                     (  _`\\ ( )                 ( )    \n" +
                        "| |       _      __     | (_(_)| |__     _ _  _ __ | |/') \n" +
                        "| |  _  /'_`\\  /'_ `\\   `\\__ \\ |  _ `\\ /'_` )( '__)| , <  \n" +
                        "| |_( )( (_) )( (_) |   ( )_) || | | |( (_| || |   | |\\`\\ \n" +
                        "(____/'`\\___/'`\\__  |   `\\____)(_) (_)`\\__,_)(_)   (_) (_)\n" +
                        "              ( )_) |                                     \n" +
                        "               \\___/'                                     \n";

        System.out.println(asciiLogger);

        String filePath = "";

        Scanner scanner = new Scanner(System.in);
        boolean filePathSuccess = false;

        while(true){
            System.out.println("Press ? for help or enter the absolute file path");
            String input = scanner.nextLine();
            switch (input){
                case "?":
                    System.out.println("find and select range query is supported");
                    System.out.println("select * from test.log where data like 'db connected' and time between ('','') order by time");
                    System.out.println("find 'hello world' from test.log");
                    break;

                case "":
                    System.out.println("Press ? for help or enter absolute file path");
                    break;

                default:
                    filePath = input;
                    if(FilePathCheck.validFilePath(input)){
                        filePathSuccess = true;
                    }
                    break;
            }
            if (filePathSuccess){
//                scanner.nextLine();
                break;
            }
        }

        while(true){
            System.out.println("Query: ");
            String query = scanner.nextLine();

            try{
                QueryProccessorFactory queryProccessorFactory = new QueryProccessorFactory();
                QueryProccessor proccessor = queryProccessorFactory.getQueryProccessor(query);
                proccessor.process(query,filePath);
            }catch (Exception ex){
                System.out.println("Please use valid query, select and find");
            }
        }
    }
}

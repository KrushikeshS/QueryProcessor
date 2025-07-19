package loggingsystem.io;

import loggingsystem.pojo.Log;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FileReader implements Reader{

    @Override
    public Collection<Log> read(Object source){
        List<Log> logs = new ArrayList<>();
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try{
            String filePath = (String) source;

            File file = new File(filePath);
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);

            while(true){
                try{
                    Log log = (Log) ois.readObject();
                    logs.add(log);
                }catch (EOFException ex){
                    System.out.println("ex");
                    break;
                }
            }

        }catch (Exception ex){
            //
        }finally {
            try{
                fis.close();
                ois.close();
            }catch (Exception ex){
                System.out.println("Error closing the streams.");
            }
        }
        return logs;
    }
}

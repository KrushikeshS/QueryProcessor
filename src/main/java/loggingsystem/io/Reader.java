package loggingsystem.io;

import loggingsystem.pojo.Log;

import java.util.Collection;

public interface Reader {
    Collection<Log> read(Object source);
}

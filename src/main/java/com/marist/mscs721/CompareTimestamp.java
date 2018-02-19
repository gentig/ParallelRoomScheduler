package com.marist.mscs721;

import java.sql.Timestamp;

@FunctionalInterface
public interface CompareTimestamp{
    boolean compare(Timestamp start, Timestamp end);
}
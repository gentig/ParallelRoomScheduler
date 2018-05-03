/**
 * Yet Another Software License, 1.0
 *
 * Lots of text, specifying the users rights, and whatever ...
 */
package com.marist.mscs721;

import java.sql.Timestamp;

/**
 * CompareTimestamp
 *
 * This is the interface for using lambda notation comparing objects
 *
 */
@FunctionalInterface
public interface CompareTimestamp{
    boolean compare(Timestamp start, Timestamp end);
}
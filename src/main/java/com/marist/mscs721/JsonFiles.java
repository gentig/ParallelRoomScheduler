/**
 * Yet Another Software License, 1.0
 *
 * Lots of text, specifying the users rights, and whatever ...
 */
package com.marist.mscs721;

/**
 * Java nio (new io in java 8 makes it easier to walk directories and files)
 */
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * JsonFiles
 *
 * This class is used in conjunction with Files.walkFileTree. The functions in
 * this class will be called as callbacks depending on what we are visiting e.g. file, folder etc.
 * Right now we are only interested to find files (JSON files)
 */
public class JsonFiles extends SimpleFileVisitor<Path>{
    private static final Logger logger = Logger.getLogger(RoomScheduler.class);
    private static ArrayList<Path> jFiles = new ArrayList<>();

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs){
        logger.info("About to visit: " + dir);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs){
        if(attrs.isRegularFile()){
            logger.info("Found regular file ");
            jFiles.add(file);
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        logger.error(exc.getMessage());
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc){
        return FileVisitResult.CONTINUE;
    }

    /**
     * getPaths
     *
     * This function will return the list of Paths, each containing the Path to a
     * JSON file.
     *
     * @return jFiles the list of Paths
     **/
    public static List<Path> getPaths(){
        return jFiles;
    }
}

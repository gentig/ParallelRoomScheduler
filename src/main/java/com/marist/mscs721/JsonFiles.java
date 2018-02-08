package com.marist.mscs721;

/**
 * Java nio (new io in java 8 makes it easier to walk directories and files)
 */
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

/**
 * JsonFiles
 *
 * This class is used in conjuction with Files.walkFileTree. The functions in
 * this class will be called as callbacks depending on what we are visiting e.g. file, folder etc.
 * Right now we are only interested with the Json files*/
public class JsonFiles extends SimpleFileVisitor<Path>{
    private static ArrayList<Path> jFiles = new ArrayList<>();
    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        System.out.println("About to visit: " + dir);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if(attrs.isRegularFile()){
            System.out.println("Regular file ");
            jFiles.add(file);
        }
        //System.out.println(file);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        System.err.println(exc.getMessage());
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    public static ArrayList<Path> getPaths(){
        return jFiles;
    }
}

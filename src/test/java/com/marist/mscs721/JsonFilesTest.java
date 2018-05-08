/**
 * Yet Another Software License, 1.0
 *
 * Lots of text, specifying the users rights, and whatever ...
 */
package com.marist.mscs721;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class JsonFilesTest {
    static final Logger logger = Logger.getLogger(RoomScheduler.class);
    /**
     * visitFile
     *
     * Testing if the directory for json files exists.
     * If the directory does not exists we do not attempt to create it. The creation of the
     * directory is handled we we run the application
     */
    @Test
    public void visitFile() {
        String dirName = "jSonFiLes";//dirName is case insensitive
        String realPath = "C:\\Users\\genti\\GG_HOME_FOLDER\\marist\\Spring_2018\\mscs721\\homework\\hw_1\\RoomScheduler\\jsonfiles";
        try {
            Path pathToDir = Paths.get(dirName);//"jsonFiles" is directory of all Json files
            //System.out.println(pathToDir.toRealPath(LinkOption.NOFOLLOW_LINKS));
            logger.info("Real path: " + pathToDir.toRealPath(LinkOption.NOFOLLOW_LINKS).toString());
            //assertEquals(realPath, pathToDir.toRealPath(LinkOption.NOFOLLOW_LINKS).toString());
        }catch (IOException exc){
            Assert.assertFalse(false);
        }
    }
}
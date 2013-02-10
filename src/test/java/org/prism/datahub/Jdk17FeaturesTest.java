package org.prism.datahub;

import org.junit.Test;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static junit.framework.Assert.*;

/**
 * Demo JDK 1.7 features, based on slides from <a>http://www.slideshare.net/denizoguz/new-features-of-jdk-7</a>
 *
 * @author Serge Pruteanu
 */
public class Jdk17FeaturesTest {

    @Test
    public void test_string_switch() {
        final List<String> integers = new ArrayList<>(Arrays.asList(Integer.toString(1), Integer.toString(2), Integer.toString(3), Integer.toString(4)));
        while (integers.size() > 0) {
            final String stringValue = integers.get(0);
            final int integerValue = Integer.valueOf(stringValue);

            assertTrue(integers.contains(Integer.toString(integerValue)));
            switch (stringValue) {
                case "1":
                    assertTrue(integers.remove(stringValue));
                    break;
                case "2":
                    assertTrue(integers.remove(stringValue));
                    break;
                case "3":
                    assertTrue(integers.remove(stringValue));
                    break;
                case "4":
                    assertTrue(integers.remove(stringValue));
                    break;
            }
        }
        assertTrue(integers.isEmpty());
    }

    @Test
    public void test_underscore_numbers() {
        final int binaryLiteral = 100_000_000;
        final int intValue = 100000000;
        assertEquals(intValue, binaryLiteral);
    }

    @Test(expected = IllegalAccessException.class)
    public void test_multi_catch_final_rethrow() throws FileNotFoundException, IllegalAccessException {
        try {
            if (System.console() != null) {
                throw new FileNotFoundException();
            } else {
                throw new IllegalAccessException();
            }
        } catch (IllegalAccessException | FileNotFoundException ignore) {
            assertTrue(true);
            throw ignore;
        }
    }

    @Test
    public void test_try_with_closeable() throws IOException {
        final AtomicBoolean resourceClosed = new AtomicBoolean();
        assertFalse(resourceClosed.get());
        final Closeable closeable = new Closeable() {
            @Override
            public void close() throws IOException {
                resourceClosed.set(true);
            }
        };

        try (final Closeable testCloseable = closeable) {
            assertSame(closeable, testCloseable);
        }

        assertTrue(resourceClosed.get());
    }

    @Test
    public void test_path_api() throws Exception {
        final Path testClassesPath = Paths.get(getTestClassesDirectory());
        assertNotNull(testClassesPath);

        boolean foundChildren = false;
        for (final Path child : testClassesPath) {
            assertNotNull(child);
            foundChildren = true;
        }
        assertTrue(foundChildren);

        assertEquals("test-classes", testClassesPath.getFileName().toString());
        assertTrue(testClassesPath.getNameCount() > 1);
        assertTrue(testClassesPath.isAbsolute());

        assertTrue(Files.isDirectory(testClassesPath));

        final FileStore fileStore = testClassesPath.getFileSystem().getFileStores().iterator().next();
        assertTrue(fileStore.getTotalSpace() > 0);
        assertTrue(fileStore.getUnallocatedSpace() > 0);
        assertTrue(fileStore.getUsableSpace() > 0);

        final List<Path> files = new ArrayList<>();
        final SimpleFileVisitor<Path> fileVisitor = new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                files.add(file);
                return super.visitFile(file, attrs);
            }
        };
        Files.walkFileTree(testClassesPath, fileVisitor);

        assertTrue(files.size() > 0);

        final Map<String, String> env = new HashMap<>();
        env.put("create", "true");
        final String zipFileName = testClassesPath + "/classes.zip";
        URI zipUri = URI.create("jar:file:" + zipFileName);
        try (final FileSystem zipFileSystem = FileSystems.newFileSystem(zipUri, env)) {
            for (final Path file : files) {
                final BasicFileAttributes fileAttributes = Files.readAttributes(file, BasicFileAttributes.class);
                assertNotNull(fileAttributes);
                assertTrue(fileAttributes.size() > 0);
                assertTrue(fileAttributes.isRegularFile());
                assertTrue(fileAttributes.creationTime().toMillis() > 0);
                assertTrue(fileAttributes.lastAccessTime().toMillis() > 0);
                assertTrue(fileAttributes.lastModifiedTime().toMillis() > 0);
                assertTrue(fileAttributes.fileKey() != null);

                //            Files.probeContentType(file);
                final Path zippedFile = zipFileSystem.getPath("/" + // todo Serge: explain tricky moments here
                        testClassesPath.relativize(file).toString()
                );
                if (!Files.exists(zippedFile.getParent())) {
                    Files.createDirectories(zippedFile.getParent());
                }
                Files.copy(file, zippedFile, StandardCopyOption.COPY_ATTRIBUTES);
            }
        }

        final List<Path> filesCopy = new ArrayList<>(files);
        files.clear();
        try(final FileSystem zipFileSystem = FileSystems.newFileSystem(zipUri, env)) {
            final FileStore zipFileStore = zipFileSystem.getFileStores().iterator().next();
            assertTrue(zipFileStore.getTotalSpace() > 0);
            assertTrue(zipFileStore.getUnallocatedSpace() > 0);
            assertTrue(zipFileStore.getUsableSpace() > 0);
            Files.walkFileTree(zipFileSystem.getPath("/"), fileVisitor);

            assertEquals(filesCopy.size(), files.size());
        }

        final Path zippedPath = Paths.get(zipFileName);
        Files.delete(zippedPath);
        assertFalse(Files.exists(zippedPath));

        files.clear();

//        FileSystems.getDefault().getPathMatcher();
//        todo Serge: mention path matcher, based on good article from http://blog.eyallupu.com/2011/11/java-7-working-with-directories.html
        for (final Path file : Files.newDirectoryStream(testClassesPath, "**{*,class}")) {
            if (Files.isRegularFile(file)) {
                files.add(file);
            }
        }
    }

    private URI getTestClassesDirectory() throws URISyntaxException {
        return getClass().getProtectionDomain().getCodeSource().getLocation().toURI();
    }

}

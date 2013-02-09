package org.prism.datahub;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * todo: add description
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

}

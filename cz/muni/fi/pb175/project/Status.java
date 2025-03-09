package cz.muni.fi.pb175.project;

import java.util.Arrays;

/**
 * Enumeration class representing status of product.
 *
 * @author Martin Uhlik
 */
public enum Status {
    NOT_READY {
        public String toString() {
            return "Není připraven";
        }
    },
    GETTING_READY {
        public String toString() {
            return "Připravuje se";
        }
    },
    IS_READY {
        public String toString() {
            return "Je připraven";
        }
    },
    MACHINING {
        public String toString() {
            return "Obrábí se";
        }
    },
    IS_MACHINED {
        public String toString() {
            return "Obrobeno";
        }
    };

    /**
     * Returns {@link MaterialType} for given name in case insensitive manner
     *
     * @param name name of the operation
     * @return {@link MaterialType} instance
     */
    public static Status forName(String name) {
        return Arrays
                .stream(values())
                .filter(o -> o.toString().equalsIgnoreCase(name))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }
}

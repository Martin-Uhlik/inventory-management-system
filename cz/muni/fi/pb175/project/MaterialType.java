package cz.muni.fi.pb175.project;


import java.util.Arrays;

/**
 * Enumeration class representing material types.
 *
 * @author Martin Uhlik
 */
public enum MaterialType {
    POLYURETHANE {
        @Override
        public String toString() {
            return "Polyuretan";
        }
    },
    POLYSTYRENE {
        @Override
        public String toString() {
            return "Polystyren";
        }
    },
    MDF_WOOD {
        @Override
        public String toString() {
            return "MDF dřevo";
        }
    },
    PLASTIC {
        @Override
        public String toString() {
            return "Plast";
        }
    };

    /**
     * Returns {@link MaterialType} for given name in case insensitive manner
     *
     * @param name name of the operation
     * @return {@link MaterialType} instance
     */
    public static MaterialType forName(String name) {
        return Arrays
                .stream(values())
                .filter(o -> o.toString().equalsIgnoreCase(name))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }
}

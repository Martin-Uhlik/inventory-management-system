package cz.muni.fi.pb175.project;

import java.util.Arrays;

/**
 * Enum class representing machines in company.
 *
 * @author Martin Uhlik
 */
public enum Machines {
    KUKA {
        @Override
        public String toString() {
            return "Kuka";
        }
    };

    /**
     * Returns {@link MaterialType} for given name in case insensitive manner
     *
     * @param name name of the machine
     * @return {@link Machines} instance
     */
    public static Machines forName(String name) {
        return Arrays
                .stream(values())
                .filter(o -> o.toString().equalsIgnoreCase(name))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }
}

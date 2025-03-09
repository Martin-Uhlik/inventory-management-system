package cz.muni.fi.pb175.project;

/**
 * Enumeration class representing users that can be logged into the system.
 *
 * @author Martin Uhlik
 */
public enum Users {
    MANAGER {
        public String toString() {
            return "Manažer výroby";
        }
    },
    PROGRAMMER {
        public String toString() {
            return "CNC Programátor";
        }
    },
    OPERATOR {
        public String toString() {
            return "CNC Operátor";
        }
    };
}

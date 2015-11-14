package model;

/**
 * Created by SOBHY on 29/09/2015.
 */

public class Budget {
    // The fields associated to the person
    private final String Budget_code, Budget_name;

    Budget(String Budget_code, String Budget_name) {
        this.Budget_code = Budget_code; this.Budget_name = Budget_name;
    }
    // since mName and mPhone combined are surely unique,
    // we don't need to add another id field
    public int getId() {
        return Budget_code.hashCode();
    }

    public static enum Field {
        Budget_code, Budget_name
    }
    public String get(Field f) {
        switch (f) {
            case Budget_code: return Budget_code;
            case Budget_name: return Budget_name;
            default: return Budget_code;
        }
    }
}

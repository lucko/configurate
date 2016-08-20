/**
 * Configurate
 * Copyright (C) zml and Configurate contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ninja.leaping.configurate;

import java.text.DateFormat;
import java.text.ParseException;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.time.temporal.*;
import java.util.Date;

/**
 * Contains functions useful for performing configuration type conversions.
 * The naming scheme is as follows:
 * as* functions attempt to convert the data passed to the appropriate type
 * strictAs* functions will only return values if the input value is already of an appropriate type
 */
public class Types {
    private Types() {
        // Always nope
    }

    public static String asString(Object value) {
        return value == null ? null : value.toString();
    }

    public static String strictAsString(Object value) {
        return value instanceof String ? (String) value : null;

    }

    public static Float asFloat(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Float) {
            return (Float) value;
        } else if (value instanceof Integer) {
            return ((Number) value).floatValue();
        }

        try {
            return Float.parseFloat(value.toString());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public static Float strictAsFloat(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Float
                || value instanceof Integer) {
            return ((Number) value).floatValue();
        }
        return null;
    }

    public static Double asDouble(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Double) {
            return (Double) value;
        } else if (value instanceof Integer
                || value instanceof Long
                || value instanceof Float) {
            return ((Number) value).doubleValue();
        }

        try {
            return Double.parseDouble(value.toString());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public static Double strictAsDouble(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Double
                || value instanceof Float
                || value instanceof Integer
                || value instanceof Long) {
            return ((Number) value).doubleValue();
        }
        return null;

    }

    public static Integer asInt(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof Float
            || value instanceof Double) {
            double val = ((Number) value).doubleValue();
            if (val == Math.floor(val)) {
                return (int) val;
            }
        }

        try {
            return Integer.parseInt(value.toString());
        } catch (IllegalArgumentException ex) {
            return null;
        }

    }

    public static Integer strictAsInt(Object value) {
        if (value == null) {
            return null;
        }

        return value instanceof Integer ? (Integer) value : null;
    }

    public static Long asLong(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Long) {
            return (Long) value;
        } else if (value instanceof Integer) {
            return ((Number) value).longValue();
        }

        if (value instanceof Float
                || value instanceof Double) {
            double val = ((Number) value).doubleValue();
            if (val == Math.floor(val)) {
                return (long) val;
            }
        }

        try {
            return Long.parseLong(value.toString());
        } catch (IllegalArgumentException ex) {
            return null;
        }

    }

    public static Long strictAsLong(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Long) {
            return (Long) value;
        } else if (value instanceof Integer) {
            return ((Number) value).longValue();
        }
        return null;
    }

    /**
     * Tries to convert a value to a boolean.
     *
     * If value is a boolean, casts and returns
     * If value is a Number, returns true if value is not 0
     * If value.toString() returns true, t, yes, y, or 1, returns true
     * If value.toString() returns false, f, no, n, or 0, returns false
     * Otherwise returns null
     *
     * @param value The value to convert
     * @return Value converted following rules specified above:w
     */
    public static Boolean asBoolean(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof Number) {
            return !value.equals(0);
        }
        final String potential = value.toString();
        if (potential.equals("true")
                || potential.equals("t")
                || potential.equals("yes")
                || potential.equals("y")
                || potential.equals("1")) {
            return true;
        } else if (potential.equals("false")
                || potential.equals("f")
                || potential.equals("no")
                || potential.equals("n")
                || potential.equals("0")) {
            return false;
        }
        return null;
    }

    public static Boolean strictAsBoolean(Object value) {
        if (value == null) {
            return null;
        }

        return value instanceof Boolean ? (Boolean) value : null;
    }

    public static Date asDate(Object value) {
        if (value == null) return null;
        if (value instanceof Date) {
            return (Date) value;
        }
        if (value instanceof TemporalAccessor) {
            TemporalAccessor temp = (TemporalAccessor) value;
            if (temp.isSupported(ChronoField.INSTANT_SECONDS)) {
                long millis = temp.get(ChronoField.INSTANT_SECONDS) * 1000;
                if (temp.isSupported(ChronoField.MILLI_OF_SECOND)) {
                    millis += temp.get(ChronoField.MILLI_OF_SECOND);
                }
                return new Date(millis);
            }
        }
        if (value instanceof Number) {
            return new Date(((Number) value).longValue());
        }
        String dateString = value.toString();
        try {
            return DateFormat.getInstance().parse(dateString);
        } catch (ParseException ex) {
            return null;
        }
    }

    public static Date strictAsDate(Object value) {
        if (value == null) {
            return null;
        }

        return value instanceof Date ? (Date) value : null;
    }

    public static Instant asInstant(Object value) {
        if (value == null) return null;
        if (value instanceof Instant) {
            return (Instant) value;
        }
        if (value instanceof Date) {
            return Instant.ofEpochMilli(((Date) value).getTime());
        }
        if (value instanceof TemporalAccessor) {
            try {
                return Instant.from((TemporalAccessor) value);
            } catch (DateTimeException ex) {
                return null;
            }
        }
        String dateString = value.toString();
        try {
            return Instant.parse(dateString);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

    public static Instant strictAsInstant(Object value) {
        if (value == null) {
            return null;
        }

        return value instanceof Instant ? (Instant) value : null;
    }
}

package client.Parsers;


import common.Collection.*;
import common.utils.CommonConstants;
import common.Exceptions.InvalidDataException;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * Class with parsers which are required to read Worker objects
 */
public class WorkerParsers {
    /**
     * String parser
     */
    public static Parser<String> stringParser = s -> s;
    /**
     * Long parser
     */
    public static Parser<Long> longParser = s -> {
        try{
            return Long.parseLong(s);
        } catch (NumberFormatException e){
            throw new InvalidDataException("invalidDataExceptionLong");
        }
    };
    /**
     * Integer parser
     */
    public static Parser<Integer> integerParser = s -> {
        try{
            return Integer.parseInt(s);
        } catch (NumberFormatException e){
            throw new InvalidDataException("invalidDataExceptionInt");
        }
    };
    /**
     * Double parser
     */
    public static Parser<Double> doubleParser = s -> {
        try{
            return Double.parseDouble(s);
        } catch (NumberFormatException e){
            throw new InvalidDataException("invalidDataExceptionDouble");
        }
    };
    /**
     * LocalDateTime parser
     */
    public static Parser<LocalDateTime> localDateTimeParser = s -> {
        try{
            return LocalDateTime.parse(s, CommonConstants.formatter);
        } catch (DateTimeParseException e){
            throw new InvalidDataException("invalidDataExceptionDate");
        }
    };
    /**
     * Status parser
     */
    public static Parser<Status> statusParser = s -> {
        try{
            return Status.valueOf(s);
        } catch (IllegalArgumentException e){
            throw new InvalidDataException("invalidDataExceptionStatus");
        }
    };
    /**
     * Color parser
     */
    public static Parser<Color> eyeColorParser = s -> {
        try{
            return Color.valueOf(s);
        } catch (IllegalArgumentException e){
            throw new InvalidDataException("invalidDataExceptionColor");
        }
    };
    /**
     * Country parser
     */
    public static Parser<Country> nationalityParser = s -> {
        try{
            return Country.valueOf(s);
        } catch (IllegalArgumentException e){
            throw new InvalidDataException("invalidDataExceptionCountry");
        }
    };
}

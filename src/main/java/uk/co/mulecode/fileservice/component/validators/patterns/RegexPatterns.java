package uk.co.mulecode.fileservice.component.validators.patterns;

public final class RegexPatterns {

    public static final String CSV_LINE_PATTERN = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}\\|" +  // UUID
            "\\w+\\|" +  // Code (assuming it consists of word characters only)
            ".+?\\|" +   // Name (assuming it can contain any characters)
            ".+?\\|" +   // likes (assuming it can contain any characters)
            ".+?\\|" +   // transport (assuming it can contain any characters)
            "\\d+\\.?\\d*\\|" +  // avgSpeed (assuming it's a decimal number)
            "\\d+\\.?\\d*$";     // topSpeed (assuming it's a decimal number)
}

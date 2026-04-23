package enums;

public enum CitationFormat {
    PLAIN_TEXT,
    BIBTEX;

    @Override
    public String toString() {
        switch (this) {
            case PLAIN_TEXT: return "Plain Text";
            case BIBTEX: return "BibTeX";
            default: return name();
        }
    }
}

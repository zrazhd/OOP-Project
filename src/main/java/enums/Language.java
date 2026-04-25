package enums;

public enum Language {
    KZ,
    EN,
    RU;

    @Override
    public String toString() {
        switch (this) {
            case KZ: return "Kazakh";
            case EN: return "English";
            case RU: return "Russian";
            default: return name();
        }
    }
}

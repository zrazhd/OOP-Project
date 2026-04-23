package enums;

public enum School {
    ENERGY_AND_OIL_GAS,
    GEOLOGY,
    IT_AND_ENGINEERING,
    BUSINESS,
    INTERNATIONAL_SCHOOL_OF_ECONOMICS,
    MARITIME_ACADEMY,
    APPLIED_MATHEMATICS,
    CHEMICAL_ENGINEERING,
    SOCIAL_SCIENCES,
    MATERIALS_AND_GREEN_TECHNOLOGIES;

    @Override
    public String toString() {
        switch (this) {
            case ENERGY_AND_OIL_GAS: return "School of Energy and Oil & Gas Industry";
            case GEOLOGY: return "School of Geology";
            case IT_AND_ENGINEERING: return "School of Information Technology and Engineering";
            case BUSINESS: return "Business School";
            case INTERNATIONAL_SCHOOL_OF_ECONOMICS: return "International School of Economics";
            case MARITIME_ACADEMY: return "Kazakhstan Maritime Academy";
            case APPLIED_MATHEMATICS: return "School of Applied Mathematics";
            case CHEMICAL_ENGINEERING: return "School of Chemical Engineering";
            case SOCIAL_SCIENCES: return "School of Social Sciences";
            case MATERIALS_AND_GREEN_TECHNOLOGIES: return "School of Materials Science and Green Technologies";
            default: return name();
        }
    }
}

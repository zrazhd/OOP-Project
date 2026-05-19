package research;

import enums.CitationFormat;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Represents the research paper in the system.
 */
public class ResearchPaper implements Serializable, Comparable<ResearchPaper> {

    private String title;
    private List<String> authors;
    private String journal;
    private int pages;
    private LocalDate datePublished;
    private String doi;
    private int citations;

    /**
     * Constructor for ResearchPaper.
     * @param title parameter value.
     * @param authors parameter value.
     * @param journal parameter value.
     * @param pages parameter value.
     * @param datePublished parameter value.
     * @param doi parameter value.
     * @param citations parameter value.
     */
    public ResearchPaper(String title, List<String> authors, String journal, int pages, LocalDate datePublished, String doi, int citations) {
        this.title = title;
        this.authors = authors;
        this.journal = journal;
        this.pages = pages;
        this.datePublished = datePublished;
        this.doi = doi;
        this.citations = citations;
    }

    /**
     * Gets the citation.
     * @param format parameter value.
     * @return String
     */
    public String getCitation(CitationFormat format) {
        String authorStr = String.join(", ", authors);
        switch (format) {
            case PLAIN_TEXT:
                return authorStr + ". \"" + title + "\". " + journal + ", " + datePublished.getYear() + ", pp. " + pages + ". DOI: " + doi;
            case BIBTEX:
                String key = authors.get(0).split(" ")[0].toLowerCase() + datePublished.getYear();
                return "@article{" + key + ",\n" +
                       "  author = {" + authorStr + "},\n" +
                       "  title = {" + title + "},\n" +
                       "  journal = {" + journal + "},\n" +
                       "  year = {" + datePublished.getYear() + "},\n" +
                       "  pages = {" + pages + "},\n" +
                       "  doi = {" + doi + "}\n" +
                       "}";
            default:
                return toString();
        }
    }

    // Comparators
    /**
     * byDate.
     * @return Comparator&lt;ResearchPaper&gt;
     */
    public static Comparator<ResearchPaper> byDate() {
        return Comparator.comparing(ResearchPaper::getDatePublished);
    }

    /**
     * byCitations.
     * @return Comparator&lt;ResearchPaper&gt;
     */
    public static Comparator<ResearchPaper> byCitations() {
        return Comparator.comparingInt(ResearchPaper::getCitations).reversed();
    }

    /**
     * byPages.
     * @return Comparator&lt;ResearchPaper&gt;
     */
    public static Comparator<ResearchPaper> byPages() {
        return Comparator.comparingInt(ResearchPaper::getPages).reversed();
    }

    @Override
    public int compareTo(ResearchPaper other) {
        return this.datePublished.compareTo(other.datePublished);
    }

    // Getters and Setters
    /**
     * Gets the title.
     * @return String
     */
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    /**
     * Gets the authors.
     * @return List&lt;String&gt;
     */
    public List<String> getAuthors() { return authors; }
    public void setAuthors(List<String> authors) { this.authors = authors; }
    /**
     * Gets the journal.
     * @return String
     */
    public String getJournal() { return journal; }
    public void setJournal(String journal) { this.journal = journal; }
    /**
     * Gets the pages.
     * @return int
     */
    public int getPages() { return pages; }
    public void setPages(int pages) { this.pages = pages; }
    /**
     * Gets the date published.
     * @return LocalDate
     */
    public LocalDate getDatePublished() { return datePublished; }
    public void setDatePublished(LocalDate datePublished) { this.datePublished = datePublished; }
    /**
     * Gets the doi.
     * @return String
     */
    public String getDoi() { return doi; }
    public void setDoi(String doi) { this.doi = doi; }
    /**
     * Gets the citations.
     * @return int
     */
    public int getCitations() { return citations; }
    public void setCitations(int citations) { this.citations = citations; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResearchPaper that = (ResearchPaper) o;
        return Objects.equals(doi, that.doi);
    }

    @Override
    public int hashCode() {
        return Objects.hash(doi);
    }

    @Override
    public String toString() {
        return "\"" + title + "\" by " + String.join(", ", authors) + " (" + datePublished.getYear() + ") - Citations: " + citations;
    }
}

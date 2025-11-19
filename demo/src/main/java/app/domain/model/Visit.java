package app.domain.model;

import java.time.LocalDate;

public class Visit {

    private LocalDate date;
    private String reason;
    private String notes;

    public Visit(LocalDate date, String reason, String notes) {
        this.date = date;
        this.reason = reason;
        this.notes = notes;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getReason() {
        return reason;
    }

    public String getNotes() {
        return notes;
    }


}

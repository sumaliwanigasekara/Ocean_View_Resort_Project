package com.oceanview.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Guest {
    private Long guestId;
    private String guestName;
    private String guestPassport;
    private String guestEmail;
    private String guestContact;
    private String guestAddress;
    private String guestNIC;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public Guest() {}

    public Guest(Long guestId, String guestName, String guestPassport, String guestEmail, String guestContact, String guestAddress, String guestNIC) {
        this.guestId = guestId;
        this.guestName = guestName;
        this.guestPassport = guestPassport;
        this.guestEmail = guestEmail;
        this.guestContact = guestContact;
        this.guestAddress = guestAddress;
        this.guestNIC = guestNIC;
    }

    // Getters and Setters
    public Long getGuestId() { return guestId; }
    public void setGuestId(Long guestId) { this.guestId = guestId; }

    public String getGuestName() { return guestName; }
    public void setGuestName(String guestName) { this.guestName = guestName; }

    public String getGuestPassport() { return guestPassport; }
    public void setGuestPassport(String guestPassport) { this.guestPassport = guestPassport; }

    public String getGuestEmail() { return guestEmail; }
    public void setGuestEmail(String guestEmail) { this.guestEmail = guestEmail; }

    public String getGuestContact() { return guestContact; }
    public void setGuestContact(String guestContact) { this.guestContact = guestContact; }

    public String getGuestAddress() { return guestAddress; }
    public void setGuestAddress(String guestAddress) { this.guestAddress = guestAddress; }

    public String getGuestNIC() { return guestNIC; }
    public void setGuestNIC(String guestNIC) { this.guestNIC = guestNIC; }

    public LocalDateTime getCreated_at() { return created_at; }
    public void setCreated_at(LocalDateTime created_at) { this.created_at = created_at; }

    public LocalDateTime getUpdated_at() { return updated_at; }
    public void setUpdated_at(LocalDateTime updated_at) { this.updated_at = updated_at; }
}

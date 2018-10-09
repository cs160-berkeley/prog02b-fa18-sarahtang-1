package com.example.sarahtang.representv2;

public class Representative implements java.io.Serializable{
    private String representative_type;
    private String first_name;
    private String last_name;
    private String party;
    private String contact_page;
    private String website;
    private String phone;
    private String bioguide_id;

    public Representative(String representative_type, String first_name, String last_name,
                          String party, String contact_page, String website, String phone,
                          String bioguide_id) {
        this.representative_type = representative_type;
        this.first_name = first_name;
        this.last_name = last_name;
        this.party = party;
        this.contact_page = contact_page;
        this.website = website;
        this.phone = phone;
        this.bioguide_id = bioguide_id;
    }

    public String getRepresentative_type() {
        return representative_type;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getParty() {
        return party;
    }

    public String getContact_page() {
        return contact_page;
    }

    public String getWebsite() {
        return website;
    }

    public String getPhone() {
        return phone;
    }

    public String getBioguide_id() {
        return bioguide_id;
    }
}

package com.hubspot.integration.config;

public final class HubSpotConstants {

    private HubSpotConstants() {}

    public static final String HUBSPOT_AUTH_URL = "https://app.hubspot.com/oauth/authorize";
    public static final String HUBSPOT_API_TOKEN_URL = "https://api.hubapi.com/oauth/v1/token";
    public static final String HUBSPOT_CONTACT_URL = "https://api.hubapi.com/crm/v3/objects/contacts";

    public static final String HUBSPOT_SCOPE = "crm.objects.contacts.read%20crm.objects.contacts.write%20oauth";
    public static final long MAX_ALLOWED_TIMESTAMP = 300_000L;

    // Headers
    public static final String SIGNATURE_HEADER = "x-hubspot-signature-v3";
    public static final String TIMESTAMP_HEADER = "X-HubSpot-Request-Timestamp";
}

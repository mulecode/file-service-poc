package uk.co.mulecode.fileservice.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IPDataResponse {
    private String status;
    private String continent;
    private String continentCode;
    private String country;
    private String countryCode;
    private String region;
    private String regionName;
    private String city;
    private String district;
    private String zip;
    private double lat;
    private double lon;
    private String timezone;
    private int offset;
    private String currency;
    private String isp;
    private String org;
    private String as;
    private String asname;
    private String reverse;
    private boolean mobile;
    private boolean proxy;
    private boolean hosting;
    private String query;
}

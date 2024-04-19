package uk.co.mulecode.fileservice.repository.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@Entity
@Table(name = "HttpRequest")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class HttpRequestEntity implements Serializable {
    @Id
    @Column(name = "requestId")
    private UUID requestId;
    @NotNull
    @Column(name = "requestUri")
    private String requestUri;
    @NotNull
    @Column(name = "requestTimestamp")
    private long requestTimestamp;
    @NotNull
    @Column(name = "responseHttpCode")
    private int responseHttpCode;
    @NotNull
    @Column(name = "requestIpAddress")
    private String requestIpAddress;
    @NotNull
    @Column(name = "requestCountryCode")
    private String requestCountryCode;
    @NotNull
    @Column(name = "requestIpProvider")
    private String requestIpProvider;
    @NotNull
    @Column(name = "timeLapsed")
    private int timeLapsed;
}

package uk.co.mulecode.fileservice.component.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "application")
public class AppProperty {

    private String name;
    private FeaturesFlags featuresFlags;
    private FeaturesConfigs featuresConfigs;

    @Getter
    @Setter
    public static class FeaturesFlags {
        private boolean enableCsvFileValidation;
    }

    @Getter
    @Setter
    public static class FeaturesConfigs {
        private String ipVerificationPartnerUrl;
    }

}

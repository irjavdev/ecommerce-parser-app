package de.kevcodez.ecommerce.parser.impl;

import static org.assertj.core.api.Assertions.withinPercentage;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;

import de.kevcodez.ecommerce.parser.domain.image.ImageVariant;
import de.kevcodez.ecommerce.parser.domain.price.Discount;
import lombok.SneakyThrows;

class AbstractParserTest {

    private static final String RESOURCE_PREFIX = "/data";

    WebsiteSourceDownloader websiteSourceDownloader;

    @BeforeEach
    void mockWebsiteSourceDownloader() {
        websiteSourceDownloader = mock(WebsiteSourceDownloader.class);
    }

    @SneakyThrows
    String getResourceAsString(String resource) {
        try (BufferedReader buffer = new BufferedReader(
            new InputStreamReader(getClass().getResourceAsStream(RESOURCE_PREFIX + resource)))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }
    }

    void verifyImageVariant(ImageVariant variant, ImageVariant expected) {
        assertAll("Verifying Image Variant",
            () -> assertThat(variant.getUrl()).isEqualTo(expected.getUrl()),
            () -> assertThat(variant.getHeight()).isEqualTo(expected.getHeight()),
            () -> assertThat(variant.getWidth()).isEqualTo(expected.getWidth()));
    }

    void verifyDiscount(Discount discount, Discount expectedDiscount) {
        assertAll("Discount",
            () -> assertThat(discount.getOldPrice()).isCloseTo(expectedDiscount.getOldPrice(), withinPercentage(0.1D)),
            () -> assertThat(discount.getDiscount()).isCloseTo(expectedDiscount.getDiscount(), withinPercentage(0.1D)),
            () -> assertThat(discount.getPercentage()).isCloseTo(expectedDiscount.getPercentage(), withinPercentage(0.1D))
        );
    }

}

package de.kevcodez.ecommerce.parser;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.kevcodez.ecommerce.parser.domain.product.Product;
import de.kevcodez.ecommerce.parser.impl.AbstractProductParser;
import de.kevcodez.ecommerce.parser.impl.AlternateParser;
import de.kevcodez.ecommerce.parser.impl.AmazonParser;
import de.kevcodez.ecommerce.parser.impl.BonPrixParser;
import de.kevcodez.ecommerce.parser.impl.ConradParser;
import de.kevcodez.ecommerce.parser.impl.WebsiteSourceDownloader;

public enum ECommerceParser {

    INSTANCE;

    private final List<AbstractProductParser> parsers = new ArrayList<>();

    private final WebsiteSourceDownloader websiteSourceDownloader = new WebsiteSourceDownloader();

    ECommerceParser() {
        parsers.add(new AlternateParser(websiteSourceDownloader));
        parsers.add(new AmazonParser(websiteSourceDownloader));
        parsers.add(new BonPrixParser(websiteSourceDownloader));
        parsers.add(new ConradParser(websiteSourceDownloader));
    }

    public Product parseLink(String url) throws URISyntaxException {
        String domainName = getDomainName(url);

        Optional<AbstractProductParser> linkDataParser = parsers.stream()
            .filter(parser -> parser.matches(domainName))
            .findFirst();

        if (linkDataParser.isPresent()) {
            return linkDataParser.get().parse(url);
        }

        throw new IllegalArgumentException("No parser found for url " + url);
    }

    private String getDomainName(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String domain = uri.getHost();
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }

}

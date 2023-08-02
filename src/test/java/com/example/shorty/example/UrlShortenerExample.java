package com.example.shorty.example;

import com.example.shorty.restapi.UrlShortener;
import java.util.List;

public class UrlShortenerExample {
    public static List<UrlShortener> getUrlShortenerExamples() {
        final UrlShortener test1 = new UrlShortener();
        test1.setUrl("www.google.com");
        test1.setShortUrl("dummy1");
        test1.setAccountId("karlo");
        test1.setRedirectType(302);
        test1.setRedirects(1);

        final UrlShortener test2 = new UrlShortener();
        test2.setUrl("www.google.com");
        test2.setShortUrl("dummy2");
        test2.setAccountId("karlo");
        test2.setRedirectType(302);
        test2.setRedirects(2);

        final UrlShortener test3 = new UrlShortener();
        test3.setUrl("www.youtube.com");
        test3.setShortUrl("dummy3");
        test3.setAccountId("karlo");
        test3.setRedirectType(302);
        test3.setRedirects(4);

        final UrlShortener test4 = new UrlShortener();
        test4.setUrl("www.youtube.com");
        test4.setShortUrl("dummy4");
        test4.setAccountId("karlo");
        test4.setRedirectType(302);
        test4.setRedirects(5);

        final UrlShortener test5 = new UrlShortener();
        test5.setUrl("www.facebook.com");
        test5.setShortUrl("dummy5");
        test5.setAccountId("karlo");
        test5.setRedirectType(302);
        test5.setRedirects(7);

        final UrlShortener test6 = new UrlShortener();
        test6.setUrl("www.facebook.com");
        test6.setShortUrl("dummy6");
        test6.setAccountId("karlo");
        test6.setRedirectType(302);
        test6.setRedirects(20);

        return List.of(test1, test2, test3, test4, test5, test6);
    }

    public static List<UrlShortener> getUniqueURLsExamples() {
        final UrlShortener unique1 = new UrlShortener();
        unique1.setUrl("www.google.com");
        unique1.setAccountId("karlo");
        unique1.setRedirects(3);

        final UrlShortener unique2 = new UrlShortener();
        unique2.setUrl("www.youtube.com");
        unique2.setAccountId("karlo");
        unique2.setRedirects(9);

        final UrlShortener unique3 = new UrlShortener();
        unique3.setUrl("www.facebook.com");
        unique3.setAccountId("karlo");
        unique3.setRedirects(27);

        return List.of(unique1, unique2, unique3);
    }
}

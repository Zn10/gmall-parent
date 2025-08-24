package com.zn.gmall.product;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JsoupTest {

    @Test
    void testSearch() throws IOException {
        Document doc = Jsoup.connect("https://list.suning.com/0-20006-0-2-0-0-0-0-0-0-7459212.html?safp=d488778a.homepagev8.126605238631.3&safc=cate.0.0&safpn=10001#search-path").get();

        Elements elements = doc.getElementsByClass("product-box ");

        for (Element element : elements) {
            String href = element.getElementsByClass("img-block")
                    .get(0).child(0).attr("href");
            String image = getImage(href);
            assertNotNull(image);
            System.out.println(image);
        }
    }

    @Test
    void testGetImage() throws IOException {
        String testHref = "//product.suning.com/0000000000/12345678901.html";
        String image = getImage(testHref);

        // 这里可以根据实际情况调整断言
        assertTrue(StringUtils.isEmpty(image) || image.startsWith("http"));
    }

    private String getImage(String href) throws IOException {
        String image = "";

        try {
            Document doc = Jsoup.connect("https:" + href).get();

            String attr = doc.getElementById("labelPicture")
                    .parent()
                    .getElementsByTag("a")
                    .get(0)
                    .child(0).attr("src");
            if (!StringUtils.isEmpty(attr)) {
                image = attr;
            }
        } catch (Exception e) {
            // 可以记录日志或进行其他处理
        }
        return image;
    }
}
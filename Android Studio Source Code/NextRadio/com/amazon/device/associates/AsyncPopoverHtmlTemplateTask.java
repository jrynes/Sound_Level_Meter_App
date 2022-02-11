package com.amazon.device.associates;

import android.util.Xml;
import com.rabbitmq.client.AMQP;
import java.io.IOException;
import java.io.StringReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* renamed from: com.amazon.device.associates.i */
public class AsyncPopoverHtmlTemplateTask extends ap<bu> {
    protected /* bridge */ /* synthetic */ bl m965b() {
        return m963a();
    }

    public /* bridge */ /* synthetic */ String m966c() {
        return super.m771c();
    }

    protected /* bridge */ /* synthetic */ Object doInBackground(Object[] objArr) {
        return m964a((ap[]) objArr);
    }

    public AsyncPopoverHtmlTemplateTask() {
        super("PO-h.db", 604800000);
    }

    protected bu m963a() {
        String str = "<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n<style type=\"text/css\">\n* {\n\tmargin: 0;\n\tpadding: 0;\n\tposition: relative;\n\tfont-size: 100%;\n\t/*border: 1px solid #CCC;*/\n}\nbody {\n\tfont-family: arial;\n}\n#shopAtAmazon {\n\tdisplay: block;\n    float: right;\n    border-radius: 5px;\n\tcolor: #FFF;\n\tbackground: #2F3941;\n\ttext-decoration: none;\n\tfont-size: 12px;\n\tpadding: 1px 5px;\n}\n#shopAtAmazon:before {\n\tborder: 1px solid #000000;\n    border-radius: 5px;\n\tcontent: \"\";\n\tposition: absolute;\n\ttop: -2px;\n\tleft: -2px;\n\theight: 18px;\n\twidth: 68px;\n}\n@media (max-width: 270px) {\n\t#prodTitle {\n\t\ttext-overflow: ellipsis; \n\t\twhite-space: nowrap; \n\t\toverflow: hidden;\n\t}\n}\n@media (max-width: 210px) {\n\t#reviewCount, #starRatings {\n\t\tdisplay: none !important;\n\t}\n}\n@media (max-width: 190px) {\n\t#shopAtAmazon, #shopAtAmazon:before {\n\t\tdisplay: none !important;\n\t}\n}\n</style>\n</head>\n<body style=\"height: $HEIGHTpx; width: $WIDTHpx;\">\n\t<div style=\"display: block; height: 100%; width: 30%; text-align: center; vertical-align: middle; float: left; background: url('$ICON_SRC') center no-repeat;\">\n\t\t&nbsp;\n\t</div>\n\t<div style=\"display: block; width: 65%; height: 100%; float: right;\">\n\t\t<div style=\"display: block; height: 40%; width: 100%; overflow: hidden; text-overflow: ellipsis; white-space: pre-line;\">\n\t\t\t<h4 id=\"prodTitle\">$TITLE</h4>\n\t\t</div>\n\t\t<div style=\"display: block; height: 30%; width: 100%;\">\n\t\t\t<div style=\"margin-top:6%;\">\n\t\t\t\t<p style=\"float:left; display:block; color:#B22222; margin: 0 2px;\">$PRICE</p>\n\t\t\t\t<span id=\"reviewCount\" style=\"float:right; font-size:60%; margin-top:2%\">$REVIEW_COUNT</span>\n\t\t\t\t<span id=\"starRatings\" style=\"display:block; height:16px; width:66px; float:right; background:url('http://ps-us.amazon-adsystem.com/images/ps-spr-1.png') no-repeat -$RATING_DISPLACEMENTpx -73px;\"></span>\n\t\t\t</div>\n\t\t</div>\n\t\t<div style=\"position: absolute; bottom: 0; display: block; height: 30%; width: 100%;\">\n\t\t\t<div style=\"margin-top:8%;\">\n\t\t\t\t<span style=\"float:left; display: block; width: 54px; height: 20px; background: url('http://ps-us.amazon-adsystem.com/images/ps-spr-1.png') no-repeat;\"></span>\n\t\t\t\t<a href=\"$RETAIL_URL\" id=\"shopAtAmazon\">Shop Now</a>\n\t\t\t</div>\n\t\t</div>\n\t</div>\n</body>\n</html>";
        return new bu("<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n<style type=\"text/css\">\n* {\n\tmargin: 0;\n\tpadding: 0;\n\tposition: relative;\n\tfont-size: 100%;\n\t/*border: 1px solid #CCC;*/\n}\nbody {\n\tfont-family: arial;\n}\n#shopAtAmazon {\n\tdisplay: block;\n    float: right;\n    border-radius: 5px;\n\tcolor: #FFF;\n\tbackground: #2F3941;\n\ttext-decoration: none;\n\tfont-size: 12px;\n\tpadding: 1px 5px;\n}\n#shopAtAmazon:before {\n\tborder: 1px solid #000000;\n    border-radius: 5px;\n\tcontent: \"\";\n\tposition: absolute;\n\ttop: -2px;\n\tleft: -2px;\n\theight: 18px;\n\twidth: 68px;\n}\n@media (max-width: 270px) {\n\t#prodTitle {\n\t\ttext-overflow: ellipsis; \n\t\twhite-space: nowrap; \n\t\toverflow: hidden;\n\t}\n}\n@media (max-width: 210px) {\n\t#reviewCount, #starRatings {\n\t\tdisplay: none !important;\n\t}\n}\n@media (max-width: 190px) {\n\t#shopAtAmazon, #shopAtAmazon:before {\n\t\tdisplay: none !important;\n\t}\n}\n</style>\n</head>\n<body style=\"height: $HEIGHTpx; width: $WIDTHpx;\">\n\t<div style=\"display: block; height: 100%; width: 30%; text-align: center; vertical-align: middle; float: left; background: url('$ICON_SRC') center no-repeat;\">\n\t\t&nbsp;\n\t</div>\n\t<div style=\"display: block; width: 65%; height: 100%; float: right;\">\n\t\t<div style=\"display: block; height: 40%; width: 100%; overflow: hidden; text-overflow: ellipsis; white-space: pre-line;\">\n\t\t\t<h4 id=\"prodTitle\">$TITLE</h4>\n\t\t</div>\n\t\t<div style=\"display: block; height: 30%; width: 100%;\">\n\t\t\t<div style=\"margin-top:6%;\">\n\t\t\t\t<p style=\"float:left; display:block; color:#B22222; margin: 0 2px;\">$PRICE</p>\n\t\t\t\t<span id=\"reviewCount\" style=\"float:right; font-size:60%; margin-top:2%\">$REVIEW_COUNT</span>\n\t\t\t\t<span id=\"starRatings\" style=\"display:block; height:16px; width:66px; float:right; background:url('http://ps-us.amazon-adsystem.com/images/ps-spr-1.png') no-repeat -$RATING_DISPLACEMENTpx -73px;\"></span>\n\t\t\t</div>\n\t\t</div>\n\t\t<div style=\"position: absolute; bottom: 0; display: block; height: 30%; width: 100%;\">\n\t\t\t<div style=\"margin-top:8%;\">\n\t\t\t\t<span style=\"float:left; display: block; width: 54px; height: 20px; background: url('http://ps-us.amazon-adsystem.com/images/ps-spr-1.png') no-repeat;\"></span>\n\t\t\t\t<a href=\"$RETAIL_URL\" id=\"shopAtAmazon\">Shop Now</a>\n\t\t\t</div>\n\t\t</div>\n\t</div>\n</body>\n</html>");
    }

    protected bu m964a(ap<bu>... apVarArr) {
        ap apVar = apVarArr[0];
        try {
            au auVar = new au("http://assoc-msdk-us.amazon-adsystem.com/getPopoverTemplates");
            auVar.m797a("MarketplaceID", "ATVPDKIKX0DER");
            auVar.m796a(RequestMethod.GET);
            if (auVar.m798b() == AMQP.REPLY_SUCCESS) {
                apVar.f1186a = m962a(auVar.m795a());
                apVar.m770b(true);
                apVar.m768a(true);
            }
        } catch (Exception e) {
            Log.m1019c("PopoverHTMLTemplate", "Service call threw exception.", e);
        }
        return (bu) apVar.f1186a;
    }

    private bu m962a(String str) throws XmlPullParserException, IOException {
        String text;
        XmlPullParser newPullParser = Xml.newPullParser();
        newPullParser.setFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces", false);
        newPullParser.setInput(new StringReader(str));
        newPullParser.nextTag();
        newPullParser.require(2, null, "PopoverTemplatesOutput");
        while (newPullParser.next() != 1) {
            if (newPullParser.getEventType() == 2 && newPullParser.getName().equals("templateURL")) {
                newPullParser.next();
                text = newPullParser.getText();
                break;
            }
        }
        text = null;
        if (text != null) {
            return new bu(text);
        }
        return null;
    }
}

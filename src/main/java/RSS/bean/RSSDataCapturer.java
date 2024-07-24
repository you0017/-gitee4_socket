package RSS.bean;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 利用SAX解析RSS数据
 */
public class RSSDataCapturer extends DefaultHandler {
    private List<CommonInformationItem> list;
    private Logger log = Logger.getLogger(RSSDataCapturer.class.getName());
    private CommonInformationItem item;
    private String source;//RSS源名
    private int status;//存当前解析到的是什么标记
    private final int STATUS_TITLE = 1;
    private final int STATUS_LINK = 2;
    private final int STATUS_DESCRIPTION = 3;
    private final int STATUS_PUBDATE = 4;
    private final int STATUS_GUID = 5;
    private StringBuffer buffer;//存文本
    //private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);


    public RSSDataCapturer(String name) {
        this.source = name;//RSS源名
        buffer = new StringBuffer();
    }

    //解析source对应的RSS源得到item集合返回
    public List<CommonInformationItem> load(String source) {
        list = new ArrayList<>();
        try {
            //SAX解析流程代码

            //1.创建SAX解析器
            SAXParserFactory spf = SAXParserFactory.newInstance();
            //2.注册事件处理器
            SAXParser parser = spf.newSAXParser();
            //3.解析xml文件
            parser.parse(source, this);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
        }
        return list;
    }

    //解析到xml文件开头
    @Override
    public void startDocument() throws SAXException {
        log.debug("文档开头");
        super.startDocument();
    }

    //解析到文档的结束要做什么
    @Override
    public void endDocument() throws SAXException {
        log.debug("文档结尾");
        super.endDocument();
    }

    //解析到一个元素开头
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        log.debug("元素开头:"+uri+"\t"+localName+"\t"+qName+"\t"+attributes);
        //记录当前解析的是什么

        if (item != null && "title".equals(qName)) {
            status = STATUS_TITLE;
        } else if (item != null && "link".equals(qName)) {
            status = STATUS_LINK;
        } else if (item != null && "description".equals(qName)) {
            status = STATUS_DESCRIPTION;
        } else if (item != null && "pubDate".equals(qName)) {
            status = STATUS_PUBDATE;
        } else if (item != null && "guid".equals(qName)) {
            status = STATUS_GUID;
        } else if ("item".equals(qName)) {
            item = new CommonInformationItem();
            item.setSource(source);
        }
    }

    //解析到一个元素结束
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        log.debug("元素结尾:"+uri+"\t"+localName+"\t"+qName);
        if (item != null && "title".equals(qName)) {
            item.setTitle(buffer.toString());
            status = 0;
        } else if (item != null && "link".equals(qName)) {
            item.setLink(buffer.toString());
            status = 0;
        } else if (item != null && "description".equals(qName)) {
            item.addDescription(buffer.toString());
            status = 0;
        } else if (item != null && "pubDate".equals(qName)) {
            item.setTxtDate(buffer.toString());
            try {
                item.setDate(df.parse(buffer.toString().trim()));
            } catch (ParseException e) {
                log.error(e);
            }
            status = 0;
        } else if (item != null && "guid".equals(qName)) {
            item.setId(buffer.toString());
            status = 0;
        } else if ("item".equals(qName)) {
            if (item.getId() == null) {
                item.setId(String.valueOf(item.getDescription().hashCode()));
            }
            list.add(item);
            item = null;
        }
        buffer.delete(0, buffer.length());
    }

    //解析到一个文本节点  包括空白节点
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String txt = new String(ch, start, length);
        //log.debug("解析到了文字:"+txt);
        buffer.append(txt);
    }
}

package com.googlecode.easyec.spirit.web.webservice.factory.impl;

import com.googlecode.easyec.spirit.web.webservice.Envelope;
import com.googlecode.easyec.spirit.web.webservice.factory.EnvelopeFactory;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import static java.nio.charset.Charset.forName;

/**
 * JAXB2.0支持的信封数据转换工厂类
 *
 * @author JunJie
 */
public class JaxbContextEnvelopeFactory implements EnvelopeFactory {

    private static final Logger logger = LoggerFactory.getLogger(JaxbContextEnvelopeFactory.class);

    private Charset charset = forName("UTF-8");
    private Unmarshaller unmarshaller;
    private Marshaller   marshaller;

    public JaxbContextEnvelopeFactory(Class[] classes) throws JAXBException {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(classes);
            unmarshaller = jaxbContext.createUnmarshaller();
            marshaller = jaxbContext.createMarshaller();
        } catch (JAXBException e) {
            logger.error(e.getMessage(), e);

            throw e;
        }
    }

    /**
     * 设置字符集
     *
     * @param charset 字符集
     */
    public void setCharset(String charset) {
        this.charset = forName(charset);
    }

    public String asXml(Envelope envelope) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            marshaller.marshal(envelope, bos);
            return new String(bos.toByteArray(), charset);
        } catch (JAXBException e) {
            logger.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(bos);
        }

        return null;
    }

    public Envelope asEnvelope(String xml) {
        InputStream bis = IOUtils.toInputStream(xml, charset);

        try {
            Object o = unmarshaller.unmarshal(bis);
            if (null != o) {
                Assert.isInstanceOf(Envelope.class, o);

                return (Envelope) o;
            }

            logger.trace("Unmarshal object is null.");
        } catch (JAXBException e) {
            logger.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(bis);
        }

        return null;
    }
}

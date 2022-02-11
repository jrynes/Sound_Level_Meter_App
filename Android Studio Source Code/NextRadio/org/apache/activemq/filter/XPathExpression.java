package org.apache.activemq.filter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import javax.jms.JMSException;
import org.apache.activemq.command.Message;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.util.JMSExceptionSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class XPathExpression implements BooleanExpression {
    private static final String DEFAULT_EVALUATOR_CLASS_NAME = "org.apache.activemq.filter.XalanXPathEvaluator";
    private static final Constructor EVALUATOR_CONSTRUCTOR;
    private static final String EVALUATOR_SYSTEM_PROPERTY = "org.apache.activemq.XPathEvaluatorClassName";
    private static final Logger LOG;
    private final XPathEvaluator evaluator;
    private final String xpath;

    public interface XPathEvaluator {
        boolean evaluate(Message message) throws JMSException;
    }

    static {
        LOG = LoggerFactory.getLogger(XPathExpression.class);
        String cn = System.getProperty(EVALUATOR_SYSTEM_PROPERTY, DEFAULT_EVALUATOR_CLASS_NAME);
        Constructor m = null;
        try {
            m = getXPathEvaluatorConstructor(cn);
        } catch (Throwable th) {
            LOG.error("Default XPath evaluator could not be loaded", e);
        }
        EVALUATOR_CONSTRUCTOR = m;
    }

    XPathExpression(String xpath) {
        this.xpath = xpath;
        this.evaluator = createEvaluator(xpath);
    }

    private static Constructor getXPathEvaluatorConstructor(String cn) throws ClassNotFoundException, SecurityException, NoSuchMethodException {
        Class c = XPathExpression.class.getClassLoader().loadClass(cn);
        if (XPathEvaluator.class.isAssignableFrom(c)) {
            return c.getConstructor(new Class[]{String.class});
        }
        throw new ClassCastException(Stomp.EMPTY + c + " is not an instance of " + XPathEvaluator.class);
    }

    private XPathEvaluator createEvaluator(String xpath2) {
        try {
            return (XPathEvaluator) EVALUATOR_CONSTRUCTOR.newInstance(new Object[]{this.xpath});
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            }
            throw new RuntimeException("Invalid XPath Expression: " + this.xpath + " reason: " + e.getMessage(), e);
        } catch (Throwable e2) {
            RuntimeException runtimeException = new RuntimeException("Invalid XPath Expression: " + this.xpath + " reason: " + e2.getMessage(), e2);
        }
    }

    public Object evaluate(MessageEvaluationContext message) throws JMSException {
        try {
            if (message.isDropped()) {
                return null;
            }
            return this.evaluator.evaluate(message.getMessage()) ? Boolean.TRUE : Boolean.FALSE;
        } catch (Exception e) {
            throw JMSExceptionSupport.create(e);
        }
    }

    public String toString() {
        return "XPATH " + ConstantExpression.encodeString(this.xpath);
    }

    public boolean matches(MessageEvaluationContext message) throws JMSException {
        Boolean object = evaluate(message);
        return object != null && object == Boolean.TRUE;
    }
}

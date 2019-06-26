package nettyRPC.protocol;

import java.io.Serializable;

/**
 * 创建者:小䶮
 */
public class InvokeProtocol implements Serializable {
    private String className;
    private String methodName;
    private Class<?>[] arrs;
    private Object[] values;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getArrs() {
        return arrs;
    }

    public void setArrs(Class<?>[] arrs) {
        this.arrs = arrs;
    }

    public Object[] getValues() {
        return values;
    }

    public void setValues(Object[] values) {
        this.values = values;
    }
}

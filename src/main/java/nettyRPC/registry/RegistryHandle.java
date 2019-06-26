package nettyRPC.registry;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.EventExecutorGroup;
import nettyRPC.protocol.InvokeProtocol;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 创建者:小䶮
 */
public class RegistryHandle extends ChannelInboundHandlerAdapter{
    private List<String> className = new ArrayList<String>();
    private Map<String,Object> registerMap = new ConcurrentHashMap<String,Object>();
    public RegistryHandle() {
        scannerClass("nettyRPC.provider");
        doRegister("nettyRPC.provider");
    }

    @Override
    //有客户端链接的时候会回调
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Object result = new Object();
        InvokeProtocol request = (InvokeProtocol)msg;
        if(registerMap.containsKey(request.getClassName())){
            Object service = registerMap.get(request.getClassName());
            Method method = service.getClass().getMethod(request.getMethodName(),request.getArrs());
            result = method.invoke(service,request.getValues());
        }
        ctx.write(result);
        ctx.flush();
        ctx.close();
    }

    @Override
    //链接发生异常的时候会回调
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }


    private void scannerClass(String s) {
        URL url = this.getClass().getClassLoader().getResource(s.replaceAll("\\.","/"));
        File classPath = new File(url.getFile());
        for (File file : classPath.listFiles()){
            if(file.isDirectory()){
                scannerClass(s + "." + file.getName());
            }else{
                className.add(s+"."+file.getName().replace(".class",""));
            }
        }

    }

    private void doRegister(String s) {
        if(className.isEmpty()){
            return;
        }
        for (String className : className){
            try{
                Class<?> clazz = Class.forName(className);
                Class<?> i = clazz.getInterfaces()[0];
                String serviceName = i.getName();
                registerMap.put(serviceName,clazz.newInstance());
            }catch (Exception e){

            }
        }
    }

}

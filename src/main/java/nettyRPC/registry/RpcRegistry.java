package nettyRPC.registry;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import javax.swing.text.html.Option;

/**
 * 创建者:小䶮
 */
public class RpcRegistry {
    private int port;

    public RpcRegistry(int port) {
        this.port = port;
    }

    public void start(){
        //serverSocket /ServerSocketChannel
        ServerBootstrap server = new ServerBootstrap();
        //基于NIO实现
        //selector主线程
        //work线程
        EventLoopGroup bossGroup = new NioEventLoopGroup();//主线程
        EventLoopGroup workGroup = new NioEventLoopGroup();//子线程
        try {

            server.group(bossGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //在Netty中，吧所有的业务逻辑处理全部归总到一个队列中
                            //这个队列中包含了各种各样的处理逻辑，对这些处理在Netty中有一个封装
                            //封装成一个对象，无锁化串行任务队列
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            //解码
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
                            //编码
                            pipeline.addLast(new LengthFieldPrepender(4));
                            //实参处理,编解码完成对数据的解析
                            pipeline.addLast("encoder",new ObjectEncoder());
                            pipeline.addLast("decoder",new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                            //执行自己的逻辑
                            //1.注册给每个对象起一个名字，对外提供服务的名字
                            //2.服务的位置做一个登记
                            pipeline.addLast( new RegistryHandle());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true);
            ChannelFuture future =  server.bind(this.port).sync();
            System.out.println("GP RPC Registry start at port " + this.port);
            future.channel().closeFuture().sync();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new RpcRegistry(8080).start();
    }
}

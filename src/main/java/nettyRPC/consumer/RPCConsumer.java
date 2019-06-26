package nettyRPC.consumer;

import nettyRPC.api.IRPCHelloService;
import nettyRPC.api.IRPCService;

/**
 * 创建者:小䶮
 */
public class RPCConsumer {

    public static void main(String[] args) {
        IRPCService rpcService = (IRPCService)RpcProxy.create(IRPCService.class);
        System.out.println("8 + 2 = " + rpcService.add(8,2));

        IRPCHelloService rpcHelloService = (IRPCHelloService)RpcProxy.create(IRPCHelloService.class);
        System.out.println(rpcHelloService.hello("yanyan"));
    }
}

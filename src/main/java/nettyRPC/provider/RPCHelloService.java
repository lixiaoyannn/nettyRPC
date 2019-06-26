package nettyRPC.provider;

import nettyRPC.api.IRPCHelloService;

/**
 * 创建者:小䶮
 */
public class RPCHelloService implements IRPCHelloService {
    @Override
    public String hello(String name) {
        return "hello "+ name;
    }
}

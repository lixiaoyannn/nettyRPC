package nettyRPC.provider;

import nettyRPC.api.IRPCService;

/**
 * 创建者:小䶮
 */
public class RPCService implements IRPCService {
    @Override
    public int add(int a, int b) {
        return a + b;
    }

    @Override
    public int sub(int a, int b) {
        return a-b;
    }

    @Override
    public int multi(int a, int b) {
        return a*b;
    }

    @Override
    public int div(int a, int b) {
        return 0;
    }
}

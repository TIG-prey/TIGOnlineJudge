

public class MySecurityManager extends SecurityManager {
    @Override
    public void checkExec(String cmd) {
        throw new SecurityException("checkExec 权限异常：" + cmd);
    }

    @Override
    public void checkRead(String file) {
        // throw new SecurityException("checkRead 权限异常：" + file);
    }

    @Override
    public void checkWrite(String file) {
        // throw new SecurityException("checkWrite 权限异常：" + file);
    }

    @Override
    public void checkConnect(String host, int port) {
        // throw new SecurityException("checkConnect 权限异常：" + host + ":" + port);
    }

    @Override
    public void checkDelete(String file) {
        // super.checkDelete(file);
    }
}
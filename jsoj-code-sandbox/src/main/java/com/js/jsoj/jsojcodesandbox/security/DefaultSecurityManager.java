package com.js.jsoj.jsojcodesandbox.security;

import java.security.Permission;

/**
 * 默认安全管理器
 *
 * @author JianShang
 * @version 1.0.0
 * @time 2024-10-28 03:22:14
 */
public class DefaultSecurityManager extends SecurityManager {

    @Override
    public void checkPermission(Permission perm) {
        // super.checkPermission(perm);
        System.out.println("默认不做任何限制");
        System.out.println(perm);
    }
}

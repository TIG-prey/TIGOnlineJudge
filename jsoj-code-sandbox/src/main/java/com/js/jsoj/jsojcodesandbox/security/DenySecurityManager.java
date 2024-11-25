package com.js.jsoj.jsojcodesandbox.security;

import java.security.Permission;

/**
 * 禁用所有权限安全管理器
 *
 * @author JianShang
 * @version 1.0.0
 * @time 2024-10-28 04:36:55
 */
public class DenySecurityManager extends SecurityManager {
    // 检查所有的权限
    @Override
    public void checkPermission(Permission perm) {
        throw new SecurityException("权限异常：" + perm.toString());
    }
}

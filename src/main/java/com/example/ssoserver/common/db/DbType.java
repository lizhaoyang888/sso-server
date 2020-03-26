package com.example.ssoserver.common.db;

/**
 * @Author: lichaoyang
 * @Date: 2019-09-22 16:14
 */
public class DbType {

    private static final ThreadLocal<Boolean> DB_TYPE = ThreadLocal.withInitial(() -> {
        return Boolean.TRUE;
    });
    private static final ThreadLocal<Boolean> FORCE_MASTER = ThreadLocal.withInitial(() -> {
        return Boolean.FALSE;
    });

    public DbType() {
    }

    public static boolean isForceMaster() {
        return (Boolean)FORCE_MASTER.get();
    }

    public static void setDBType(Boolean dbType) {
        DB_TYPE.set(dbType);
    }

    public static boolean isMaster() {
        return (Boolean)DB_TYPE.get();
    }

    public static void forceMaster() {
        DB_TYPE.set(Boolean.TRUE);
        FORCE_MASTER.set(Boolean.TRUE);
    }

    public static void reset() {
        DB_TYPE.set(Boolean.TRUE);
        FORCE_MASTER.set(Boolean.FALSE);
    }

}

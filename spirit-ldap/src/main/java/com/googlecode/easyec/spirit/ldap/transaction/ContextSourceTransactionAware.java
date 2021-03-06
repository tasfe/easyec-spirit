package com.googlecode.easyec.spirit.ldap.transaction;

import com.googlecode.easyec.spirit.transaction.PlatformTransactionAware;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionStatus;

/**
 * 抽象的，<code>ContextSource</code>资源对象事务监视类。
 * 该类配合{@link PlatformTransactionManager}事务管理对象，
 * 为LDAP操作提供事务支持。并且使用此类，可以定义并支持嵌套事务
 * 的功能扩展。
 *
 * @author JunJie
 */
public abstract class ContextSourceTransactionAware implements PlatformTransactionAware {

    private static final long serialVersionUID = 8883678346883125672L;

    public void commit(TransactionStatus status) throws TransactionException {
        commit(((DefaultTransactionStatus) status));
    }

    public void rollback(TransactionStatus status) throws TransactionException {
        rollback(((DefaultTransactionStatus) status));
    }

    /**
     * 执行当前LDAP的事务提交操作
     *
     * @param status 默认的事务状态
     * @throws TransactionException
     */
    abstract protected void commit(DefaultTransactionStatus status) throws TransactionException;

    /**
     * 执行当前LDAP的事务回滚操作
     *
     * @param status 默认的事务状态
     * @throws TransactionException
     */
    abstract protected void rollback(DefaultTransactionStatus status) throws TransactionException;
}

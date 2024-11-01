package com.app.login.domain;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 * Authority
 * @author Megadotnet
 * @date 2018-05-10
 */
@Entity
@Getter
@Setter
@Table(name = "app_authority")
public class Authority implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 权限名称，不能为空，长度在 0 到 50 之间
     */
    @NotNull
    @Size(min = 0, max = 50)
    @Id
    @Column(length = 50)
    private String name;

    /**
     * 重写 equals 方法，比较两个 Authority 对象是否相等
     * @param o 待比较的对象
     * @return 如果两个对象相等，返回 true，否则返回 false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass()!= o.getClass()) {
            return false;
        }

        Authority authority = (Authority) o;

        return!(name!= null?!name.equals(authority.name) : authority.name!= null);
    }

    /**
     * 重写 hashCode 方法，返回权限名称的哈希码
     * @return 权限名称的哈希码
     */
    @Override
    public int hashCode() {
        return name!= null? name.hashCode() : 0;
    }

    /**
     * 重写 toString 方法，返回权限名称的字符串表示
     * @return 权限名称的字符串表示
     */
    @Override
    public String toString() {
        return "Authority{" + "name='" + name + '\'' + "}";
    }
}


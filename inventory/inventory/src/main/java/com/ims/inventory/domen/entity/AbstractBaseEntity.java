package com.ims.inventory.domen.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;

import java.util.UUID;

@MappedSuperclass
public class AbstractBaseEntity {

    @Getter
    @Id
    @Column(name = "id")
    private final String id;

    @Version
    @Column(name = "version")
    private int version;

    @Column(name = "is_active")
    private boolean isActive;

    public AbstractBaseEntity() {
        this.id = UUID.randomUUID().toString();
    }

    @Override
    public int hashCode() { return id.hashCode(); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof AbstractBaseEntity)) {
            return false;
        }
        AbstractBaseEntity baseObj = (AbstractBaseEntity) obj;
        return getId().equals(baseObj.getId());
    }
}

package br.com.vitor.cybercrimes.tracker.model;

import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class BaseEntity implements Serializable {

    private LocalDateTime creationDateTime;
    private UUID createdBy;
    private LocalDateTime lastUpdateDateTime;
    private UUID updatedBy;

    public BaseEntity() {
        LocalDateTime now = LocalDateTime.now();
        this.creationDateTime = now;
        this.createdBy = UUID.randomUUID();
        this.lastUpdateDateTime = now;
        this.updatedBy = UUID.randomUUID();
    }
}

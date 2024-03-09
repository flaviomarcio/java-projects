package com.app.business.model.ofservice;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "notify_target")
public class NotifyTarget {
    @Id
    private UUID id;

    @Column(nullable = false)
    private LocalDateTime dtCreate;

    @Column(nullable = false)
    private LocalDateTime dtChange;

    @ManyToOne
    @JoinColumn(nullable = false, name = "target_rev_id")
    private NotifyTargetRev targetRev;

    @ManyToOne
    @JoinColumn(nullable = false, name = "group_id")
    private NotifyGroup group;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(length = 200)
    private String description;

    @Column(nullable = false)
    private boolean enabled;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn
    private List<NotifyTargetItem> items;

    public List<NotifyTargetItem> getItems() {
        if (this.items == null)
            this.items = new ArrayList<>();
        return this.items;
    }
}

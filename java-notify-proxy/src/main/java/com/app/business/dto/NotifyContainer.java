package com.app.business.dto;

import com.app.business.model.ofservice.NotifyForwarder;
import com.app.business.model.ofservice.NotifyTarget;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotifyContainer {
    private NotifyTarget target;
    private List<NotifyForwarder> forwarders;

    public List<NotifyForwarder> getForwarders() {
        if (this.forwarders == null)
            this.forwarders = new ArrayList<>();
        return this.forwarders;
    }
}

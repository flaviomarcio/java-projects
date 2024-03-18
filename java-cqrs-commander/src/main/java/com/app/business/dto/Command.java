package com.app.business.dto;

import com.app.business.domain.Action;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Command {

    @Schema(title = "Command",
            example = "0000-0000-0000-0000-000000000000",
            description = "Command actions",
            requiredMode = Schema.RequiredMode.REQUIRED)
    public Action command;
    @Schema(title = "target",
            example = "0000-0000-0000-0000-000000000000",
            description = "Body of object target for command actions",
            requiredMode = Schema.RequiredMode.REQUIRED)
    public Target target;
    @Schema(title = "callBack",
            example = "0000-0000-0000-0000-000000000000",
            description = "Enable callback of message id",
            requiredMode = Schema.RequiredMode.REQUIRED)
    public boolean callBack;
    @Schema(title = "Command message id",
            example = "0000-0000-0000-0000-000000000000",
            description = "Message identofier",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID messageId;
}

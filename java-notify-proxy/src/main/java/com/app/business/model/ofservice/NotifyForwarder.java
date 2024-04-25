package com.app.business.model.ofservice;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "notify_forwarder")
public class NotifyForwarder {


    @Id
    private UUID id;

    @Column(nullable = false)
    private LocalDateTime dt;

    @ManyToOne
    @JoinColumn(nullable = false, name = "target_id")
    private NotifyTargetRev targetRev;

    @Column(nullable = false)
    private Dispatcher dispatcher;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false)
    private boolean enabled;

    public Dispatcher getDispatcher() {
        if (this.dispatcher == null)
            this.dispatcher = Dispatcher.Logging;
        return this.dispatcher;
    }

    @Getter
    public enum Dispatcher {
        Logging(0),
        eMail(1),
        Telegram(2),
        Slack(3),
        GoogleChat(4),
        Teams(5),
        SMSZenvia(6),
        SMSTallos(7),
        CallBack(8);
        private final int value;

        Dispatcher(int value) {
            this.value = value;
        }

        public static Dispatcher of(Object value) {
            if (value == null)
                return null;
            for (var e : values()) {
                if (e.name().equalsIgnoreCase(value.toString()))
                    return e;
                try {
                    var id = Integer.parseInt(value.toString());
                    if (e.getValue() == id)
                        return e;
                } catch (NumberFormatException ignored) {
                }
            }
            return null;
        }

        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        public static Dispatcher get(String value) {
            var e = of(value);
            if (e != null)
                return e;
            throw fail(value);
        }

        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        public static Dispatcher get(int value) {
            var e = of(value);
            if (e != null)
                return e;
            throw fail(value);
        }

        private static IllegalArgumentException fail(Object value) {
            var enums = new StringBuilder();
            for (var e : values())
                enums
                        .append(e.getValue())
                        .append("|").append(e.getValue())
                        .append(" ");
            return new IllegalArgumentException(String.format("%s is not valid %s, valid parameters: %s ", value, Dispatcher.class.getSimpleName(), enums));
        }
    }

}

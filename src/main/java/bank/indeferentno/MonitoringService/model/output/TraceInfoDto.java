package bank.indeferentno.MonitoringService.model.output;

import java.time.OffsetDateTime;

public record TraceInfoDto(
        String traceId,
        OffsetDateTime firstEventTime,
        OffsetDateTime lastEventTime,
        int spanCount,
        boolean hasErrors
) {
}

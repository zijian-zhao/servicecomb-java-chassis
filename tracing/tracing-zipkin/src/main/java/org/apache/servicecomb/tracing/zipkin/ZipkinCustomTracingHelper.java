package org.apache.servicecomb.tracing.zipkin;

import brave.Span;
import brave.Tracer;
import brave.Tracing;

import java.util.Map;

/**
 * @ClassName ZipkinCustomTracingHelper
 * @Description TODO
 * @Author zijian zhao
 * @Date 2020/3/10 17:15
 */
public class ZipkinCustomTracingHelper {

    static final String CALL_PATH = "call.path";

    private final Tracer tracer;

    ZipkinCustomTracingHelper(Tracing tracing) {
        this.tracer = tracing.tracer();
    }

    private Span createSpan(String spanName, String path) {
        Span currentSpan = tracer.currentSpan();
        if (currentSpan != null) {
            return tracer.newChild(currentSpan.context()).name(spanName).tag(CALL_PATH, path).start();
        }

        return tracer.newTrace().name(spanName).tag(CALL_PATH, path).start();
    }

    @SuppressWarnings({"unused", "try"})
    public void record(String spanName, String path, Map<String, Object> tagsMap)
    {
        Span span = createSpan(spanName, path);
        try (Tracer.SpanInScope spanInScope = tracer.withSpanInScope(span)) {
            if (tagsMap == null || tagsMap.isEmpty())
            {
                return;
            }
            for (Map.Entry<String, Object> entry: tagsMap.entrySet()) {
                span.tag(entry.getKey(), JsonUtils.toJsonString(entry.getValue()));
            }
        } catch (Throwable throwable) {
            span.tag("error", throwable.getClass().getSimpleName() + ": " + throwable.getMessage());
            throw throwable;
        } finally {
            span.finish();
        }
    }
}

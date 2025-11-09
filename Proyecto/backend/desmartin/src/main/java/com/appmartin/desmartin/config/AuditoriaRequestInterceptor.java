package com.appmartin.desmartin.config;

import com.appmartin.desmartin.model.LogAcceso;
import com.appmartin.desmartin.service.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;
import java.util.Optional;

@Component
public class AuditoriaRequestInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(AuditoriaRequestInterceptor.class);

    private static final String START_TIME_ATTR = AuditoriaRequestInterceptor.class.getName() + ".START_TIME";
    private static final String HANDLER_METHOD_ATTR = AuditoriaRequestInterceptor.class.getName() + ".HANDLER_METHOD";
    private static final int MAX_ERROR_DETAIL_LENGTH = 4000;

    private final LogService logService;

    public AuditoriaRequestInterceptor(LogService logService) {
        this.logService = logService;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {
        request.setAttribute(START_TIME_ATTR, System.currentTimeMillis());
        if (handler instanceof HandlerMethod handlerMethod) {
            request.setAttribute(HANDLER_METHOD_ATTR, handlerMethod);
        }
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,
                                @NonNull HttpServletResponse response,
                                @NonNull Object handler,
                                @Nullable Exception ex) {
        try {
            Object handlerAttr = request.getAttribute(HANDLER_METHOD_ATTR);
            if (!(handlerAttr instanceof HandlerMethod handlerMethod)) {
                return;
            }

            long duration = resolveDuration(request);
            LogAcceso logEntry = new LogAcceso();
            logEntry.setMetodoHttp(request.getMethod());
            logEntry.setEndpoint(buildEndpoint(request));
            logEntry.setIpOrigen(resolveClientIp(request));
            logEntry.setCodigoRespuesta(response.getStatus());
            logEntry.setDuracionMs(duration);
            logEntry.setAccion(resolveAccion(request.getMethod()));
            logEntry.setEntidadAfectada(resolveEntidad(handlerMethod));
            logEntry.setOrigen(resolveOrigen(handlerMethod));
            logEntry.setIdEntidadAfectada(resolveIdEntidad(request));
            logEntry.setIdUsuario(resolveIdUsuario(request));
            logEntry.setTipoUsuario(resolveTipoUsuario(request));
            logEntry.setDescripcion(buildDescripcion(request, response, duration, ex));

            if (ex != null) {
                logEntry.setNivel("ERROR");
                logEntry.setDetalleError(truncateStackTrace(ex));
            } else if (response.getStatus() >= 500) {
                logEntry.setNivel("ERROR");
            } else if (response.getStatus() >= 400) {
                logEntry.setNivel("WARN");
                logEntry.setDetalleError(resolveErrorMessage(request).orElse(null));
            } else {
                logEntry.setNivel("INFO");
            }

            logService.registrarEvento(logEntry);
        } catch (Exception loggingException) {
            log.warn("No se pudo registrar la auditoría centralizada", loggingException);
        }
    }

    private long resolveDuration(HttpServletRequest request) {
        Object start = request.getAttribute(START_TIME_ATTR);
        if (start instanceof Long startTime) {
            return System.currentTimeMillis() - startTime;
        }
        return 0L;
    }

    private String buildEndpoint(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String query = request.getQueryString();
        return query != null ? uri + "?" + query : uri;
    }

    private String resolveAccion(String method) {
        return switch (method.toUpperCase()) {
            case "POST" -> "CREATE";
            case "PUT", "PATCH" -> "UPDATE";
            case "DELETE" -> "DELETE";
            case "GET" -> "READ";
            default -> "ACTION";
        };
    }

    private String resolveEntidad(HandlerMethod handlerMethod) {
        String simpleName = handlerMethod.getBeanType().getSimpleName();
        if (simpleName.endsWith("Controller")) {
            return simpleName.substring(0, simpleName.length() - "Controller".length());
        }
        return simpleName;
    }

    private String resolveOrigen(HandlerMethod handlerMethod) {
        return handlerMethod.getBeanType().getName() + "#" + handlerMethod.getMethod().getName();
    }

    private Integer resolveIdEntidad(HttpServletRequest request) {
        Object attribute = request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if (attribute instanceof Map<?, ?> variables) {
            for (Map.Entry<?, ?> entry : variables.entrySet()) {
                if (entry.getKey() instanceof String key && entry.getValue() instanceof String value) {
                    String lowerKey = key.toLowerCase();
                    if (lowerKey.equals("id") || lowerKey.endsWith("id")) {
                        try {
                            return Integer.valueOf(value);
                        } catch (NumberFormatException ignored) {
                            // continuar probando con otras variables
                        }
                    }
                }
            }
        }
        return null;
    }

    private Integer resolveIdUsuario(HttpServletRequest request) {
        String header = Optional.ofNullable(request.getHeader("X-User-Id"))
            .orElse(request.getHeader("X-Usuario-Id"));
        if (header == null) {
            return null;
        }
        try {
            return Integer.valueOf(header);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private String resolveTipoUsuario(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("X-User-Type"))
            .orElse(request.getHeader("X-Tipo-Usuario"));
    }

    private String resolveClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private String buildDescripcion(HttpServletRequest request,
                                    HttpServletResponse response,
                                    long duration,
                                    Exception ex) {
        StringBuilder builder = new StringBuilder();
        builder.append(request.getMethod())
            .append(" ")
            .append(request.getRequestURI())
            .append(" -> ")
            .append(response.getStatus())
            .append(" en ")
            .append(duration)
            .append(" ms");

        if (ex != null) {
            builder.append(" | error: ").append(ex.getClass().getSimpleName()).append(" - ")
                .append(Optional.ofNullable(ex.getMessage()).orElse("sin mensaje"));
        } else if (response.getStatus() >= 400) {
            resolveErrorMessage(request).ifPresent(msg -> builder.append(" | detalle: ").append(msg));
        }
        return builder.toString();
    }

    private Optional<String> resolveErrorMessage(HttpServletRequest request) {
        return Optional.ofNullable(request.getAttribute("jakarta.servlet.error.message"))
            .map(Object::toString);
    }

    private String truncateStackTrace(Exception ex) {
        StringBuilder sb = new StringBuilder();
        sb.append(ex.getClass().getName()).append(": ")
            .append(Optional.ofNullable(ex.getMessage()).orElse("sin mensaje"));
        for (StackTraceElement element : ex.getStackTrace()) {
            sb.append(System.lineSeparator()).append("\tat ").append(element);
            if (sb.length() >= MAX_ERROR_DETAIL_LENGTH) {
                sb.append(System.lineSeparator()).append("...truncado...");
                break;
            }
        }
        return sb.toString();
    }
}


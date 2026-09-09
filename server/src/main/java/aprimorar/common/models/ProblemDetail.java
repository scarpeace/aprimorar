package aprimorar.common.models;

import java.net.URI;

/**
 * Draft of the RFC 9457 problem-details contract used by the API.
 *
 * This type is not wired into the exception handlers yet.
 */
public record ProblemDetail(
    URI type,
    String title,
    Integer status,
    String detail,
    URI instance
) {
}

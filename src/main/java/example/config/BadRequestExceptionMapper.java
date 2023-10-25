package example.config;

import org.springframework.stereotype.Component;

import com.yahoo.elide.ElideErrorResponse;
import com.yahoo.elide.core.exceptions.BadRequestException;
import com.yahoo.elide.core.exceptions.ErrorContext;
import com.yahoo.elide.core.exceptions.ExceptionMapper;

@Component
public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException, Object> {
	public ElideErrorResponse<? extends Object> toErrorResponse(BadRequestException exception,
			ErrorContext errorContext) {
//		if (exception.getCause() instanceof com.yahoo.elide.core.filter.dialect.ParseException parseException) {
//			return ElideErrorResponse.badRequest()
//					.errors(errors -> errors.error(error -> error.message(parseException.getMessage())));
//		}
		return errorContext.isVerbose() ? exception.getVerboseErrorResponse() : exception.getErrorResponse();
	}
}
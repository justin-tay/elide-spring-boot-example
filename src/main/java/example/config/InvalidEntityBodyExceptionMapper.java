package example.config;

import org.springframework.stereotype.Component;

import com.yahoo.elide.ElideErrorResponse;
import com.yahoo.elide.ElideErrors;
import com.yahoo.elide.core.exceptions.ErrorContext;
import com.yahoo.elide.core.exceptions.ExceptionMapper;
import com.yahoo.elide.core.exceptions.InvalidEntityBodyException;

@Component
public class InvalidEntityBodyExceptionMapper implements ExceptionMapper<InvalidEntityBodyException, ElideErrors> {
	public ElideErrorResponse<ElideErrors> toErrorResponse(InvalidEntityBodyException exception,
			ErrorContext errorContext) {
		return ElideErrorResponse.badRequest()
				.errors(errors -> errors.error(error -> error.message("Invalid entity body")));
	}
}
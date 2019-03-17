package evm.dmc.web.exceptions;

import org.springframework.dao.DataAccessException;

public class NotFoundException extends DataAccessException {
    /**
     *
     */
    private static final long serialVersionUID = -9042102628034582981L;

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}

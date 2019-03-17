package evm.dmc.webApi.exceptions;

import java.util.function.Supplier;

/**
 * Throwed when no Data Set model found
 */
public class MetaDataNotFoundException extends NotFoundException {

    /**
     * Defined to avoid problems with serialization
     */
    private static final long serialVersionUID = -7557190537228887032L;

    public static Supplier<ProjectNotFoundException> supplier(Long id) {
        return () -> new ProjectNotFoundException("Dataset with id=" + id + " not found");
    }

    public MetaDataNotFoundException(String message) {
        super(message);
    }

    public MetaDataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}

package evm.dmc.core.data;

/**
 * Indicates that implementation contains multiple attributes, such as
 * several columns in the dataset.
 * 
 * @author id23cat
 * 
 * @param <T>
 */
public interface HasMultiAttributes {
	/**
	 * Returns new Data instance contained single attribute selected by index
	 * from current multiAttribute dataset.
	 * 
	 * @param index
	 * @return
	 */
	Data<?> getAttribute(int index);

	/**
	 * Returns new Data instance contained single attribute selected by set of
	 * indexes
	 * from current multiAttribute dataset.
	 * 
	 * @param indexes
	 * @return
	 */
	Data<?> getAttributes(int... indexes);

}

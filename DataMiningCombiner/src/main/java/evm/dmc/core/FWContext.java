package evm.dmc.core;

import org.springframework.beans.factory.annotation.Qualifier;

@Qualifier
public @interface FWContext {
	String value();

}

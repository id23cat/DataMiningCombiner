package evm.dmc.web.controllers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CheckboxNamesBean {
    private String[] names = null;
//	private Set<String> names = null;

    public Set<String> getNamesSet() {
        return new HashSet<String>(Arrays.asList(names));
    }

}

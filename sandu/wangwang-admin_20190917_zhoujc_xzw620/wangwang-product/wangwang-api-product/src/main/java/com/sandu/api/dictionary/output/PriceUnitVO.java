package com.sandu.api.dictionary.output;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Sandu
 * @ClassName PriceUnitVO
 * @date 2019/5/17-10:46$
 */

@Data
public class PriceUnitVO extends DictionaryVO implements Serializable {
	private String suitableCodes;
}

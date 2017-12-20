package com.ctapweb.api.db.data_generators;

import com.ctapweb.api.db.pojos.Fs_Me;

/**
 * For generating fs_me entries for testing.
 * @author xiaobin
 *
 */
public class TestFs_Mes {

	public Fs_Me generateFs_Me(long fs_id, long measure_id) {
		return new Fs_Me(1, fs_id, measure_id);
	}
	
}

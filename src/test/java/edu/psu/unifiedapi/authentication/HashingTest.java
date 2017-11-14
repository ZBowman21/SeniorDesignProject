package edu.psu.unifiedapi.authentication;

import org.junit.Test;

import javax.xml.bind.DatatypeConverter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import static org.junit.Assert.assertArrayEquals;

/**
 * @author mthwate
 */
public class HashingTest {

	@Test
	public void hash() throws Exception {

		Map<String, String> tests = new HashMap<>();

		tests.put("Hello world!", "F6CDE2A0F819314CDDE55FC227D8D7DAE3D28CC556222A0A8AD66D91CCAD4AAD6094F517A2182360C9AACF6A3DC323162CB6FD8CDFFEDB0FE038F55E85FFB5B6");
		tests.put("", "CF83E1357EEFB8BDF1542850D66D8007D620E4050B5715DC83F4A921D36CE9CE47D0D13C5D85F2B0FF8318D2877EEC2F63B931BD47417A81A538327AF927DA3E");

		for (Entry<String, String> test : tests.entrySet()) {
			assertArrayEquals(Hashing.hash(test.getKey()), DatatypeConverter.parseHexBinary(test.getValue()));
		}

	}

}
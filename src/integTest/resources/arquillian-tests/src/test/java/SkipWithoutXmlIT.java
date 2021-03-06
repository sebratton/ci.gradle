/**
 * (C) Copyright IBM Corporation 2017, 2018.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Test;

import net.wasdev.wlp.common.arquillian.util.Constants;

/**
 * Only included in the skip-without-xml Maven profile.
 * 
 * @author ctianus.ibm.com
 *
 */
public class SkipWithoutXmlIT {

	@Test
	public void testSkipWithoutXML() throws Exception {
		// This test has the skip flag true with the XML file not existing.
		// In this case, the server should use the arquillian.xml that is
		// generated by the Maven configure-arquillian goal.
		File arquillianXML = new File("build/resources/test/arquillian.xml");
		InputStream is = new FileInputStream(arquillianXML);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line;
		boolean found = false;
		while ((line = br.readLine()) != null) {
			// Make sure a comment indicates that the file was generated
			// by the configure-arquillian goal.
			if(line.equals(Constants.CONFIGURE_ARQUILLIAN_COMMENT)) {
				found = true;
			}
		}
		br.close();
		assertTrue(found);
	}

}

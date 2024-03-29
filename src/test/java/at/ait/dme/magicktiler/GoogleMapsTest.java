package at.ait.dme.magicktiler;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import at.ait.dme.magicktiler.gmaps.GoogleMapsTiler;
import at.ait.dme.magicktiler.gmaps.GoogleMapsValidator;

/**
 * Google Maps tiling tests
 * 
 * @author Christian Sadilek <christian.sadilek@gmail.com>
 */
public class GoogleMapsTest extends BaseTest {
	
	private File workingDir = new File("test/gmap");
	
	@Before
	public void setUp() {
		deleteDir(workingDir);
	}
	
	@Test 
	public void testGoogleMapsTiling() throws TilingException, ValidationFailedException {
		// Generate a Google Maps tileset from the test image
		MagickTiler t = new GoogleMapsTiler();
		t.setWorkingDirectory(workingDir);
		t.setGeneratePreviewHTML(true);
		TilesetInfo info = t.convert(new File("src/test/resources/Hong_Kong_Night_Skyline.jpg"));
		
		// Check if image was resized correctly
		assertEquals("Wrong width calculated for the GMAP tileset!", 4096, info.getImageWidth());
		assertEquals("Wrong height calculated for the GMAP tileset!", 4096, info.getImageHeight());
		
		// Check if tileset files were generated correctly
		File tilesetRoot = new File(workingDir, "Hong_Kong_Night_Skyline");
		assertTrue("Tileset root directory not found!", tilesetRoot.exists());
		assertEquals("Wrong number of x-basetiles calculated for the GMAP tileset!", 16, info.getNumberOfXTiles(0));
		assertEquals("Wrong number of y-basetiles calculated for the GMAP tileset!", 16, info.getNumberOfYTiles(0));
		assertEquals("Wrong number of zoom levels calculated for the GMAP tileset!", 5, info.getZoomLevels());
		
		// execute the validator
		new GoogleMapsValidator().validate(tilesetRoot);
		
		assertTrue("Preview was not generated!" , new File(tilesetRoot,"preview.html").exists());
	}
}

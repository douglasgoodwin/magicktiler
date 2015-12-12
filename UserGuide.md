# Installation #

To install MagickTiler, simply unzip the distribution bundle to your hard disk.

**Important:** MagickTiler uses GraphicsMagick to perform image manipulation (resizing, cropping, etc.) Therefore, you must also install GraphicsMagick from http://www.graphicsmagick.org/ before using MagickTiler!

# CLI and GUI Usage #

MagickTiler can be used from the command line prompt using the command
'java -jar magicktiler.jar', plus appropriate options.

**Example:** `java -jar magicktiler.jar -s tms -f jpeg -p -i images`

The example command will create TMS tilesets (with JPEG tiles) for each file
in the folder 'images'. A preview HTML file will be added to each tileset.

**Command options:**<br>
<blockquote><code>-h</code> .... displays help text<br>
<code>-i</code> ....   input file or directory<br>
<code>-o</code> ....   output directory (for tilesets) or file (for PTIF)<br>
<code>-s</code> ....   selects the tiling scheme ('tms', 'zoomify','gmap' or 'ptif')<br>
<code>-f</code> ....   selects the tile image format ('jpeg' or 'png')<br>
<code>-q</code> ....   sets the JPEG compression quality from 0 (low) to 100 (high)<br>
<code>-b</code> ....   selects a background color (if applicable for the selected tile scheme)<br>
<code>-p</code> ....   generates an HTML preview file<br>
<code>-g</code> ....   starts the GUI<br>
<code>-l</code> ....   writes all relevant reporting information to a log file<br>
<code>-v</code> ....   validate instead of convert: checks existing tilesets and generates a report about their correctness/integrity</blockquote>

<h1>Library Usage</h1>

MagickTiler can also be used as a Java library. Just add the <code>magicktiler-lib-&lt;version&gt;.jar</code> to your build path and you're good to go. Here is a simple code example for generating Google Map tiles. It works in the same way for all other tiling schemes (just replace the <code>GoogleMapsTiler</code> with a <code>TMSTiler</code>, <code>ZoomifyTiler</code>, <code>PTIFTiler</code>, etc.).<br>
<br>
<pre><code>    public static void main(String... args) {<br>
	try {<br>
	    File input = new File("/path/to/your/image.jpg");<br>
	    File output = new File("/tileset/output/path");<br>
	<br>
	    MagickTiler tiler = new GoogleMapsTiler();<br>
            // optional settings (these are the default values)<br>
	    tiler.setTileFormat(ImageFormat.JPEG);<br>
	    tiler.setJPEGCompressionQuality(75);<br>
	    tiler.setBackgroundColor("#ffffffff");<br>
	    tiler.setWorkingDirectory(new File("."));<br>
	    tiler.setGeneratePreviewHTML(true);<br>
		<br>
	    // create the tiles<br>
	    TilesetInfo info = tiler.convert(input, output);<br>
	} catch (TilingException te) {<br>
	    te.printStackTrace();<br>
	}<br>
    }<br>
</code></pre>
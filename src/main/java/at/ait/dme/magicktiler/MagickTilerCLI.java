/*
 * Copyright 2010 Austrian Institute of Technology
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the
 * Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */
package at.ait.dme.magicktiler;

import java.io.File;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * MagickTiler Command-line interface.
 * <br><br>
 * Example usage: <em>java -jar magicktiler.jar -s tms -f jpeg -p -i images</em><br><br>
 * The command will create TMS tilesets (with JPEG tiles) for each file in the
 * folder /images. A preview HTML file will be added to each tileset. 
 * <br><br>
 * Command options:<br>
 * -h   displays this help text<br>
 * -s   tiling scheme ('tms', 'zoomify' or 'ptif')<br>
 * -f   tile format ('jpeg' or 'png')<br>
 * -b   background color<br>
 * -i	input file or directory<br>
 * -o   output directory (for tilesets) or file (for PTIF)<br>
 * -p   generate an HTML preview file
 * 
 * @author aboutgeo@no5.at
 * @author Christian Sadilek <christian.sadilek@gmail.com>
 */
public class MagickTilerCLI {	
	private static final String TARGET_SCHEME_TMS = "TMS tileset";
	private static final String TARGET_SCHEME_ZOOMIFY = "Zoomify tileset";
	private static final String TARGET_SCHEME_PTIF = "Pyramid TIFF";
	private static final String TARGET_FMT_JPEG = "(JPEG tiles)";
	private static final String TARGET_FMT_PNG = "(PNG tiles)";
	
	private static final String VERSION = "Version 0.1-SNAPSHOT";
	private static final String WEBSITE = "http://code.google.com/p/magicktiler";
	
	private static final String USAGE_HEADER =
		"MagickTiler " + VERSION + "\n" + 
		"Copyright (C) 2010 AIT Austrian Institute of Technology.\n" +
		"Additional licences apply to this software.\n" +
		"See " + WEBSITE + " for details.\n";
	private static final String USAGE_FOOTER = 
		"Example: java -jar magicktiler.jar -s tms -f jpeg -i image.tif -p";
	
	/**
	 * @param args
	 * @throws TilingException 
	 */
	public static void main(String[] args) throws TilingException {
		MagickTiler tiler = null;
		String consoleOutScheme = null;
		String consoleOutFormat = "";
		
		Options options = new Options();
		options.addOption(new Option("s", "scheme", "mandatory tiling scheme ('tms', 'zoomify' or 'ptif')", true));
		options.addOption(new Option("i", "input", "mandatory input file or directory", true));
		options.addOption(new Option("o", "output", "output directory (for tilesets) or file (for PTIF)", false));
		options.addOption(new Option("f", "format", "tile format ('jpeg' or 'png'), default=jpeg", false));
		options.addOption(new Option("b", "color", "background color (e.g \"white\")", false));
		options.addOption(new Option("p", null, "generate an HTML preview file", false));
		options.addOption(new Option("h", null, "displays this help text", false));

		try {
			CommandLine cmd = new BasicParser().parse(options, args);
			if(cmd.hasOption("h")) {
				printUsage(options);
				return;
			}
			
			String scheme = cmd.getOptionValue("s");
			if (scheme.equalsIgnoreCase("tms")) {
				tiler = new TMSTiler();
				consoleOutScheme = TARGET_SCHEME_TMS;
				consoleOutFormat = TARGET_FMT_JPEG;
			} else if (scheme.equalsIgnoreCase("zoomify")) {
				tiler = new ZoomifyTiler();
				consoleOutScheme = TARGET_SCHEME_ZOOMIFY;
				consoleOutFormat = TARGET_FMT_JPEG;
			} else if (scheme.equalsIgnoreCase("ptif")) {
				tiler = new PTIFConverter();
				consoleOutScheme = TARGET_SCHEME_PTIF;
			}
			
			if (tiler == null) {
				System.out.println("Unsupported tiling scheme: " + scheme);
				return;
			}
			
			// Tile format
			String format = cmd.getOptionValue("f");
			if (format != null && format.equalsIgnoreCase("png")) {
				tiler.setTileFormat(TileFormat.PNG); 
				consoleOutFormat = TARGET_FMT_PNG;
			}
			
			// Background color
			String background = cmd.getOptionValue("b");
			if (background != null) tiler.setBackgroundColor(background);
			
			// Destination
			File destination = null;
			if (cmd.hasOption("o")) {
				destination = new File(cmd.getOptionValue("o"));
			}
			
			// HTML Preview
			tiler.setGeneratePreviewHTML(cmd.hasOption("p"));
			
			// input filename
			File file = new File(cmd.getOptionValue("i"));
			if (!file.exists()) {
				System.out.println("File not found: " + file.getName());
				return;
			}
			
			generateTiles(tiler, file, destination, consoleOutScheme, consoleOutFormat);
		} catch (ParseException exp) {
			System.err.println("Failed to parse command line arguments: " + exp.getMessage());
			printUsage(options);
		}
	}
		
	private static void generateTiles(MagickTiler tiler, File input, File destination, 
			String consoleOutScheme, String consoleOutFormat) throws TilingException {
		
		long startTime = System.currentTimeMillis();
		System.out.println("Generating " + consoleOutScheme + " from file " + 
				input.getAbsolutePath() + " " + consoleOutFormat);
		if (destination != null) {
			System.out.println("Destination: " + destination.getAbsolutePath());
		}
		
		if (input.isFile()) {
			// Tile single file
			tiler.convert(input, destination);
		} else {
			// Tile folder full of files
			tiler.setWorkingDirectory(input);
			String files[] = input.list();
			for (int i=0; i<files.length; i++) {
				File child = new File(input, files[i]);
				if (child.isFile()) tiler.convert(child, destination);
			}
		}
		
		System.out.println("Done. Took " + (System.currentTimeMillis() - startTime) + " ms.");
	}
	
	private static void printUsage(Options options) {
		System.out.println(USAGE_HEADER);
		new HelpFormatter().printHelp("java -jar magicktiler", "", options, USAGE_FOOTER, true);
	}
	
	private static class Option extends org.apache.commons.cli.Option {
		public Option(String opt, String argName, String description, boolean required) {
			super(opt, (argName!=null), description);
			this.setRequired(required);
			this.setArgName(argName);
		}
	}
}

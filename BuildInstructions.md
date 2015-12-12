# How to Build MagickTiler from Source #

MagickTiler is built using Ant. The following build targets exist:

> - build:dist ... creates the MagickTiler distribution bundle<br>
<blockquote>- build:lib .... creates the embeddable library file without dependencies<br>
- test ......... runs JUnit tests<br>
- doc .......... generates JavaDoc<br></blockquote>

The distribution bundle includes an executable .jar file (called <code>magicktiler.jar</code>) with all dependencies included. Use this file in case you want to use MagickTiler as a command-line tool.<br>
<br>
The MagickTiler embeddable library file (called <code>magicktiler-lib-&lt;version&gt;.jar</code>) is for use in your own Java applications. It does not include any external dependencies. To use MagickTiler in your own applications, you need to add the embeddable library .jar and the following dependencies to your project build path:<br>
<ul><li><code>im4java-1.1.0.jar</code>
</li><li><code>log4j-1.2.15.jar</code>
</li><li><code>commons-cli-1.2.jar</code>
</li><li><code>xstream-1.3.1.jar</code>
</li><li><code>scala-library.jar (optional)</code>
</li><li><code>scala-swing.jar (optional)</code></li></ul>

All these files can be found in the /lib folder of the MagickTiler project.<br>
<br>
If you like to build the executable or the embeddable jar without the dependency to Scala, you will have to exclude the GUI from the build. You can achieve this by passing the <code>-Dinclude.gui=false</code> property to Ant.
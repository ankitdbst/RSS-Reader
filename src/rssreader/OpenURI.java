/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rssreader;

/**
 *
 * @author victor
 */

public class OpenURI {

    public OpenURI(String browserURI){

        if( !java.awt.Desktop.isDesktopSupported() ) {

            System.err.println( "Desktop is not supported(fatal)");
            System.exit( 1 );
        }

        java.awt.Desktop desktop = java.awt.Desktop.getDesktop();

        if( !desktop.isSupported( java.awt.Desktop.Action.BROWSE ) ) {

            System.exit( 1 );
        }

        try {

            java.net.URI uri = new java.net.URI(browserURI);
            desktop.browse( uri );
        }
        catch ( Exception e ) {

            System.err.println( e.getMessage() );
        }
    }
}

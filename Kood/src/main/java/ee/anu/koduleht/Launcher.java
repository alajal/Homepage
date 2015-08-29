package ee.anu.koduleht;

import ee.anu.server.Divider;
import ee.anu.server.MyServer;
import ee.anu.server.handlers.FileHandler;

import java.io.IOException;

public class Launcher {
    public static void main(String[] args) throws IOException {
        System.out.println(Launcher.class.getName());
        Divider divider = new Divider();
        divider.addFixedHandler("/rongid", new Rongid());
         //esimene parameeter on divideri jaoks; FileHandleri parameetrid on ainult FailHandlerile
        ArtRepository artRepository = new ArtRepository(new DataSource());
        FileHandler fileHandler = new FileHandler("web", "/");
        divider.addWildHandler("/", new SmarterFileHandler(fileHandler, artRepository));
        divider.addWildHandler("/assets", new ArtImgHandler(fileHandler, artRepository));
        divider.addFixedHandler("/art.html", new ArtHtmlHandler(fileHandler, artRepository));
        int p;
        String port = System.getenv("PORT");
        if (port != null ){
            p = Integer.parseInt(port);
        } else {
            p = 8080;
        }
        MyServer.serve(divider, p);
    }

}


//kõik failid on jar faili sees
//javavirtual machine teab kus on jar - laeb klassid, classpath on asukoht, kust jvm faile oskab otsida
//classloader - oskab kõikide jaride seest faile otsida, classpath võib koosneda mitmest jarist
